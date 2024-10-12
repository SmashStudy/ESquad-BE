package com.esquad.esquadbe.studypage.controller;

import com.esquad.esquadbe.studypage.dto.StudyPageCreateDto;
import com.esquad.esquadbe.studypage.dto.StudyPageReadDto;
import com.esquad.esquadbe.studypage.dto.UpdateStudyPageRequestDto;
import com.esquad.esquadbe.studypage.service.BookService;
import com.esquad.esquadbe.studypage.service.StudyPageService;
import com.esquad.esquadbe.studypage.service.StudyPageUserService;
import com.esquad.esquadbe.studypage.service.StudyRemindService;
import jakarta.persistence.EntityNotFoundException;
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

    // Create
    @PostMapping("/{teamId}/study-pages")
    public ResponseEntity<String> createStudyPage(
            @PathVariable("teamId") Long teamId,
            @RequestBody StudyPageCreateDto dto) {

        log.info("Creating a new study page for teamId: {}", teamId);

        Long bookId = bookService.createBookInfo(dto.getBookDto());
        Long studyPageId = studyPageService.createStudyPage(teamId, bookId, dto.getStudyInfoDto());

        // Create study reminds and assign users to the study page
        studyRemindService.createRemind(studyPageId, dto.getReminds());
        studyPageUserService.createStudyPageUser(studyPageId, dto.getUserIds());

        return ResponseEntity.status(HttpStatus.CREATED).body("Study page created successfully.");
    }

    // Read List
    @GetMapping("/{teamId}/study-pages")
    public ResponseEntity<List<StudyPageReadDto>> getStudyPages(@PathVariable("teamId") Long teamId) {
        log.info("Fetching study pages for teamId: {}", teamId);
        List<StudyPageReadDto> studyPages = studyPageService.readStudyPages(teamId);
        return ResponseEntity.status(HttpStatus.OK).body(studyPages);
    }

    // Update
    @PutMapping("/{teamId}/study-pages/{studyId}")
    public ResponseEntity<String> updateStudyPage(
            @PathVariable("teamId") Long teamId,
            @PathVariable("studyId") Long studyId,
            @RequestBody UpdateStudyPageRequestDto request) {

        log.info("Updating study page with studyId: {} for userId: {}", studyId, request.getUserId());

        boolean isUpdated = studyPageService.updateStudyPage(studyId, request);

        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("Study page updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have authorization to update this study page.");
        }
    }

    // Delete
    @DeleteMapping("/{teamId}/study-pages/{studyId}")
    public ResponseEntity<String> deleteStudyPage(
            @PathVariable("teamId") Long teamId,
            @PathVariable("studyId") Long studyId,
            @RequestParam("name") String studyPageName) {

        log.info("Deleting study page with ID: {} and name: {}", studyId, studyPageName);

        try {
            studyPageService.deleteStudyPage(studyId, studyPageName);
            return ResponseEntity.status(HttpStatus.OK).body("Study page deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
