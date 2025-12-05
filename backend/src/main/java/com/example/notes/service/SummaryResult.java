package com.example.notes.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResult {
    private List<String> keyPoints;
    private String summary;
}
