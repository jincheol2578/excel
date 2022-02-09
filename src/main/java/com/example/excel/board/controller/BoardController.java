package com.example.excel.board.controller;

import com.example.excel.board.model.BoardDTO;
import com.example.excel.board.service.BoardService;
import com.example.excel.commons.excel.ExcelDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BoardController {

    final static Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    BoardService service;

    @GetMapping("/excel/download")
    public ResponseEntity excelDownlaod(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "good");
        service.excelDownload(request, response);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @PostMapping("excel/upload")
    public ResponseEntity excelUpload(@RequestParam MultipartFile uploadfile) {
        Map<String, List<BoardDTO>> map = new HashMap<>();
        map.put("data", service.excelUpload(uploadfile));
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @GetMapping("/excel")
    public ResponseEntity selBoard() {
        Map<String, List<BoardDTO>> map = new HashMap<>();
        map.put("data", service.selBoard());
        return new ResponseEntity(map,HttpStatus.OK);
    }

    @PostMapping("/excel")
    public int regBoard(@RequestBody BoardDTO param) {
        return service.insBoard(param);
    }
}
