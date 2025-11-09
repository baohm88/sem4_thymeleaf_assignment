package com.example.jobboard.model;

public enum JobType {
    FULL_TIME("Toàn thời gian"),
    PART_TIME("Bán thời gian"),
    REMOTE("Làm từ xa"),
    INTERNSHIP("Thực tập");

    private final String label;

    JobType(String label) { this.label = label; }

    public String getLabel() { return label; }
}