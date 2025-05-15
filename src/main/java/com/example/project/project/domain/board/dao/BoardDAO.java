package com.example.project.project.domain.board.dao;

import com.example.project.project.domain.entity.Board;
import java.util.List;
import java.util.Optional;

public interface BoardDAO {
  // 게시글 작성 기능
  Long save (Board board);
  // 게시글 조회 기능
  Optional<Board> findById(Long id);
  // 게시글 수정 기능
  int updateById(Long boardId, Board board);
  // 게시글 삭제 기능
  int deleteById(Long boardId);
  // 게시글 목록 조회 기능
  List<Board> findAll();
}