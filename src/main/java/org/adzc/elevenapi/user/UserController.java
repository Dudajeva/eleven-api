package org.adzc.elevenapi.user;

import org.adzc.elevenapi.auth.CurrentUid;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取用户资料
     * @param userId 查询对象ID
     * @param selfId 当前登录人ID（自动注入）
     */
    @GetMapping("/{userId}")
    public Map<String,Object> detail(@PathVariable Long userId, @CurrentUid Long selfId) {
        return userService.detail(userId, selfId);
    }
}
