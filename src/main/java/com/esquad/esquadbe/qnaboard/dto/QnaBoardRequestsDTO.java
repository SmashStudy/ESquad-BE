
    // 정적 팩토리 메서드 추가
    public static QnaBoardRequestsDTO fromEntity(BookQnaBoard bookQnaBoard) {
        return QnaBoardRequestsDTO.builder()
                .id(bookQnaBoard.getId())
                .writer(bookQnaBoard.getWriter())
                .title(bookQnaBoard.getTitle())
                .book(bookQnaBoard.getBook())
                .content(bookQnaBoard.getContent())
                .likes(bookQnaBoard.getLikes())
                .build();
    }

}