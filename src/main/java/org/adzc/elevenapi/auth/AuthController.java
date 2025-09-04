package org.adzc.elevenapi.auth;

import jakarta.validation.constraints.NotBlank;
import org.adzc.elevenapi.auth.dto.RegisterRequest;
import org.adzc.elevenapi.domain.User;
import org.adzc.elevenapi.domain.UserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器（数据库版）
 * - 接口：POST /api/auth/login
 * - 请求：{ "identity": "<邮箱或手机号>", "password": "<明文>" }
 * - 成功：{ "token", "userId", "identity", "nickname" }
 */
@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin(origins = "*") // 开发期放开，生产记得收紧
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public static class LoginRequest {
        @NotBlank(message = "identity 不能为空（邮箱或电话）")
        public String identity;
        @NotBlank(message = "password 不能为空")
        public String password;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        User user = authService.register(req);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody @Validated LoginRequest req) {
        try {
            AuthService.LoginResult r = authService.login(req.identity, req.password);
            User u = r.getUser();
            UserProfile userProfile = r.getUserProfile();

            Map<String, Object> resp = new HashMap<>();
            resp.put("token", r.getToken());
            resp.put("userId", "u_" + u.getId());
            // 统一回传“登录用的 identity”（邮箱优先，否则手机号；若两者皆空则回请求值）
            String idVal = (u.getIdentity() != null && !u.getIdentity().isBlank())
                    ? u.getIdentity()
                    : (u.getEmail() != null && !u.getEmail().isBlank())
                    ? u.getEmail()
                    : (u.getPhone() != null ? u.getPhone() : req.identity);
            resp.put("identity", idVal);
            resp.put("nickname", u.getNickname());
            resp.put("firstLogin",userProfile.getFirstLogin());
            // 性别这轮先不返回；后续需要时可加 resp.put("gender", u.getGender());
            return resp;

        } catch (IllegalArgumentException bad) {
            throw new BadRequestException("用户名或密码错误");
        }
    }

    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    static class BadRequestException extends RuntimeException {
        public BadRequestException(String msg) { super(msg); }
    }
}
