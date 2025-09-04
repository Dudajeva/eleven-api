package org.adzc.elevenapi.user;

import org.adzc.elevenapi.auth.CurrentUid;
import org.adzc.elevenapi.domain.UserProfile;
import org.springframework.http.MediaType;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }


    @GetMapping("/me")
    public UserProfile me(@CurrentUid Long uid) {
        return service.getOrInit(uid);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@RequestBody UserProfile body,@CurrentUid Long uid) {
        body.setUserId(uid);
        service.save(body);
        return "OK";
    }

    @PostMapping(path = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest req,@CurrentUid Long uid) throws Exception {
        if (file.isEmpty()) throw new RuntimeException("空文件");
        if (file.getSize() > DataSize.ofMegabytes(8).toBytes()) throw new RuntimeException("文件过大(>8MB)");

        File dir = new File("uploads/avatar");
        if (!dir.exists() && !dir.mkdirs()) throw new RuntimeException("无法创建目录");

        String ext = ".bin";
        String ct = file.getContentType();
        if ("image/png".equals(ct))  ext = ".png";
        if ("image/jpeg".equals(ct)) ext = ".jpg";

        String name = uid + "-" + LocalDateTime.now().toString().replace(":", "-") + ext;
        File dest = new File(dir, name);
        file.transferTo(dest);

        String url = "/uploads/avatar/" + name;
        service.updateAvatar(uid, url);
        return url; // 返回可直接展示的 URL
    }

    @PatchMapping("/hide")
    public ResponseEntity<?> setHidePatch(@RequestBody Map<String, Object> body,@CurrentUid Long uid) {
        boolean hide = Boolean.TRUE.equals(body.get("hide")) ||
                "true".equalsIgnoreCase(String.valueOf(body.get("hide")));
        service.updateHidePhotos(uid, hide);
        return ResponseEntity.ok(Map.of("hide", hide));
    }
}
