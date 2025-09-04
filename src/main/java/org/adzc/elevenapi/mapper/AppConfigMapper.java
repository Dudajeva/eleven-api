package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.domain.AppConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppConfigMapper {
    AppConfig selectById(@Param("id") Long id);

    AppConfig selectByKey(@Param("cfgKey") String cfgKey);

    List<AppConfig> selectAll();

    int insert(AppConfig config);

    int update(AppConfig config);

    int deleteById(@Param("id") Long id);
}
