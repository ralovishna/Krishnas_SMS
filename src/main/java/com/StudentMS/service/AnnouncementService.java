package com.StudentMS.service;

import com.StudentMS.entity.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    List<Announcement> getAllAnnouncements();

    Optional<Announcement> getAnnouncementById(Long id);

    void createAnnouncement(Announcement announcement);

    void updateAnnouncement(Long id, Announcement updatedAnnouncement);

    void deleteAnnouncement(Long id);

    List<Announcement> getAnnouncementsByPriority(Integer priority);
}