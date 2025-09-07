package org.adzc.elevenapi.home;

import org.adzc.elevenapi.auth.CurrentUid;
import org.adzc.elevenapi.home.dto.UserCard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/api/home")
    public List<UserCard> feed(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(required = false) String province,
                               @RequestParam(required = false) String city,
                               @CurrentUid Long userId) {

        return homeService.list(page, size, province, city,userId);
    }
}
