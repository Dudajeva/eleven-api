// src/main/java/org/adzc/elevenapi/geo/GeoService.java
package org.adzc.elevenapi.geo;

import org.adzc.elevenapi.mapper.GeoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeoService {

    private final GeoMapper geoMapper;

    // 可在 application.properties 调整，默认 7 天
    @Value("${geo.cache.ttl-seconds:604800}")
    private long ttlSeconds;

    public GeoService(GeoMapper geoMapper) {
        this.geoMapper = geoMapper;
    }

    public GeoResponse loadRegions() {
        var provinces = geoMapper.selectProvinces();
        List<GeoResponse.Region> regions = new ArrayList<>(provinces.size());

        for (var p : provinces) {
            var cityRows = geoMapper.selectCitiesByProvince(p.getId());
            List<String> cities = new ArrayList<>(cityRows.size());
            for (var c : cityRows) cities.add(c.getName());
            regions.add(new GeoResponse.Region(p.getName(), cities));
        }

        OffsetDateTime max = geoMapper.selectMaxUpdatedAt();
        String version = max == null ? "v0" : max.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String updatedAt = max == null ? "" : max.toString();

        GeoResponse resp = new GeoResponse();
        resp.setVersion(version);
        resp.setUpdatedAt(updatedAt);
        resp.setRegions(regions);

        // 关键：由后端明确告知缓存多久过期
        resp.setCacheTtlSeconds(ttlSeconds);
        resp.setExpiresAt(Instant.now().plusSeconds(ttlSeconds).toEpochMilli());
        return resp;
    }
}
