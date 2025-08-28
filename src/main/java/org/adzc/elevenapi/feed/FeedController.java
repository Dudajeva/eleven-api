package org.adzc.elevenapi.feed;

import org.adzc.elevenapi.common.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/api/feed")
    public List<UserCard> feed(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(required = false) String province,
                               @RequestParam(required = false) String city) {
        return feedService.list(page, size, province, city);
    }

    @GetMapping("/api/feed/page")
    public PageResult<UserCard> feedPage(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "20") int size,
                                         @RequestParam(required = false) String province,
                                         @RequestParam(required = false) String city) {
        return feedService.page(page, size, province, city);
    }
}
