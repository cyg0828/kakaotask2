package com.example.task2.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ScheduleResponseDTO {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

