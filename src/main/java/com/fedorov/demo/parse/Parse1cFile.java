package com.fedorov.demo.parse;

import com.fedorov.demo.entity.CorrectPosition;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class Parse1cFile {

    @SneakyThrows
    public Map<String, CorrectPosition> parse(String file) {
        Map<String, CorrectPosition> result = new HashMap<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cellName = row.getCell(4);
                Cell cellCountPlus = row.getCell(6);
                Cell cellCountMinus = row.getCell(7);
                try {
                    String name = parseName(cellName.getStringCellValue());
                    if (name.isEmpty()) continue;
                    double count = (cellCountPlus == null || cellCountPlus.getNumericCellValue() == 0.0) ? cellCountMinus.getNumericCellValue() * -1 : cellCountPlus.getNumericCellValue();
                    CorrectPosition correctPosition = CorrectPosition.builder().code(name).count(count).build();
                    result.merge(name, correctPosition, (first, second) ->
                            CorrectPosition.builder().code(first.getCode()).count(first.getCount() + second.getCount()).build());
                } catch (Exception e) {
                    log.warn("Не обработана строчка с параметрами {}, {}, {}", cellName, cellCountPlus, cellCountMinus);
                }

            }
        }
        return result;
    }

    private String parseName(String stringCellValue) {
        String[] a = stringCellValue.split(" ");
        for (String s : a) {
            if (s.contains("/")) {
                return s.replaceAll("[a-zA-Zа-яА-Я]*", "");
            }
        }
        return "";
    }
}
