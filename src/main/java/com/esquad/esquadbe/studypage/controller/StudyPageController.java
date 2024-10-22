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
@RequestMapping("/api/{teamId}/study-pages")
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

    // Create
    @PostMapping("/")
    public ResponseEntity<String> createStudyPage(
            @PathVariable("teamId") Long teamId,
            @RequestBody @Valid StudyPageCreateDto dto) {

        log.info("Creating a new study page for teamId: {}", teamId);
        Long bookId = bookService.createBookInfo(dto.getBookDto());
        Long studyPageId = studyPageService.createStudyPage(teamId, bookId, dto.getStudyInfoDto());

        studyRemindService.createRemind(studyPageId, dto.getReminds());
        studyPageUserService.createStudyPageUser(teamId, studyPageId, dto.getUserIds());

        return ResponseEntity.status(HttpStatus.CREATED).body("Study page created successfully.");
    }

    // Read List
    @GetMapping("/")
    public ResponseEntity<List<StudyPageReadDto>> getStudyPages(@PathVariable("teamId") Long teamId) {
        log.info("Fetching study pages for teamId: {}", teamId);
        List<StudyPageReadDto> studyPages = studyPageService.readStudyPages(teamId);
        return ResponseEntity.ok(studyPages);
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyInfoDto> getStudyPageInfo(
            @PathVariable("teamId") Long teamId,
            @PathVariable("studyId") Long studyId) {
        log.info("Fetching study page info: teamId = {}, studyId = {}", teamId, studyId);

        StudyInfoDto studyInfoDto = studyPageService.readStudyPageInfo(studyId);
        return ResponseEntity.ok(studyInfoDto);
    }

    // Update
    @PostMapping("/{studyId}")
    public ResponseEntity<String> updateStudyPage(
            @PathVariable("teamId") Long teamId,
            @PathVariable("studyId") Long studyId,
            @RequestBody @Valid UpdateStudyPageRequestDto request) {

        log.info("Updating study page with studyId: {}", studyId);

        boolean isUpdated = studyPageService.updateStudyPage(studyId, request);

        if (isUpdated) {
            return ResponseEntity.ok("Study page updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have authorization to update this study page.");
        }
    }

    // Delete
    @DeleteMapping("/{studyId}")
    public ResponseEntity<String> deleteStudyPage(
            @PathVariable("studyId") Long studyId,
            @RequestParam("name") String studyPageName) {

        log.info("Attempting to delete study page with ID: {} and name: {}", studyId, studyPageName);

        studyPageService.deleteStudyPage(studyId, studyPageName);
        return ResponseEntity.ok("Study page deleted successfully.");
    }
}
