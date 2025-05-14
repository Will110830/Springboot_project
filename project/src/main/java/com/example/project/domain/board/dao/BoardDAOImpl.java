package com.example.project.domain.board.dao;

import com.example.project.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
class BoardDAOImpl implements BoardDAO{

  final private NamedParameterJdbcTemplate template;

  //수동매핑
  RowMapper<Board> boardRowMapper(){

    return (rs, rowNum)->{
      Board board = new Board();
      board.setBoardId(rs.getLong("board_id"));
      board.setTitle(rs.getString("title"));
      board.setContent(rs.getString("content"));
      board.setWriter(rs.getString("writer"));
      board.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
      return board;
    };
  }

  /**
   * 게시글 등록
   * @param board
   * @return 저장된 게시글 ID
   */
  @Override
  public Long save(Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO board (title, content, writer, created_at) ");
    sql.append("VALUES (:title, :content, :writer, CURRENT_TIMESTAMP)");

    //BeanPropertySqlParameterSource : 자바객체 필드명과 SQL파라미터명이 같을때 자동 매칭함.
    SqlParameterSource param = new BeanPropertySqlParameterSource(board);

    // template.update()가 수행된 레코드의 특정 컬럼값을 읽어오는 용도
    KeyHolder keyHolder = new GeneratedKeyHolder();
    long rows = template.update(sql.toString(),param, keyHolder, new String[]{"board_id"} );

    return keyHolder.getKey().longValue();
  }

  /**
   * 게시글 조회
   * @param id 조회할 게시글 ID
   * @return 상품정보
   */
  @Override
  public Optional<Board> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT *");
    sql.append("  FROM board");
    sql.append("  WHERE board_id = :id ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);

    Board board = null;
    try {
      board = template.queryForObject(sql.toString(), param, BeanPropertyRowMapper.newInstance(Board.class));
    } catch (EmptyResultDataAccessException e) { //template.queryForObject() : 레코드를 못찾으면 예외 발생
      return Optional.empty();
    }

    return Optional.of(board);
  }

  /**
   * 게시글 수정
   * @param boardId 수정할 게시글 ID
   * @param board 수정할 게시글 데이터
   * @return 게시글 수정 건수
   */
  @Override
  public int updateById(Long boardId, Board board) {
    StringBuilder sql = new StringBuilder();
    sql.append("UPDATE board ");
    sql.append("   SET title = :title, ");
    sql.append("       content = :content, ");
    sql.append("       updated_at = CURRENT_TIMESTAMP ");
    sql.append(" WHERE board_id = :boardId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", board.getTitle())
        .addValue("content", board.getContent())
        .addValue("boardId", boardId);

    return template.update(sql.toString(), param);
  }

  /**
   * 게시글 삭제
   * @param id 게시글 ID
   * @return 삭제건수
   */
  @Override
  public int deleteById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM board ");
    sql.append(" WHERE board_id = :id ");

    Map<String, Long> param = Map.of("id",id);
    int rows = template.update(sql.toString(), param); //삭제된 행의 수 반환
    return rows;
  }


  /**
   * 게시글 목록 조회
   * @return 게시글 목록 (제목, 작성자, 작성일)
   */
  @Override
  public List<Board> findAll() {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("  SELECT title, writer, created_at ");
    sql.append("    FROM board ");
    sql.append("order BY board_id desc ");

    //db요청
    List<Board> list = template.query(sql.toString(), boardRowMapper());
    return list;
  }
}