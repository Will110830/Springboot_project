package com.example.project.web.form.board;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateForm {
  private Long boardId;

  @NotBlank(message = "제목은 필수 입력 항목입니다.")
  @Size(max = 30, message = "제목은 30자 이내로 입력해주세요.")
  private String title;

  @NotBlank(message = "내용은 필수 입력 항목입니다.")
  private String content;

  @NotBlank(message = "작성자는 필수 입력 항목입니다.")
  @Size(max = 10, message = "작성자 이름은 10자 이내로 입력해주세요.")
  private String writer;
  private LocalDateTime updatedAt;
}
