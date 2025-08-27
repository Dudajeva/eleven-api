package org.adzc.elevenapi.feed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 首页卡片流（演示数据）
 * 说明：
 *  - 结构先稳定下来：id、name、age、city、tier、photoUrl
 *  - 后续切换到 MyBatis + MySQL 时，仅替换数据来源即可
 */
@RestController
public class FeedController {

    @GetMapping("/api/feed")
    public List<Map<String, Object>> feed() {
        return List.of(
                Map.of(
                        "id", "u1001",
                        "name", "我是用户名",
                        "age", 32,
                        "city", "上海",
                        "tier", "diamond",
                        "photoUrl", "https://www.cpophome.com/wp-content/uploads/2020/11/Zhao-Liying.jpg"      // 暂无图，用前端占位粉色
                ),
                Map.of(
                        "id", "u1002",
                        "name", "我是用户名",
                        "age", 29,
                        "city", "上海",
                        "tier", "normal",
                        "photoUrl", "https://www.cpophome.com/wp-content/uploads/2020/11/Zhao-Liying.jpg"
                ),
                Map.of(
                        "id", "u1003",
                        "name", "我是用户名",
                        "age", 24,
                        "city", "上海",
                        "tier", "normal",
                        "photoUrl", "https://www.cpophome.com/wp-content/uploads/2020/11/Zhao-Liying.jpg"
                ),
                Map.of(
                        "id", "u1004",
                        "name", "我是用户名",
                        "age", 27,
                        "city", "上海",
                        "tier", "supreme",
                        "photoUrl", "https://www.cpophome.com/wp-content/uploads/2020/11/Zhao-Liying.jpg"
                )
        );
    }
}
