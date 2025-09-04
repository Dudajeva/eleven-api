package org.adzc.elevenapi.config;

import org.adzc.elevenapi.domain.AppConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/config")
public class AppConfigController {

    private final AppConfigService appConfigService;

    public AppConfigController(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }


    @GetMapping("/{key}")
    public Map<String, String> get(@PathVariable("key") String key) {
        AppConfig config = appConfigService.selectByKey(key); // null 表示没配
        Map<String, String> resp = new HashMap<>();
        resp.put("key", key);
        resp.put("value", config != null ? config.getCfgValue() : "");
        return resp;
    }
}

