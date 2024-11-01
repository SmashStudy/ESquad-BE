package com.esquad.esquadbe.studypage.service;

import com.esquad.esquadbe.studypage.dto.StudyRemindDto;
import com.esquad.esquadbe.studypage.entity.StudyPage;
import com.esquad.esquadbe.studypage.entity.StudyRemind;
import com.esquad.esquadbe.studypage.exception.BookJsonProcessingException;
import com.esquad.esquadbe.studypage.repository.StudyPageRepository;
import com.esquad.esquadbe.studypage.repository.StudyRemindRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyRemindService {

    private final StudyRemindRepository studyRemindRepository;
    private final StudyPageRepository studyPageRepository;

    @Autowired
    public StudyRemindService(StudyRemindRepository studyRemindRepository, StudyPageRepository studyPageRepository) {
        this.studyRemindRepository = studyRemindRepository;
        this.studyPageRepository = studyPageRepository;
    }

    public void createRemind(Long studyPageId, List<StudyRemindDto> remindDtos) {
        StudyPage studyPage = studyPageRepository.findById(studyPageId).orElseThrow(BookJsonProcessingException::new);

        for (StudyRemindDto remindDto : remindDtos) {
            StudyRemind remind = remindDto.from(studyPage);
            studyRemindRepository.save(remind);
        }
    }
}
