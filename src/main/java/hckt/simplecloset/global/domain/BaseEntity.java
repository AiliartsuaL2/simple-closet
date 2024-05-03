package hckt.simplecloset.global.domain;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    @Column
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    protected LocalDateTime modifiedDate;

    protected <T> void notNullValidation(T data, String message) {
        if (ObjectUtils.isEmpty(data)) {
            throw new IllegalArgumentException(message);
        }
    }
}