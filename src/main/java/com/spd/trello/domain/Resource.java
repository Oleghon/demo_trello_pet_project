package com.spd.trello.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Data
@MappedSuperclass
public class Resource extends Domain {
    @Column(updatable = false)
    private String createdBy;
    private String updatedBy;

    @CreatedDate
    @Column(updatable = false)
    private Timestamp createdDate;

    @LastModifiedDate
    private Timestamp updatedDate;
}
