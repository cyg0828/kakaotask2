package com.example.task2.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Schedule {
    @Id
    private Long id;
    private String title;
    private String author;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}