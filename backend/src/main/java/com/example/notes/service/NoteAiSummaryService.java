package com.example.notes.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteAiSummaryService {

    public SummaryResult generateSummary(String rawContent) {
        if (rawContent == null) {
            return new SummaryResult(List.of(), "内容为空，无法生成总结。");
        }
        List<String> lines = Arrays.stream(rawContent.split("\\r?\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());

        List<String> keyPoints = new ArrayList<>();
        int maxKeyPoints = Math.min(5, lines.size());
        int minKeyPoints = Math.min(3, maxKeyPoints);
        for (int i = 0; i < maxKeyPoints; i++) {
            keyPoints.add(lines.get(i));
        }
        if (keyPoints.isEmpty() && !lines.isEmpty()) {
            keyPoints.addAll(lines);
        } else if (keyPoints.size() < minKeyPoints && !lines.isEmpty()) {
            for (int i = keyPoints.size(); i < minKeyPoints && i < lines.size(); i++) {
                keyPoints.add(lines.get(i));
            }
        }

        String topic = lines.isEmpty() ? "课堂笔记" : lines.get(0);
        int length = rawContent.length();
        String densityDescription;
        if (length < 80) {
            densityDescription = "内容简洁，重点清晰";
        } else if (length < 300) {
            densityDescription = "包含若干细节，适合快速回顾";
        } else {
            densityDescription = "信息量较大，建议结合要点逐段复习";
        }

        StringBuilder summaryBuilder = new StringBuilder();
        summaryBuilder.append("这是一份关于《").append(topic).append("》的课堂笔记。");
        summaryBuilder.append("主要提到了关键知识点和课堂要点，");
        summaryBuilder.append(densityDescription).append("。");
        summaryBuilder.append("建议按要点顺序复盘，加深理解。");

        return new SummaryResult(keyPoints, summaryBuilder.toString());
    }
}
