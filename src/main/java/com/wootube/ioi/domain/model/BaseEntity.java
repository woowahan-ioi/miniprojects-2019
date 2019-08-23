package com.wootube.ioi.domain.model;

import java.time.LocalDateTime;
import javax.persistence.*;

import com.wootube.ioi.domain.exception.InactivatedException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@Getter
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public boolean isSameUserAndWriter(Long userId) {
        return id.equals(userId);
    }
}
