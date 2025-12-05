package com.example.notes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("note")
public class Note {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title length must be <= 100")
    private String title;

    @Size(max = 50, message = "Course name length must be <= 50")
    @TableField("course_name")
    private String courseName;

    @Size(max = 200, message = "Tags length must be <= 200")
    private String tags;

    @TableField("raw_content")
    private String rawContent;

    @TableField("ai_summary")
    private String aiSummary;

    @TableField("ai_keypoints")
    private String aiKeypoints;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
