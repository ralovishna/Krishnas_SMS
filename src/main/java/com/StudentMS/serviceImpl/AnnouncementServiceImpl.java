package com.StudentMS.serviceImpl;

import com.StudentMS.entity.Announcement;
import com.StudentMS.repository.AnnouncementRepo;
import com.StudentMS.service.AnnouncementService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private final AnnouncementRepo announcementRepo;
    private final  CustomUserDetailsService customUserDetailsService;

    public AnnouncementServiceImpl(AnnouncementRepo announcementRepo, CustomUserDetailsService customUserDetailsService) {
        this.announcementRepo = announcementRepo;
        this.customUserDetailsService = customUserDetailsService;
    }


    public List<Announcement> getAllAnnouncements() {
        return announcementRepo.findAll();
    }

    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepo.findById(id);
    }

    public void createAnnouncement(@NotNull Announcement announcement) {
        announcement.setCreationDate(LocalDateTime.now());
        announcement.setAuthor(customUserDetailsService.getCurrentUser());
        announcementRepo.save(announcement);
    }

    public void updateAnnouncement(Long id, Announcement updatedAnnouncement) {
        Optional<Announcement> existingAnnouncement = announcementRepo.findById(id);
        if (existingAnnouncement.isPresent()) {
            updatedAnnouncement.setAuthor(customUserDetailsService.getCurrentUser());
            updatedAnnouncement.setCreationDate(existingAnnouncement.get().getCreationDate());
            updatedAnnouncement.setAnnouncementID(id);
            announcementRepo.save(updatedAnnouncement);
        } else {
        }
    }

    public void deleteAnnouncement(Long id) {
        announcementRepo.deleteById(id);
    }

    public List<Announcement> getActiveAnnouncements() {
        LocalDateTime now = LocalDateTime.now();
        return announcementRepo.findByExpirationDateGreaterThanOrExpirationDateIsNull(now);
    }

    public List<Announcement> getAnnouncementsByPriority(Integer priority) {
        return announcementRepo.findByPriority(priority);
    }
}