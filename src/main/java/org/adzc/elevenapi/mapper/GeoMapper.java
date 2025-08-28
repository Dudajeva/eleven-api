package org.adzc.elevenapi.mapper;

import org.adzc.elevenapi.geo.RegionRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface GeoMapper {

    long countRegions();

    int insertRegion(RegionRow row);                     // useGeneratedKeys -> row.id

    List<RegionRow> selectProvinces();                   // level='province'
    List<RegionRow> selectCitiesByProvince(@Param("provinceId") long provinceId);

    OffsetDateTime selectMaxUpdatedAt();                 // 版本用
}
