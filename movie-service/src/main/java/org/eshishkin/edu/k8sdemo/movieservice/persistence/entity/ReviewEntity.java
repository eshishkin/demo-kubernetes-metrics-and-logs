package org.eshishkin.edu.k8sdemo.movieservice.persistence.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewEntity {
    private String id;
    private String movieId;
    private String title;
    private String content;
    private String authorId;
    private LocalDate created;
}
