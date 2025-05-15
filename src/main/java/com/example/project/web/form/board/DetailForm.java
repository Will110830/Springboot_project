package com.example.project.web.form.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetailForm {
  private Long boardId;
  private String title;
  private String content;
  private String writer;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}