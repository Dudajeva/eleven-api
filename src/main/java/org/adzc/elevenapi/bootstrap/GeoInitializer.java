package org.adzc.elevenapi.bootstrap;

import org.adzc.elevenapi.geo.RegionRow;
import org.adzc.elevenapi.mapper.GeoMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 若 regions 为空，插入少量演示数据（北京/上海/广东/浙江）
 * 便于前端立即可用；后续你再批量导入全量数据
 */
@Component
public class GeoInitializer implements ApplicationRunner {

    private final GeoMapper geoMapper;

    public GeoInitializer(GeoMapper geoMapper) {
        this.geoMapper = geoMapper;
    }
    @Override
    public void run(ApplicationArguments args) {
        if (geoMapper.countRegions() > 0) return;

        // 省份 -> 城市列表（两级；直辖市/SAR只保留自身为城市）
        var data = new java.util.LinkedHashMap<String, java.util.List<String>>();

        // —— 直辖市 ——
        data.put("北京市", java.util.List.of("北京"));
        data.put("上海市", java.util.List.of("上海"));
        data.put("天津市", java.util.List.of("天津"));
        data.put("重庆市", java.util.List.of("重庆"));

        // —— 省 ——
        data.put("河北省", java.util.List.of("石家庄","唐山","秦皇岛","邯郸","邢台","保定","张家口","承德","沧州","廊坊","衡水"));
        data.put("山西省", java.util.List.of("太原","大同","阳泉","长治","晋城","朔州","晋中","运城","忻州","临汾","吕梁"));
        data.put("辽宁省", java.util.List.of("沈阳","大连","鞍山","抚顺","本溪","丹东","锦州","营口","阜新","辽阳","盘锦","铁岭","朝阳","葫芦岛"));
        data.put("吉林省", java.util.List.of("长春","吉林","四平","辽源","通化","白山","松原","白城","延边"));
        data.put("黑龙江省", java.util.List.of("哈尔滨","齐齐哈尔","牡丹江","佳木斯","大庆","伊春","鸡西","鹤岗","双鸭山","七台河","黑河","绥化","大兴安岭"));
        data.put("江苏省", java.util.List.of("南京","无锡","徐州","常州","苏州","南通","连云港","淮安","盐城","扬州","镇江","泰州","宿迁"));
        data.put("浙江省", java.util.List.of("杭州","宁波","温州","嘉兴","湖州","绍兴","金华","衢州","舟山","台州","丽水"));
        data.put("安徽省", java.util.List.of("合肥","芜湖","蚌埠","淮南","马鞍山","淮北","铜陵","安庆","黄山","滁州","阜阳","宿州","六安","亳州","池州","宣城"));
        data.put("福建省", java.util.List.of("福州","厦门","莆田","三明","泉州","漳州","南平","龙岩","宁德"));
        data.put("江西省", java.util.List.of("南昌","景德镇","萍乡","九江","新余","鹰潭","赣州","吉安","宜春","抚州","上饶"));
        data.put("山东省", java.util.List.of("济南","青岛","淄博","枣庄","东营","烟台","潍坊","济宁","泰安","威海","日照","临沂","德州","聊城","滨州","菏泽"));
        data.put("河南省", java.util.List.of("郑州","开封","洛阳","平顶山","安阳","鹤壁","新乡","焦作","濮阳","许昌","漯河","三门峡","南阳","商丘","信阳","周口","驻马店","济源"));
        data.put("湖北省", java.util.List.of("武汉","黄石","十堰","宜昌","襄阳","鄂州","荆门","孝感","荆州","黄冈","咸宁","随州","恩施","仙桃","潜江","天门","神农架"));
        data.put("湖南省", java.util.List.of("长沙","株洲","湘潭","衡阳","邵阳","岳阳","常德","张家界","益阳","郴州","永州","怀化","娄底","湘西"));
        data.put("广东省", java.util.List.of("广州","深圳","珠海","汕头","佛山","江门","湛江","茂名","肇庆","惠州","梅州","汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮"));
        data.put("海南省", java.util.List.of("海口","三亚","三沙","儋州"));
        data.put("四川省", java.util.List.of("成都","自贡","攀枝花","泸州","德阳","绵阳","广元","遂宁","内江","乐山","南充","眉山","宜宾","广安","达州","雅安","巴中","资阳","阿坝","甘孜","凉山"));
        data.put("贵州省", java.util.List.of("贵阳","六盘水","遵义","安顺","毕节","铜仁","黔西南","黔东南","黔南"));
        data.put("云南省", java.util.List.of("昆明","曲靖","玉溪","保山","昭通","丽江","普洱","临沧","楚雄","红河","文山","西双版纳","大理","德宏","怒江","迪庆"));
        data.put("陕西省", java.util.List.of("西安","铜川","宝鸡","咸阳","渭南","延安","汉中","榆林","安康","商洛"));
        data.put("甘肃省", java.util.List.of("兰州","嘉峪关","金昌","白银","天水","武威","张掖","平凉","酒泉","庆阳","定西","陇南","临夏","甘南"));
        data.put("青海省", java.util.List.of("西宁","海东","海北","黄南","海南","果洛","玉树","海西"));
        data.put("台湾省", java.util.List.of("台北","新北","桃园","台中","台南","高雄","基隆","新竹","嘉义","宜兰","苗栗","彰化","南投","云林","嘉义县","屏东","台东","花莲","澎湖"));

        // —— 自治区 ——
        data.put("内蒙古自治区", java.util.List.of("呼和浩特","包头","乌海","赤峰","通辽","鄂尔多斯","呼伦贝尔","巴彦淖尔","乌兰察布","兴安盟","锡林郭勒盟","阿拉善盟"));
        data.put("广西壮族自治区", java.util.List.of("南宁","柳州","桂林","梧州","北海","防城港","钦州","贵港","玉林","百色","贺州","河池","来宾","崇左"));
        data.put("西藏自治区", java.util.List.of("拉萨","昌都","山南","日喀则","那曲","阿里","林芝"));
        data.put("宁夏回族自治区", java.util.List.of("银川","石嘴山","吴忠","固原","中卫"));
        data.put("新疆维吾尔自治区", java.util.List.of("乌鲁木齐","克拉玛依","吐鲁番","哈密","昌吉","博尔塔拉","巴音郭楞","阿克苏","克孜勒苏","喀什","和田","伊犁","塔城","阿勒泰"));

        // —— 特别行政区（可选，如不需要可删除） ——
        data.put("香港特别行政区", java.util.List.of("香港"));
        data.put("澳门特别行政区", java.util.List.of("澳门"));

        // 插入：先省，再城（城的 parentId 指向省）
        data.forEach((provinceName, cities) -> {
            var prov = new RegionRow(provinceName, null, "province");
            geoMapper.insertRegion(prov);
            var pid = prov.getId(); // 确保你的 mapper/ORM 能回填主键
            for (var c : cities) {
                geoMapper.insertRegion(new RegionRow(c, pid, "city"));
            }
        });

        System.out.println("[GeoInitializer] seeded full China regions (province/city, single-table)");
    }

}
