package main.entity;

import org.springframework.stereotype.Component;

@Component
public enum ReleaseStatus {
    INACTIVE, PUBLISHED, PENDING, DECLINED;
}
