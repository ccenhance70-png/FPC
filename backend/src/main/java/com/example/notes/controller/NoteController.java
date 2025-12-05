package com.example.notes.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.notes.common.Result;
import com.example.notes.entity.Note;
import com.example.notes.service.NoteAiSummaryService;
import com.example.notes.service.NoteService;
import com.example.notes.service.SummaryResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final NoteAiSummaryService noteAiSummaryService;

    public NoteController(NoteService noteService, NoteAiSummaryService noteAiSummaryService) {
        this.noteService = noteService;
        this.noteAiSummaryService = noteAiSummaryService;
    }

    @GetMapping
    public Result<Page<Note>> listNotes(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String courseName) {
        Page<Note> pageRequest = new Page<>(page, pageSize);
        Page<Note> notes = noteService.searchNotes(pageRequest, keyword, courseName);
        return Result.success(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<Note>> getNote(@PathVariable Long id) {
        Note note = noteService.getById(id);
        return Optional.ofNullable(note)
                .map(value -> ResponseEntity.ok(Result.success(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Result.failure(HttpStatus.NOT_FOUND.value(), "Note not found")));
    }

    @PostMapping
    public Result<Note> createNote(@Valid @RequestBody Note note) {
        LocalDateTime now = LocalDateTime.now();
        note.setCreatedAt(now);
        note.setUpdatedAt(now);
        noteService.save(note);
        return Result.success("Created", note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<Note>> updateNote(@PathVariable Long id, @Valid @RequestBody Note note) {
        Note existing = noteService.getById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.failure(HttpStatus.NOT_FOUND.value(), "Note not found"));
        }
        note.setId(id);
        note.setCreatedAt(existing.getCreatedAt());
        note.setUpdatedAt(LocalDateTime.now());
        noteService.updateById(note);
        Note updated = noteService.getById(id);
        return ResponseEntity.ok(Result.success("Updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deleteNote(@PathVariable Long id) {
        boolean removed = noteService.removeById(id);
        if (removed) {
            return ResponseEntity.ok(Result.success(null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Result.failure(HttpStatus.NOT_FOUND.value(), "Note not found"));
    }

    @PostMapping("/{id}/summarize")
    public ResponseEntity<Result<Note>> summarize(@PathVariable Long id) {
        Note note = noteService.getById(id);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Result.failure(HttpStatus.NOT_FOUND.value(), "Note not found"));
        }
        if (note.getRawContent() == null || note.getRawContent().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Result.failure(HttpStatus.BAD_REQUEST.value(), "Note raw content is empty"));
        }

        SummaryResult summaryResult = noteAiSummaryService.generateSummary(note.getRawContent());
        note.setAiKeypoints(String.join("\n", summaryResult.getKeyPoints()));
        note.setAiSummary(summaryResult.getSummary());
        note.setUpdatedAt(LocalDateTime.now());
        noteService.updateById(note);
        Note updated = noteService.getById(id);
        return ResponseEntity.ok(Result.success("Summary generated", updated));
    }
}
