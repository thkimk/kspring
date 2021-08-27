package com.kkk.sbgtest.stock;

import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockItems {
    List<StockItem> items = new ArrayList<>();

    public StockItems() {
        readExcel();
    }

    void readExcel() {
        try {
            FileInputStream file = new FileInputStream("d:/stock/stock.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet("stocks");

            // 행(Raw)만큼 looping
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                XSSFRow row = sheet.getRow(i);
                StockItem lItem = new StockItem();

                // 열(cell)만큼 looping
                int cells = row.getPhysicalNumberOfCells();
                for (int j = 0; j < cells; j++) {
                    XSSFCell cell = row.getCell(j);
                    switch (j) {
                        case 0: lItem.setCode(cell.toString()); break;
                        case 1: lItem.setName(cell.toString()); break;
                        case 2: lItem.setType(cell.toString()); break;
                    }
                }

                // 종목 1개 완성
                items.add(lItem);
            }
        } catch (Exception e) {

        }
    }
}
