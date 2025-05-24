package com.example.task2.controller;

import com.example.task2.dto.ScheduleResponseDTO;
import com.example.task2.model.Schedule;
import com.example.task2.service.ScheduleService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ScheduleResponseDTO create(@RequestBody Schedule schedule) {
        return scheduleService.createSchedule(schedule);
    }

    @GetMapping
    public List<ScheduleResponseDTO> list(@RequestParam(required = false) String author,
                                          @RequestParam(required = false) String updated_date) {
        return scheduleService.getSchedules(author, updated_date);
    }

    @GetMapping("/{id}")
    public ScheduleResponseDTO get(@PathVariable Long id) {
        return scheduleService.getSchedule(id);
    }

    @PutMapping("/{id}")
    public ScheduleResponseDTO update(@PathVariable Long id,
                                      @RequestBody UpdateRequest req) {
        return scheduleService.updateSchedule(id, req.title, req.author, req.password);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id, @RequestBody DeleteRequest req) {
        scheduleService.deleteSchedule(id, req.password);
        Map<String, String> response = new HashMap<>();
        response.put("message", "삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Getter @Setter
    public static class UpdateRequest {
        private String title;
        private String author;
        private String password;
    }

    @Getter @Setter
    public static class DeleteRequest {
        private String password;
    }
}

