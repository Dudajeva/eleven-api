package org.adzc.elevenapi.config;

import org.adzc.elevenapi.domain.AppConfig;
import org.adzc.elevenapi.mapper.AppConfigMapper;
import org.springframework.stereotype.Service;

@Service
public class AppConfigService {

    private final AppConfigMapper appConfigMapper;

    public AppConfigService(AppConfigMapper appConfigMapper) {
        this.appConfigMapper = appConfigMapper;
    }

    public AppConfig selectByKey(String key){
        return appConfigMapper.selectByKey(key);
    }

}
