package org.adzc.elevenapi.config;

import org.adzc.elevenapi.common.PageResult;
import org.adzc.elevenapi.domain.AppConfig;
import org.adzc.elevenapi.mapper.AppConfigMapper;
import org.adzc.elevenapi.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

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
