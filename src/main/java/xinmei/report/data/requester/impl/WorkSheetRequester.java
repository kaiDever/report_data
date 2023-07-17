package xinmei.report.data.requester.impl;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.requester.Requester;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * dateResponse -> local excel
 */
@Service
public class WorkSheetRequester implements Requester {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void saveToLocalExcel(DataResponse dataResponse, String sheetName) {
        // Create Excel file and sheet
        Workbook workbook;
        try {
            // 打开要操作的Excel文件
            String filePath = "test.xlsx";
            File file = new File(filePath);
            if (file.exists()) {
                workbook = new XSSFWorkbook(Files.newInputStream(file.toPath()));
            } else {
                workbook = new XSSFWorkbook();
            }


            // 找到要添加数据的工作表
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            // 写入表头
            Row headerRow = sheet.createRow(0);
            List<Object> columns = dataResponse.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i).toString());
            }

            // 写入数据
            List<List<Object>> rows = dataResponse.getRows();
            int rowNum = 1;
            for (List<Object> row : rows) {
                Row dataRow = sheet.createRow(rowNum);
                rowNum++;
                for (int i = 0; i < row.size(); i++) {
                    Cell cell = dataRow.createCell(i);
                    cell.setCellValue(row.get(i).toString());
                }
            }

            // 将Excel文件保存
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            out.close();
            workbook.close();
        } catch (IOException e) {
            logger.error("写入本地excel失败,{}", e.getMessage());
        }
    }


    @Override
    public void save(DataResponse dataResponse, String sheetName) {
        saveToLocalExcel(dataResponse, sheetName);
    }
}



