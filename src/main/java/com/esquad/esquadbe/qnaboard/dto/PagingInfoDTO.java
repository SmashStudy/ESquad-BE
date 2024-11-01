package com.esquad.esquadbe.qnaboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingInfoDTO {
    private int totalPosts;      // 모든 글 개수
    private int currentPage;     // 현재 페이지 번호
    private int postsPerPage;    // 한 페이지당 표시할 글 개수
    private int displayPageNum;  // 한 번에 표시할 페이지 개수

    // 총 페이지 수 계산
    public int getTotalPages() {
        return (int) Math.ceil((double) totalPosts / postsPerPage);
    }

    // 현재 페이지 그룹 계산
    public int getCurrentPageGroup() {
        return (int) Math.ceil((double) currentPage / displayPageNum);
    }

    // 시작 페이지 번호 계산
    public int getStartPage() {
        return (getCurrentPageGroup() - 1) * displayPageNum + 1;
    }

    // 끝 페이지 번호 계산
    public int getEndPage() {
        return Math.min(getStartPage() + displayPageNum - 1, getTotalPages());
    }
}