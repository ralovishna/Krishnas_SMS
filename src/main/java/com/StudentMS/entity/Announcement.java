package com.StudentMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcementID")
    private Long announcementID;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(columnDefinition = "TEXT", name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @Column(name = "creationDate")
    private LocalDateTime creationDate;

    @Column(name = "expirationDate")
    private LocalDateTime expirationDate;

    @Column(name = "priority")
    private Integer priority;
}