package com.example.task2.service;

import com.example.task2.dto.ScheduleResponseDTO;
import com.example.task2.exception.UserException;
import com.example.task2.model.Schedule;
import com.example.task2.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleResponseDTO createSchedule(Schedule schedule) {
        LocalDateTime now = LocalDateTime.now();
        schedule.setCreatedAt(now);
        schedule.setUpdatedAt(now);
        Schedule saved = scheduleRepository.save(schedule);
        return toDTO(saved);
    }

    public List<ScheduleResponseDTO> getSchedules(String author, String updatedDate) {
        List<Schedule> result;
        if (author != null && updatedDate != null) {
            LocalDate date = LocalDate.parse(updatedDate);
            result = scheduleRepository.findByAuthorAndUpdatedDate(author, date);
        } else if (author != null) {
            result = scheduleRepository.findByAuthor(author);
        } else {
            result = scheduleRepository.findAll();
        }
        return result.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ScheduleResponseDTO getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("일정이 존재하지 않습니다."));
    }

    public ScheduleResponseDTO updateSchedule(Long id, String title, String author, String password) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new UserException("일정이 존재하지 않습니다."));
        if (!schedule.getPassword().equals(password)) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }
        schedule.setTitle(title);
        schedule.setAuthor(author);
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleRepository.update(schedule);
        return toDTO(schedule);
    }

    public void deleteSchedule(Long id, String password) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new UserException("일정이 존재하지 않습니다."));
        if (!schedule.getPassword().equals(password)) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }
        scheduleRepository.delete(id);
    }

    private ScheduleResponseDTO toDTO(Schedule s) {
        return ScheduleResponseDTO.builder()
                .id(s.getId())
                .title(s.getTitle())
                .author(s.getAuthor())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
