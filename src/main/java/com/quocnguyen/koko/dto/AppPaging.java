package com.quocnguyen.koko.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Quoc Nguyen on {2024-08-17}
 */

@Builder
@Data
public class AppPaging<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPages;
    private Long total;
    private List<T> list;

    public static<T> AppPaging<T> convert(Page<T> page) {
        return AppPaging.<T>builder()
                .pageNum(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .list(page.getContent())
                .build();

    }

    public static<T, H> AppPaging<T> convertExcludeContent(Page<H> page) {
        return AppPaging.<T>builder()
                .pageNum(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .total(page.getTotalElements())
                .build();
    }
}
