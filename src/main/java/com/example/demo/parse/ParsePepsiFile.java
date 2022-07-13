package com.example.demo.parse;

import com.example.demo.entity.CorrectPosition;
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
public class ParsePepsiFile {
//    private File file = new File("D:/Downloads/parse/demo/src/main/resources/тара расшифрока 200248248.XLSX");

    @SneakyThrows
    public Map<String, CorrectPosition> parse(String file) {
        Map<String, CorrectPosition> result = new HashMap<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cellName = row.getCell(6);
                Cell cellCount = row.getCell(8);
                try {
                    String name = parseName(cellName.getStringCellValue());
                    if (name.isEmpty()) continue;
                    CorrectPosition correctPosition = CorrectPosition.builder().code(cellName.getStringCellValue()).count(cellCount.getNumericCellValue()).build();
                    result.merge(name, correctPosition, (first, second) ->
                            CorrectPosition.builder().code(first.getCode()).count(first.getCount() + second.getCount()).build());
                } catch (Exception e) {
                    log.warn("Не обработана строчка с параметрами {}, {}", cellName, cellCount);
                }
            }
        }
        return result;
    }

    private String parseName(String stringCellValue) {
        return stringCellValue.replaceAll("[a-zA-Zа-яА-Я]*", "");
    }
}
