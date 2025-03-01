package com.StudentMS.controller;

import com.StudentMS.entity.Course;
import com.StudentMS.entity.File;
import com.StudentMS.entity.User;
import com.StudentMS.service.CourseService;
import com.StudentMS.service.FileService;
import com.StudentMS.service.UserService;
import com.StudentMS.serviceImpl.CustomUserDetailsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private final FileService fileService;
    private final CourseService courseService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;

    @Autowired
    public FileController(FileService fileService, CourseService courseService, CustomUserDetailsService customUserDetailsService, UserService userService) {
        this.fileService = fileService;
        this.courseService = courseService;
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public String getFiles(@NotNull Model model) {
        List<File> files = fileService.findAll();
        model.addAttribute("course", new Course());
        model.addAttribute("user", new User());
        model.addAttribute("files", files);
        return "filer/Filler";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("visibility") boolean visibility,
                             @RequestParam("course") String courseCode) throws IOException {
        // Assuming you fetch the Course entity based on the course code
        User user = customUserDetailsService.getCurrentUser();
        if (user == null) {
            // Handle case where the course is not found
            return "ErrorPage";
        }

        Course course = courseService.findByCode(courseCode);
        if (course == null) {
            // Handle case where the course is not found
            return "ErrorPage";
        }
        fileService.storeFile(file, user, visibility, course);
        return "redirect:/files";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            Resource resource = fileService.loadFileAsResource(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}