package org.adzc.elevenapi.geo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeoController {

    private final GeoService geoService;

    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }

    /**
     * 地区列表（省/直辖市 + 城市）
     * GET /api/geo/regions
     * 返回 { version, updatedAt, regions: [{name, cities:[]}, ...] }
     */
    @GetMapping("/api/geo/regions")
    public GeoResponse regions() {
        GeoResponse gres = geoService.loadRegions();
        return gres;
    }
}
