package com.example.excel.commons.utils;

import com.example.excel.board.model.BoardDTO;
import com.example.excel.board.model.ExcelDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

public class Utils {

    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public static String getMethod(BoardDTO dto, String[] headerKey, int idx, List<ExcelDTO> tableInfo) {
        Method getMethod = null;
        Object methodResult = null;
        try {
            getMethod = dto.getClass().getMethod("get"+ upperCaseFirst(headerKey[idx]));
            methodResult = getMethod.invoke(dto);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        String result = String.valueOf(methodResult);

        if(tableInfo.get(idx).getType().equals("datetime")) {
            result = result.substring(0, result.length()-2);
        }
        return result;
    }
}
