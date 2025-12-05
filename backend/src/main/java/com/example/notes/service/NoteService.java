package com.example.notes.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.notes.entity.Note;

public interface NoteService extends IService<Note> {
    Page<Note> searchNotes(Page<Note> page, String keyword, String courseName);
}
