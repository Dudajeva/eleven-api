package org.adzc.elevenapi.geo;

/**
 * 仅用于 MyBatis 插入/查询的简单行对象（与单表 regions 对应）
 */
public class RegionRow {
    private Long id;
    private String name;
    private Long parentId;   // 省为 null，市为省 id
    private String level;    // "province" or "city"

    public RegionRow() {}

    public RegionRow(String name, Long parentId, String level) {
        this.name = name;
        this.parentId = parentId;
        this.level = level;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
