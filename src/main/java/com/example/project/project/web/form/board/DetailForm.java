package com.example.project.project.web.form.board;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class DetailForm {
  private Long boardId;
  private String title;
  private String content;
  private String writer;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // 기본 생성자
  public DetailForm() {
  }

  public DetailForm(Long boardId, String title, String content, String writer, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.boardId = boardId;
    this.title = title;
    this.content = content;
    this.writer = writer;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

}