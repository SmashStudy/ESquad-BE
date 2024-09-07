package com.esquad.esquadbe.studypage.entity;

import com.esquad.esquadbe.global.entity.BasicEntity;
import com.esquad.esquadbe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "STUDY_PAGE_USERS")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudyPageUser extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "STUDY_PAGE_ID")
    private StudyPage studyPage;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User member;

    @Column(name = "OWNER_FLAG", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean ownerFlag = false;

    public void setStudyPage(StudyPage studyPage) {
        this.studyPage = studyPage;
        studyPage.getStudyPageUsers().add(this);
    }

}
