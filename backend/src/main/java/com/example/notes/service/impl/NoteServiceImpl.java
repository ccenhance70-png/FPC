package com.example.notes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.notes.entity.Note;
import com.example.notes.mapper.NoteMapper;
import com.example.notes.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    @Override
    public Page<Note> searchNotes(Page<Note> page, String keyword, String courseName) {
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Note::getTitle, keyword)
                    .or()
                    .like(Note::getTags, keyword)
                    .or()
                    .like(Note::getRawContent, keyword)
                    .or()
                    .like(Note::getAiSummary, keyword)
                    .or()
                    .like(Note::getAiKeypoints, keyword));
        }
        if (StringUtils.hasText(courseName)) {
            wrapper.like(Note::getCourseName, courseName);
        }
        wrapper.orderByDesc(Note::getUpdatedAt).orderByDesc(Note::getCreatedAt);
        return this.page(page, wrapper);
    }
}
