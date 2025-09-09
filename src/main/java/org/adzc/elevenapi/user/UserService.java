package org.adzc.elevenapi.user;

import org.adzc.elevenapi.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    private final UserMapper userMapper;
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Map<String,Object> detail(Long userId, Long selfId){
        // 查用户基本信息 + profile + membership
        return userMapper.findDetail(userId);
    }
}
