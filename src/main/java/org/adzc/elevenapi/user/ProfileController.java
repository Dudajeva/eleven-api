package org.adzc.elevenapi.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ProfileController {

    @GetMapping("/api/me")
    public Map<String, Object> me(HttpServletRequest req) {
        // 由 JwtAuthFilter 塞入的属性
        return Map.of(
                "userId", String.valueOf(req.getAttribute("auth.uid")),
                "identity", String.valueOf(req.getAttribute("auth.identity")),
                "nickname", String.valueOf(req.getAttribute("auth.nick"))
        );
    }
}
