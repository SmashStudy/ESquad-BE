package com.esquad.esquadbe.studypage.controller;
import com.esquad.esquadbe.studypage.dto.StudyPageDto;
import com.esquad.esquadbe.studypage.repository.StudyPageUserRepository;
import com.esquad.esquadbe.studypage.service.BookService;
import com.esquad.esquadbe.studypage.service.StudyPageService;
import com.esquad.esquadbe.studypage.service.StudyPageUserService;
import com.esquad.esquadbe.studypage.service.StudyRemindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/{teamId}/studyPage")
public class StudyPageController {

    private final BookService bookService;
    private final StudyPageService studyPageService;
    private final StudyRemindService studyRemindService;
    private final StudyPageUserService studyPageUserService;

    // 생성자 주입
    @Autowired
    public StudyPageController(BookService bookService, StudyPageService studyPageService, StudyRemindService studyRemindService, StudyPageUserService studyPageUserService, StudyPageUserRepository studyPageUserRepository) {
        this.bookService = bookService;
        this.studyPageService = studyPageService;
        this.studyRemindService = studyRemindService;
        this.studyPageUserService = studyPageUserService;
    }

    // Create
    @PostMapping("/create")
    public ResponseEntity<String> createStudyPage(@PathVariable("teamId") Long teamId, @RequestBody StudyPageDto dto) {
        log.info("새로운 스터디 페이지 생성: {}", dto.getStudyInfoDto().getStudyPageName());

        //도서, 스터디 페이지 정보 생성
        Long bookId = bookService.createBookInfo(dto.getBookDto());
        Long StudyPageId = studyPageService.createStudyPage(teamId, bookId, dto.getStudyInfoDto());

        //스터디 리마인드, 멤버 정보 생성J
        studyRemindService.createRemind(StudyPageId, dto.getReminds());
        studyPageUserService.createStudyPageUser(StudyPageId, dto.getUserIds());

        // 201 : create 완료 반환
        return ResponseEntity.status(HttpStatus.CREATED).body("Study page created successfully.");
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudyPage(@PathVariable Long id, @RequestBody StudyPageDto dto) {
        log.info("Updating study page with id {}: {}", id, dto);
        studyPageService.updateStudyPage(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Study page updated successfully.");
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudyPage(@PathVariable Long id, @RequestBody String name) {
        log.info("Deleting study page with id {} and name {}", id, name);
        studyPageService.deleteStudyPage(id, name);
        return ResponseEntity.status(HttpStatus.OK).body("Study page deleted successfully.");
    }
}

