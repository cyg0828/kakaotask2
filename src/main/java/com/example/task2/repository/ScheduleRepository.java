package com.example.task2.repository;


import com.example.task2.model.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Schedule> rowMapper = (rs, rowNum) -> Schedule.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .author(rs.getString("author"))
            .password(rs.getString("password"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .build();

    public Schedule save(Schedule s) {
        jdbcTemplate.update("INSERT INTO schedule (title, author, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
                s.getTitle(), s.getAuthor(), s.getPassword(), Timestamp.valueOf(s.getCreatedAt()), Timestamp.valueOf(s.getUpdatedAt()));
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        s.setId(id);
        return s;
    }

    public List<Schedule> findAll() {
        return jdbcTemplate.query("SELECT * FROM schedule ORDER BY updated_at DESC", rowMapper);
    }

    public List<Schedule> findByAuthor(String author) {
        return jdbcTemplate.query("SELECT * FROM schedule WHERE author LIKE ? ORDER BY updated_at DESC", rowMapper, "%" + author + "%");
    }

    public List<Schedule> findByAuthorAndUpdatedDate(String author, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return jdbcTemplate.query("SELECT * FROM schedule WHERE author LIKE ? AND updated_at BETWEEN ? AND ? ORDER BY updated_at DESC",
                rowMapper, "%" + author + "%", Timestamp.valueOf(start), Timestamp.valueOf(end));
    }

    public Optional<Schedule> findById(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", rowMapper, id);
        return result.stream().findFirst();
    }

    public void update(Schedule s) {
        jdbcTemplate.update("UPDATE schedule SET title = ?, author = ?, updated_at = ? WHERE id = ?",
                s.getTitle(), s.getAuthor(), Timestamp.valueOf(s.getUpdatedAt()), s.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM schedule WHERE id = ?", id);
    }
}

