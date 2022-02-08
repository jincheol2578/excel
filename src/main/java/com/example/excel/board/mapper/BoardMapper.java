package com.example.excel.board.mapper;

import com.example.excel.board.model.BoardDTO;
import com.example.excel.board.model.ExcelDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<BoardDTO> selBoard();
    int insBoard(BoardDTO param);
    List<ExcelDTO> selTableInfo(String table);
}
