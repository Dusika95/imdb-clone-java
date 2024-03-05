package org.imdb.clone.DTOs;

import java.util.List;

public class ReviewListDto {
    private int pageIndex;
    private int pageCount;
    private Long total;
    private List<ReviewDto> reviewList;

    public ReviewListDto() {
    }

    public ReviewListDto(int pageIndex, int pageCount, Long total, List<ReviewDto> reviewList) {
        this.pageIndex = pageIndex;
        this.pageCount = pageCount;
        this.total = total;
        this.reviewList = reviewList;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<ReviewDto> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<ReviewDto> reviewList) {
        this.reviewList = reviewList;
    }
}

