package org.example.stamppaw_backend.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BasicTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime registeredAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @PrePersist
    public void setPrePersist() {
        this.registeredAt = now();
    }
}