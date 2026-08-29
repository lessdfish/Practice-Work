package com.mall.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ClassName:RestAccessDeniedHandler
 * Package:com.mall.security
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 14:02
 * @Version: v1.0
 *
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setCharacterEncoding("UTF-8");

        Result<Void> result = Result.fail(40300,"Request Forbidden");

        objectMapper.writeValue(response.getWriter(),result);
    }
}
