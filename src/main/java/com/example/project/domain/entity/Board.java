package com.example.project.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Data
public class Board {
  private Long boardId;
  private String title;
  private String content;
  private String writer;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  // 생성자
//  public Board(Long boardId, String title, String content, String writer, LocalDateTime createdAt, LocalDateTime updatedAt) {
//    this.boardId = boardId;
//    this.title = title;
//    this.content = content;
//    this.writer = writer;
//    this.createdAt = createdAt;
//    this.updatedAt = updatedAt;
//  }
}