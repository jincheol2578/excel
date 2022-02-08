package com.example.excel.board.model;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class BoardDTO {
    private int id;
    private String title;
    private String ctnt;
    private String regdt;
    private String writer;
    private int test;
}
