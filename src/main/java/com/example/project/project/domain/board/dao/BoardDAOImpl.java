package com.example.project.project.domain.board.dao;

import com.example.project.project.domain.entity.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Clob;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
class BoardDAOImpl implements BoardDAO {

  final private NamedParameterJdbcTemplate template;

  // 내용포함 상세조회용 RowMapper
  RowMapper<Board> boardRowMapper() {
    return (rs, rowNum) -> {
      Board board = new Board();
      board.setBoardId(rs.getLong("board_id"));
      board.setTitle(rs.getString("title"));

      Clob clob = rs.getClob("content");
      if (clob != null) {
        board.setContent(clob.getSubString(1, (int) clob.length()));
      } else {
        board.setContent(null);
      }

      board.setWriter(rs.getString("writer"));

      Timestamp createdTs = rs.getTimestamp("created_at");
      if (createdTs != null) {
        board.setCreatedAt(createdTs.toLocalDateTime());
      }

      Timestamp updatedTs = rs.getTimestamp("updated_at");
      if (updatedTs != null) {
        board.setUpdatedAt(updatedTs.toLocalDateTime());
      }

      return board;
    };
  }

  // 내용제외한 목록조회용 RowMapper
  RowMapper<Board> boardListRowMapper() {
    return (rs, rowNum) -> {
      Board board = new Board();
      board.setBoardId(rs.getLong("board_id"));
      board.setTitle(rs.getString("title"));
      board.setWriter(rs.getString("writer"));

      Timestamp createdTs = rs.getTimestamp("created_at");
      if (createdTs != null) {
        board.setCreatedAt(createdTs.toLocalDateTime());
      }

      Timestamp updatedTs = rs.getTimestamp("updated_at");
      if (updatedTs != null) {
        board.setUpdatedAt(updatedTs.toLocalDateTime());
      }

      return board;
    };
  }

  //작성
  @Override
  public Long save(Board board) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO board (title, content, writer, created_at) ");
    sql.append("VALUES (:title, :content, :writer, CURRENT_TIMESTAMP)");

    if (board.getContent() == null) {
      board.setContent("");
    }

    SqlParameterSource param = new BeanPropertySqlParameterSource(board);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(), param, keyHolder, new String[]{"board_id"});

    return keyHolder.getKey().longValue();
  }

  //조회
  @Override
  public Optional<Board> findById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT * ");
    sql.append("  FROM board ");
    sql.append(" WHERE board_id = :id ");

    SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

    Board board = null;
    try {
      board = template.queryForObject(sql.toString(), param, boardRowMapper());
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }

    return Optional.of(board);
  }

  //수정
  @Override
  public int updateById(Long boardId, Board board) {
    StringBuilder sql = new StringBuilder();
    sql.append("UPDATE board ");
    sql.append("   SET title = :title, ");
    sql.append("       content = :content, ");
    sql.append("       updated_at = CURRENT_TIMESTAMP ");
    sql.append(" WHERE board_id = :boardId ");

    if (board.getContent() == null) {
      board.setContent("");
    }

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("title", board.getTitle())
        .addValue("content", board.getContent())
        .addValue("boardId", boardId);

    return template.update(sql.toString(), param);
  }

  //삭제
  @Override
  public int deleteById(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM board ");
    sql.append(" WHERE board_id = :id ");

    Map<String, Long> param = Map.of("id", id);
    return template.update(sql.toString(), param);
  }

  //목록 조회
  @Override
  public List<Board> findAll() {
    StringBuffer sql = new StringBuffer();
    sql.append(" SELECT board_id, title, writer, created_at, updated_at ");
    sql.append("   FROM board ");
    sql.append("ORDER BY board_id DESC");

    return template.query(sql.toString(), boardListRowMapper());
  }
}
