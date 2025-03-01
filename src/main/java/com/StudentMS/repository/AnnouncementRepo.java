package com.StudentMS.repository;

import com.StudentMS.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    List<Announcement> findByExpirationDateGreaterThanOrExpirationDateIsNull(LocalDateTime now);

    List<Announcement> findByPriority(Integer priority);
}