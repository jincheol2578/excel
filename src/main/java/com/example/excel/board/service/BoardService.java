package com.example.excel.board.service;

import com.example.excel.board.mapper.BoardMapper;
import com.example.excel.board.model.BoardDTO;
import com.example.excel.commons.excel.ExcelDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class BoardService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    BoardMapper mapper;

    @Autowired
    ExcelDownload excelDownload;

    public List<BoardDTO> selBoard() {
        return mapper.selBoard();
    }

    public int insBoard(BoardDTO param) {
        return mapper.insBoard(param);
    }

    public void excelDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        excelDownload.reqExcelDownload(request, response, "t_board");
    }

}
