package org.adzc.elevenapi.user;

import org.adzc.elevenapi.domain.UserProfile;
import org.springframework.http.MediaType;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    /** 从请求中取 uid：你已有 JWT 过滤器可把 uid 放到 request attribute；否则用开发兜底 X-UID 头 */
    private long uid(HttpServletRequest req) {
        Object v = req.getAttribute("auth.uid"); // ✅ 与过滤器一致
        if (v instanceof Number) return ((Number) v).longValue();
        if (v != null) return Long.parseLong(String.valueOf(v));

        Object v2 = req.getAttribute("uid");
        if (v2 instanceof Number) return ((Number) v2).longValue();
        if (v2 != null) return Long.parseLong(String.valueOf(v2));

        String hdr = req.getHeader("X-UID");
        if (hdr != null && !hdr.isBlank()) return Long.parseLong(hdr);

        throw new RuntimeException("Unauthorized: uid not found");
    }

    @GetMapping("/me")
    public UserProfile me(HttpServletRequest req) {
        return service.getOrInit(uid(req));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@RequestBody UserProfile body, HttpServletRequest req) {
        body.setUserId(uid(req));
        service.save(body);
        return "OK";
    }

    @PostMapping(path = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {
        if (file.isEmpty()) throw new RuntimeException("空文件");
        if (file.getSize() > DataSize.ofMegabytes(8).toBytes()) throw new RuntimeException("文件过大(>8MB)");

        File dir = new File("uploads/avatar");
        if (!dir.exists() && !dir.mkdirs()) throw new RuntimeException("无法创建目录");

        String ext = ".bin";
        String ct = file.getContentType();
        if ("image/png".equals(ct))  ext = ".png";
        if ("image/jpeg".equals(ct)) ext = ".jpg";

        String name = uid(req) + "-" + LocalDateTime.now().toString().replace(":", "-") + ext;
        File dest = new File(dir, name);
        file.transferTo(dest);

        String url = "/uploads/avatar/" + name;
        service.updateAvatar(uid(req), url);
        return url; // 返回可直接展示的 URL
    }
}
