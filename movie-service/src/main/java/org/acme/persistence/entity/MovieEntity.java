package org.acme.persistence.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieEntity {
    private String id;
    private String title;
    private String description;
    private LocalDate released;
}
