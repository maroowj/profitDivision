package com.muzisoft.division.domain.base;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseCreationTimeEntity {

    @CreatedDate
    @Column(updatable = false, columnDefinition="timestamp(6) default now(6)")
    private Date createdAt;

}
