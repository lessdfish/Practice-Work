package com.mall.security;

import com.mall.user.domain.User;
import com.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ClassName:MallUserDetailService
 * Package:com.mall.security
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 13:05
 * @Version: v1.0
 *
 */
@Service
public class MallUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return new MallUserDetails(user);
    }
}
