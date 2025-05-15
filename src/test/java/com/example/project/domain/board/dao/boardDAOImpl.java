package com.example.project.domain.board.dao;

import com.example.project.project.domain.entity.Board;
import com.example.project.project.domain.board.dao.BoardDAO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class BoardDAOImplTest {

  @Autowired
  BoardDAO boardDAO;

  @Test
  void testLog() {
    log.info("hi");
    log.warn("warn");
    log.error("error");
  }

  @Test
  @DisplayName("게시글 목록 조회")
  void findAll() {
    List<Board> list = boardDAO.findAll();
    for (Board board : list) {
      log.info("board={}", board);
    }
  }

  @Test
  @DisplayName("게시글 등록")
  void save() {
    Board board = new Board();
    board.setTitle("테스트 제목");
    board.setContent("테스트 내용");
    board.setWriter("작성자");

    Long id = boardDAO.save(board);
    log.info("저장된 게시글 ID={}", id);
  }

  @Test
  @DisplayName("게시글 조회")
  void findById() {
    Long boardId = 1L;
    Optional<Board> optionalBoard = boardDAO.findById(boardId);
    Board foundBoard = optionalBoard.orElseThrow();
    log.info("조회된 게시글={}", foundBoard);
  }

  @Test
  @DisplayName("게시글 수정")
  void updateById() {
    Long boardId = 1L;
    Board board = new Board();
    board.setTitle("수정된 제목");
    board.setContent("수정된 내용");

    int rows = boardDAO.updateById(boardId, board);
    Assertions.assertThat(rows).isEqualTo(1);

    Optional<Board> optionalBoard = boardDAO.findById(boardId);
    Board updatedBoard = optionalBoard.orElseThrow();

    Assertions.assertThat(updatedBoard.getTitle()).isEqualTo("수정된 제목");
    Assertions.assertThat(updatedBoard.getContent()).isEqualTo("수정된 내용");
  }

  @Test
  @DisplayName("게시글 삭제")
  void deleteById() {
    Long boardId = 1L;
    int rows = boardDAO.deleteById(boardId);
    log.info("삭제 건수={}", rows);
    Assertions.assertThat(rows).isEqualTo(1);
  }
}