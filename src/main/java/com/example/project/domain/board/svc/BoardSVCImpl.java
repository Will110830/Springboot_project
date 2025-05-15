package com.example.project.domain.board.svc;

import com.example.project.domain.entity.Board;
import com.example.project.domain.board.dao.BoardDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSVCImpl implements BoardSVC{

  final private BoardDAO boardDAO;

  // 게시글 등록
  @Override
  public Long save(Board board) {
    return boardDAO.save(board);
  }

  // 게시글 조회
  @Override
  public Optional<Board> findById(Long id) {
    return boardDAO.findById(id);
  }

  // 게시글 수정
  @Override
  public int updateById(Long boardId, Board board) {
    return boardDAO.updateById(boardId, board);
  }

  // 게시글 삭제
  @Override
  public int deleteById(Long id) {
    return boardDAO.deleteById(id);
  }
  //상품목록
  @Override
  public List<Board> findAll() {
    return boardDAO.findAll();
  }
}