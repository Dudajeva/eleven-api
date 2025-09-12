package org.adzc.elevenapi.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 标准分页返回：/api/feed/page 使用
 */
@Data
@AllArgsConstructor
public class PageResult<T> {
    private List<T> list;
    private int page;
    private int size;
    private long total;

    public boolean isHasNext() {
        return (long) page * size < total;
    }
}
