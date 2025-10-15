package org.dows.member.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Objects;

@Schema(name = "分页查询")
@Data
public class PageQuery {


    /**
     * 分页大小
     */
    @Schema(description = "分页大小")
    private Integer pageSize;

    /**
     * 当前页数
     */
    @Schema(description = "当前页数")
    private Integer pageNum;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;


    /**
     * 每页显示记录数 默认值 默认每页20条
     */
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    public PageQuery build() {
        this.pageNum = Objects.isNull(this.pageNum) || this.pageNum < 1 ? DEFAULT_PAGE_NUM : this.pageNum;
        this.pageSize = Objects.isNull(this.pageSize) ||  this.pageSize < 1 ? DEFAULT_PAGE_SIZE : this.pageSize;
        this.pageSize = this.pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : this.pageSize;
        return this;
    }

    public long offset() {
        return this.getPageSize() * (this.getPageNum() - 1L);
    }

}
