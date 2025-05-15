package com.example.project.web;

import com.example.project.domain.entity.Board;
import com.example.project.domain.board.svc.BoardSVC;
import com.example.project.web.form.board.DetailForm;
import com.example.project.web.form.board.SaveForm;
import com.example.project.web.form.board.UpdateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  final private BoardSVC boardSVC;

  // 게시글 목록
  @GetMapping
  public String findAll(Model model) {
    List<Board> list = boardSVC.findAll();
    model.addAttribute("list", list);
    return "board/all";
  }

  // 작성 화면
  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("saveForm", new SaveForm());
    return "board/addForm";
  }

  // 작성 처리
  @PostMapping("/add")
  public String add(
      @Valid @ModelAttribute SaveForm saveForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes,
      Model model
  ) {
    log.info("title={}, content={}, writer={}",
        saveForm.getTitle(), saveForm.getContent(), saveForm.getWriter());

    // 유효성 검사
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "board/addForm";
    }

    // 게시글 저장
    Board board = new Board();
    board.setTitle(saveForm.getTitle());
    board.setContent(saveForm.getContent());
    board.setWriter(saveForm.getWriter());
    board.setCreatedAt(LocalDateTime.now());  // 현재 시간으로 작성일 설정

    Long boardId = boardSVC.save(board);
    redirectAttributes.addAttribute("id", boardId);
    return "redirect:/boards/{id}";
  }

  // 게시글 조회
  @GetMapping("/{id}")
  public String findById(
      @PathVariable("id") Long id,
      Model model
  ) {
    Optional<Board> optionalBoard = boardSVC.findById(id);
    Board findedBoard = optionalBoard.orElseThrow();

    DetailForm detailForm = new DetailForm();
    detailForm.setBoardId(findedBoard.getBoardId());
    detailForm.setTitle(findedBoard.getTitle());
    detailForm.setContent(findedBoard.getContent());
    detailForm.setWriter(findedBoard.getWriter());
    detailForm.setCreatedAt(findedBoard.getCreatedAt());
    detailForm.setUpdatedAt(findedBoard.getUpdatedAt());

    model.addAttribute("detailForm", detailForm);
    return "board/detailForm";
  }

  // 게시글 삭제
  @GetMapping("/{id}/del")
  public String deleteById(@PathVariable("id") Long boardId) {
    boardSVC.deleteById(boardId);
    return "redirect:/boards";
  }

  // 게시글 수정 화면
  @GetMapping("/{id}/edit")
  public String updateForm(
      @PathVariable("id") Long boardId,
      Model model
  ) {
    Optional<Board> optionalBoard = boardSVC.findById(boardId);
    Board findedBoard = optionalBoard.orElseThrow();

    UpdateForm updateForm = new UpdateForm();
    updateForm.setBoardId(findedBoard.getBoardId());
    updateForm.setTitle(findedBoard.getTitle());
    updateForm.setContent(findedBoard.getContent());

    model.addAttribute("updateForm", updateForm);
    return "board/updateForm";
  }

  // 게시글 수정 처리
  @PostMapping("/{id}/edit")
  public String updateById(
      @PathVariable("id") Long boardId,
      @Valid @ModelAttribute UpdateForm updateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ) {
    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "board/updateForm";
    }

    Board board = new Board();
    board.setTitle(updateForm.getTitle());
    board.setContent(updateForm.getContent());
    board.setUpdatedAt(LocalDateTime.now());  // 수정 시에 updatedAt 갱신

    boardSVC.updateById(boardId, board);

    redirectAttributes.addAttribute("id", boardId);
    return "redirect:/boards/{id}";
  }
}