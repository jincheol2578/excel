package com.example.excel.commons.excel;

import com.example.excel.board.model.BoardDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Component
public class ExcelUpload {

    public List<BoardDTO> excelRead(MultipartFile uploadFile) {
        List<BoardDTO> data = new ArrayList<BoardDTO>();
        try {
            InputStream file = new BufferedInputStream(uploadFile.getInputStream());
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            // 0번째 시트 읽기
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                BoardDTO dto = new BoardDTO();
                Row row = rowIterator.next();
                // 헤더 스킵
                if(row.getRowNum() == 0) {
                    continue;
                }
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int idx = cell.getColumnIndex();
                    switch (idx) {
                        case 0:
                            dto.setId(((Double)getValueFromSell(cell)).intValue());
                            break;
                        case 1:
                            dto.setWriter((String) getValueFromSell(cell));
                            break;
                        case 2:
                            dto.setTitle((String) getValueFromSell(cell));
                            break;
                        case 3:
                            dto.setCtnt((String) getValueFromSell(cell));
                            break;
                        case 4:
                            dto.setRegdt((String) getValueFromSell(cell));
                            break;
                    }
                }
                data.add(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Object getValueFromSell(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
//                if(DateUtil.isCellDateFormatted(cell)) {
//                    return cell.getDateCellValue();
//                }
                return cell.getNumericCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
