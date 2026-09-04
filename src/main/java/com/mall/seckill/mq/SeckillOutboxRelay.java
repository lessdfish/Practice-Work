package com.mall.seckill.mq;

import com.mall.common.redis.RedisKeyConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:SeckillOutboxRelay
 * Package:com.mall.seckill.mq
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/1 - 23:17
 * @Version: v1.0
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SeckillOutboxRelay {
    private static final String GROUP = RedisKeyConstants.SECKILL_OUTBOX_GROUP;
    public static final String REGISTRY = RedisKeyConstants.SECKILL_OUTBOX_STREAM_REGISTRY;
    public static final Duration CLAIM_IDLE = Duration.ofSeconds(30);

    private final Set<String> initializedStreams = ConcurrentHashMap.newKeySet();
    private final StringRedisTemplate redisTemplate;
    private final SeckillOrderProducer producer;

    private final String consumerName = "relay-" + UUID.randomUUID().toString().substring(0,8);
    //TODO 临时容量，之后需结合Redis容量、单条消息大小、业务峰值设值
    private static final long STREAM_WARN_SIZE = 10_000;
    private static final long STREAM_CRITICAL_SIZE = 50_000;

    @Scheduled(fixedDelay = 300)
    public void relayNewMessages(){
        for (String stream: streams()){
            ensureGroup(stream);
            relayNewMessage(stream);
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void recoverPendingMessages(){
        for (String stream : streams()) {
            ensureGroup(stream);
            recoverPendingMessage(stream);
        }
    }

    @Scheduled(fixedDelay = 10_000)
    public void monitorOutbox(){
        for (String stream : streams()) {
            Long size = ops().size(stream);

            if (size!=null && size >= STREAM_CRITICAL_SIZE) {
                log.error("秒杀Outbox严重积压，stream={},size={}",stream,size);
            }else if (size!=null && size >=STREAM_WARN_SIZE){
                log.warn("秒杀Outbox出现积压，stream={},size={}",stream,size);
            }
        }
    }

    private StreamOperations<String,Object,Object> ops() {
        return redisTemplate.opsForStream();
    }

    private void publish(String stream,MapRecord<String,Object,Object> record){
        var message = toMessage(record);
        var result = producer.send(message);

        if (result!= MqPublishResult.CONFIRMED) {
            log.warn("Outbox投递未确认，requestId={},result={}",
                    message.getRequestId(),result);
            return;
        }

        ops().acknowledge(stream,GROUP,record.getId());

        ops().delete(stream,record.getId());
        log.info("Outbox投递成功，requestId={},stream={}",message.getRequestId(),stream);
    }

    private SeckillOrderMessage toMessage(MapRecord<String,Object,Object> record){
        Map<Object,Object> data = record.getValue();
        return new SeckillOrderMessage(data.get("requestId").toString(),
                Long.valueOf(data.get("activityId").toString()),
                Long.valueOf(data.get("userId").toString()));
    }

    private Set<String> streams(){
        Set<String> streams = redisTemplate.opsForSet().members(REGISTRY);

        return streams == null ?Set.of() :streams;
    }

    private void relayNewMessage(String stream){
        var records = ops().read(
                Consumer.from(GROUP,consumerName),
                StreamReadOptions.empty().count(20),
                StreamOffset.create(stream,ReadOffset.lastConsumed())
        );

        if (records == null || records.isEmpty()) {
            return;
        }

        records.forEach(record -> publish(stream,record));
    }

    private void ensureGroup(String stream){
        if (!initializedStreams.add(stream)) {
            return;
        }

        try {
            ops().createGroup(stream,ReadOffset.from("0-0"),GROUP);
            log.info("创建 Outbox Consumer Group,stream={}",stream);
        }catch (RuntimeException e){
            if (!String.valueOf(e.getMessage()).contains("BUSYGROUP")) {
                initializedStreams.remove(stream);
                throw e;
            }
        }
    }

    private void recoverPendingMessage(String stream){
        var pending = ops().pending(stream,GROUP, Range.unbounded(),100);
        if (pending == null || pending.isEmpty()) {
            return;
        }

        var ids = new ArrayList<RecordId>();

        for (var message : pending){
            if (message.getElapsedTimeSinceLastDelivery().compareTo(CLAIM_IDLE) >= 0) {
                ids.add(message.getId());
            }
        }

        if (ids.isEmpty()){
            return;
        }

        var claimed = ops().claim(stream,GROUP,consumerName,CLAIM_IDLE,ids.toArray(RecordId[]::new));

        if (claimed!=null) {
            claimed.forEach(record -> publish(stream,record));
        }
    }

}
