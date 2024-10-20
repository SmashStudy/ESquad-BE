package com.esquad.esquadbe.qnaboard.dto;


import com.esquad.esquadbe.qnaboard.entity.BookQnaBoard;
import com.esquad.esquadbe.studypage.entity.Book;
import com.esquad.esquadbe.team.entity.TeamSpace;
import com.esquad.esquadbe.user.entity.User;
import lombok.Builder;

@Builder
public record QnaRequestDTO(
        Long id,
        String title,
        String content,
        Long userId,
        Long bookId,
        Long teamSpaceId
) {
    public BookQnaBoard to(User user, Book book, TeamSpace teamSpace) {
        return BookQnaBoard.builder()
                .title(title)
                .content(content)
                .writer(user)
                .book(book)
                .teamSpace(teamSpace)
                .likes(0)
                .build();

    }
}
