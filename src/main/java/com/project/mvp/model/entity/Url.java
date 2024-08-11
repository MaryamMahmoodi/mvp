package com.project.mvp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Url
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Original URL cannot be blank")
    @Pattern(regexp = "^(https?://).+", message = "Invalid URL format")
    private String originalUrl;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Short code cannot be blank")
    @Size(min = 5, max = 10, message = "Short code must be between 5 and 10 characters")
    private String shortCode;

    public Url(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }
}
