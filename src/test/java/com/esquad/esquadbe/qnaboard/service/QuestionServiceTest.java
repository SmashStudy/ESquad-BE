package com.esquad.esquadbe.qnaboard.service;

import com.esquad.esquadbe.qnaboard.repository.QuestionRepository;
import com.esquad.esquadbe.storage.service.S3FileService;
import com.esquad.esquadbe.studypage.repository.BookRepository;
import com.esquad.esquadbe.team.entity.repository.TeamSpaceRepository;
import com.esquad.esquadbe.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class QuestionServiceTest {


    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamSpaceRepository teamSpaceRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    S3FileService s3FileService;



    @Test
    @DisplayName("질문게시판에 작성된 모든 게시글을 조회한다.")
    void getAllQuestions() {
        // Given


    }

    @Test
    @DisplayName("게시글번호(ID)로 게시글을 조회한다.")
    void getQuestionById() {
    }

    @Test
    @DisplayName("유저, 팀스페이스, 스터디페이지에 등록된 책으로 질문을 생성할수있다. ")
    void createQuestion() {
    }

    @Test
    @DisplayName("제목으로 게시글을 조회할 수 있다. ")
    void getQuestionsByTitle() {
    }

    @Test
    @DisplayName("게시글의 작성자로 게시글을 조회할 수 있다. ")
    void getQuestionsByWriter() {
    }

    @Test
    @DisplayName("게시글을 작성한 유저만 게시글을 수정할 수 있다.")
    void updateQuestion() {
    }

    @Test
    @DisplayName("게시글을 작성한 유저만 게시글을 삭제할 수 있다.")
    void deleteQuestion() {
    }

    @Test
    @DisplayName("게시글의 좋아요는 ")
    void boardLike() {
    }
}