-- 테이블 삭제 --
drop table BOARD;
-- 시퀀스 삭제 --
drop sequence BOARD_SEQ;

-- 코드 --

-- 시퀀스 생성
CREATE SEQUENCE BOARD_SEQ START WITH 1 INCREMENT BY 1;

-- 게시판 테이블 생성
CREATE TABLE BOARD (
    BOARD_ID NUMBER(11),                        -- 게시글 ID (PK)
    TITLE VARCHAR2(30),                         -- 제목
    CONTENT CLOB,                               -- 내용
    WRITER VARCHAR2(11) NOT NULL,               -- 작성자
    CREATED_AT TIMESTAMP DEFAULT SYSTIMESTAMP,  -- 작성 날짜
    UPDATED_AT TIMESTAMP DEFAULT SYSTIMESTAMP   -- 수정 날짜
);
--기본키
alter table BOARD add Constraint BOARD_ID_PK primary key (BOARD_ID);

CREATE OR REPLACE TRIGGER BOARD_ID_TRG
BEFORE INSERT ON BOARD
FOR EACH ROW
BEGIN
  IF :NEW.BOARD_ID IS NULL THEN
    SELECT BOARD_SEQ.NEXTVAL INTO :NEW.BOARD_ID FROM DUAL;
  END IF;
END;

COMMIT;
