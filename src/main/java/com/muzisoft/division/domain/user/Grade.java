package com.muzisoft.division.domain.user;

import com.muzisoft.division.domain.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table
public class Grade extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, columnDefinition = "tinyint(1)")
    @Id
    private int seq;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private float percent;

    @Column(columnDefinition = "TEXT")
    private String comment;

    public static Grade create(String title, float percent, String comment) {
        Grade grade = new Grade();
        grade.setTitle(title);
        grade.setPercent(percent);
        grade.setComment(comment);
        return grade;
    }

    public void update(String title, float percent, String comment) {
        this.title=title;
        this.percent=percent;
        this.comment=comment;
    }
}
