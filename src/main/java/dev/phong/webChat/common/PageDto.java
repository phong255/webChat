package dev.phong.webChat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private Integer pageIndex;
    private Integer pageSize;
    private int totalPages;
    private int totalRecords;
    private int beginIndex;
    private int endIndex;
    private List<T> data;

    public static <T> PageDto<T> of(Page page, List<T> data) {
        PageDto<T> pageDto = new PageDto<>();
        pageDto.pageIndex = page.getPageable().getPageNumber() + 1;
        pageDto.pageSize = page.getPageable().getPageSize();
        pageDto.totalPages = page.getTotalPages();
        pageDto.totalRecords = Math.toIntExact(page.getTotalElements());
        pageDto.beginIndex = Math.toIntExact(page.getPageable().getOffset());
        pageDto.endIndex = Math.toIntExact(page.getPageable().getOffset() + page.getNumberOfElements());
        pageDto.data = data;
        return pageDto;
    }
}