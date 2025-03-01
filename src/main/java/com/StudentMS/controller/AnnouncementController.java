package com.StudentMS.controller;

import com.StudentMS.entity.Announcement;
import com.StudentMS.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/announcements") // Removed "/api"
public class AnnouncementController {

    @Autowired
    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public String getAllAnnouncements(Model model) {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        model.addAttribute("announcements", announcements);
        return "anna/Announcements"; // View name (e.g., anna/list.html)
    }

    @PostMapping("/create")
    public String createAnnouncement(@ModelAttribute Announcement announcement) {
        announcementService.createAnnouncement(announcement);
        return "redirect:/announcements"; // Redirect to list
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Announcement> announcement = announcementService.getAnnouncementById(id);
        if (announcement.isPresent()) {
            model.addAttribute("announcement", announcement.get());
            return "anna/EditAnnouncement"; // View name (e.g., anna/edit.html)
        } else {
            return "ErrorPage";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateAnnouncement(@PathVariable Long id, @ModelAttribute Announcement updatedAnnouncement) {
        announcementService.updateAnnouncement(id, updatedAnnouncement);
        return "redirect:/announcements";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return "redirect:/announcements";
    }
}