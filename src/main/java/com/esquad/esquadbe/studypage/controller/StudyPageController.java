package com.esquad.esquadbe.studypage.controller;

import com.esquad.esquadbe.studypage.dto.StudyInfoDto;
import com.esquad.esquadbe.studypage.dto.StudyPageCreateDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.service.BookService;
import com.esquad.esquadbe.studypage.service.StudyPageService;
import com.esquad.esquadbe.studypage.service.StudyPageUserService;
import com.esquad.esquadbe.studypage.service.StudyRemindService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class StudyPageController {

    private final BookService bookService;
    private final StudyPageService studyPageService;
    private final StudyRemindService studyRemindService;
    private final StudyPageUserService studyPageUserService;

    @Autowired
    public StudyPageController(BookService bookService, StudyPageService studyPageService, StudyRemindService studyRemindService, StudyPageUserService studyPageUserService) {
        this.bookService = bookService;
        this.studyPageService = studyPageService;
        this.studyRemindService = studyRemindService;
        this.studyPageUserService = studyPageUserService;
    }

    @PostMapping("/{teamId}/study-pages")
    public ResponseEntity<Long> createStudyPage(
            @PathVariable("teamId") Long teamId,
            @RequestBody @Valid StudyPageCreateDto dto) {
        log.info("try");
        try {
            log.info("Creating a new study page for teamId: {}", teamId);
            Long bookId = bookService.createBookInfo(dto.getBookDto());
            Long studyPageId = studyPageService.createStudyPage(teamId, bookId, dto.getStudyInfoDto());

            studyRemindService.createRemind(studyPageId, dto.getReminds());
            studyPageUserService.createStudyPageUser(teamId, studyPageId, dto.getUserIds());

            return ResponseEntity.status(HttpStatus.CREATED).body(studyPageId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{teamId}/study-pages")
    public ResponseEntity<List<StudyPageReadDto>> getStudyPages(@PathVariable("teamId") Long teamId) {

        List<StudyPageReadDto> studyPages = studyPageService.readStudyPages(teamId);
        return ResponseEntity.ok(studyPages);
    }

    @GetMapping("/{teamId}/study-pages/{studyId}")
    public ResponseEntity<StudyInfoDto> getStudyPageInfo(
            @PathVariable("teamId") Long teamId,
            @PathVariable("studyId") Long studyId) {

        StudyInfoDto studyInfoDto = studyPageService.readStudyPageInfo(studyId);
        return ResponseEntity.ok(studyInfoDto);
    }

    @PostMapping("/{teamId}/study-pages/{studyId}")
    public ResponseEntity<Long> updateStudyPage(
            @PathVariable("teamId") Long teamId,
            @PathVariable("studyId") Long studyId,
            @RequestBody @Valid UpdateStudyPageRequestDto request) {

        return ResponseEntity.ok(studyPageService.updateStudyPage(studyId, request));
    }
    @DeleteMapping("/{teamId}/study-pages/{studyId}")
    public ResponseEntity<String> deleteStudyPage(
            @PathVariable("studyId") Long studyId,
            @RequestParam("name") String studyPageName) {

        studyPageService.deleteStudyPage(studyId, studyPageName);
        return ResponseEntity.ok("Study page deleted successfully.");
    }
}
