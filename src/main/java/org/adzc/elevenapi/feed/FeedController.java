package org.adzc.elevenapi.feed;

import lombok.RequiredArgsConstructor;
import org.adzc.elevenapi.auth.CurrentUid;
import org.adzc.elevenapi.feed.dto.FeedItemDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    // 列表：?page=1&size=20&province=&city=
    @GetMapping
    public List<FeedItemDTO> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @CurrentUid Long userId
    ) {
        int offset = Math.max(0, (page - 1) * size);
         List<FeedItemDTO> feedList=feedService.list(userId, offset, size, province, city);
        return feedList;
    }

    // 发布动态
    @PostMapping
    public void create(@RequestBody CreateFeedReq req, @CurrentUid Long userId) {
        feedService.create(userId, req.getText(), req.getPhotoUrl());
    }

    // 点赞
    @PostMapping("/{id}/like")
    public void like(@PathVariable Long id, @CurrentUid Long userId) {
        feedService.like(userId, id);
    }

    // 取消点赞
    @DeleteMapping("/{id}/like")
    public void unlike(@PathVariable Long id, @CurrentUid Long userId) {
        feedService.unlike(userId, id);
    }

    // --- 内部请求体 ---
    @lombok.Data
    public static class CreateFeedReq {
        private String text;
        private String photoUrl;
    }
}