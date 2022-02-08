package com.example.excel.commons.excel;

import com.example.excel.board.mapper.BoardMapper;
import com.example.excel.board.model.BoardDTO;
import com.example.excel.board.model.ExcelDTO;
import com.example.excel.commons.utils.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ExcelDownload {

    @Autowired
    BoardMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public XSSFWorkbook excelDownload(String kind, String[] headerKey, List<ExcelDTO> tableInfo) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        List<BoardDTO> param = mapper.selBoard();
        //시트명
        Sheet sheet = workbook.createSheet(kind);
        // 행,열
        Row row = null;
        Cell cell = null;

        // 테이블 헤더 스타일
        CellStyle headerStyle = CellStyleSetting(workbook, "header");
        CellStyle dataStyle = CellStyleSetting(workbook, "data");
        CellStyle dateStyle = CellStyleSetting(workbook, "date");

        row = sheet.createRow(0);
        for(int i=0; i<headerKey.length; i++) { // 헤더 추가
            cell = row.createCell(i);
            cell.setCellValue(headerKey[i]);
            cell.setCellStyle(headerStyle);
        }

        for(int i=0; i<param.size(); i++) {     // 데이터 추가
            row = sheet.createRow(i + 1);

            BoardDTO dto = param.get(i);

            String str = dto.getRegdt();

            for(int j=0; j<headerKey.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(Utils.getMethod(dto, headerKey, j, tableInfo));

                if(tableInfo.get(j).getType().equals("int")) {
                    cell.setCellType(CellType.NUMERIC);
                }
                if(tableInfo.get(j).getType().equals("datetime")) {
                    cell.setCellStyle(dateStyle);
                } else {
                    cell.setCellStyle(dataStyle);
                }
            }
        }

        // 셀 넓이 자동조절
        for(int i=0; i<headerKey.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i));
        }

        return workbook;
    }

    //각 셀의 스타일 세팅
    public CellStyle CellStyleSetting(XSSFWorkbook workbook, String kind) {
        //테이블 스타일
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFDataFormat format = workbook.createDataFormat();

        //가는 경계선
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        if(kind.equals("header")) {
            //배경색 회색
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        //데이터는 가운데 정렬
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //중앙 정렬

        //폰트 설정
        Font fontOfGothic = workbook.createFont();
        fontOfGothic.setFontName("맑은 고딕");
        cellStyle.setFont(fontOfGothic);

        //날짜형식
        if(kind.equals("date")) {
            cellStyle.setDataFormat(format.getFormat("yyyy-mm-dd hh:mm:ss"));
        }

        return cellStyle;
    }

    public void reqExcelDownload(HttpServletRequest request, HttpServletResponse response, String table) throws Exception {
        OutputStream outs = response.getOutputStream();
        List<ExcelDTO> tableInfo = mapper.selTableInfo(table);
        String[] headerKey = new String[tableInfo.size()];
        System.out.println(tableInfo);


        // 파일명 인코딩
        String fileName = table + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 브라우저별 파일 인코딩 FileUtil 만들기
        // String encFileName = FileUtil.get (request, fileName, "UTF-8");
        logger.info(">>> [" + fileName + "] 엑셀 다운로드 시작.");

        try {
            XSSFWorkbook workbook = null;

            for(int i=0; i<tableInfo.size(); i++) {
                headerKey[i] = tableInfo.get(i).getColumn();
            }
            workbook = excelDownload(table, headerKey, tableInfo);

            // 컨텐츠 타입과 파일명 지정
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");

            workbook.write(outs);
        } catch (IOException e) {
            logger.error(">>> 엑셀 다운로드 도중 오류가 발생했습니다. : {}", e);
            e.printStackTrace();

            response.reset();
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter pout = response.getWriter();
            pout.println("<script type=\"text/javascript\">");
            pout.println("alert('[IOException] 엑셀 다운로드 도중 오류가 발생했습니다.\\n시스템 관리자에게 문의 바랍니다.');history.go(-1);");
            pout.println("</script>");
            pout.flush();
        } catch (Exception e) {
            logger.error(">>> 엑셀 다운로드 도중 오류가 발생했습니다. : {}", e);
            e.printStackTrace();

            response.reset();
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter pout = response.getWriter();
            pout.println("<script type=\"text/javascript\">");
            pout.println("alert('[Exception] 엑셀 다운로드 도중 오류가 발생했습니다.\\n시스템 관리자에게 문의 바랍니다.');history.go(-1);");
            pout.println("</script>");
            pout.flush();
        } finally {
            logger.info(">>> 엑셀 다운로드 종료.");
            if(outs != null) outs.close();
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }
}
