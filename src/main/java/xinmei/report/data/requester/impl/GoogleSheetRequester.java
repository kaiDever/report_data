package xinmei.report.data.requester.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xinmei.report.data.bean.SpreadSheetTable;
import xinmei.report.data.bean.SpreadSheetTableExample;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.enums.ValueInputOption;
import xinmei.report.data.ex.ConfigException;
import xinmei.report.data.mapper.SpreadSheetTableMapper;
import xinmei.report.data.requester.Requester;
import xinmei.report.data.utils.GoogleUtil;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GoogleSheetRequester implements Requester {
    private static final String APPLICATION_NAME = "google sheets application";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String rangeAll = "!A:Z";
    private static final String rangeRowTemplate = "!A${rows}:Z";
    private Sheets service;
    private String spreadSheetId;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SpreadSheetTableMapper spreadSheetTableMapper;


    @PostConstruct
    public void init() throws GeneralSecurityException, IOException {
        this.service = getSheets();
        this.spreadSheetId = getGoogleSheetIdBySql();
    }


    private static Sheets getSheets() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleUtil.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private void update(String spreadsheetId, String range, List<List<Object>> values) {
        ValueRange valueRange = new ValueRange();
        valueRange.setValues(values);

        try {
            service.spreadsheets().values()
                    .update(spreadsheetId, range, valueRange)
                    .setValueInputOption(ValueInputOption.RAW.toString())
                    .execute();
        } catch (IOException e) {
            logger.error("update google sheet failed ,{}", e.getMessage());
        }
    }

    private void append(String spreadsheetId, String range, List<List<Object>> values) {
        ValueRange valueRange = new ValueRange();
        valueRange.setValues(values);

        try {
            service.spreadsheets().values()
                    .append(spreadsheetId, range, valueRange)
                    .setValueInputOption(ValueInputOption.RAW.toString())
                    .execute();
        } catch (IOException e) {
            logger.error("append google sheet failed,{}", e.getMessage());
        }
    }

    private List<List<Object>> get(String spreadsheetId, String range, String sheetName) {
        ValueRange response;
        try {
            response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
        } catch (IOException e) {
            logger.info("新建表格[{}]", sheetName);
            createSheet(sheetName);
            return null;
        }
        if (response == null) {
            logger.error("get range :{} do not exist", range);
            return null;
        }
        return response.getValues();
    }

    @VisibleForTesting
    public void createSheet(String sheetName) {
        // Define the new sheet's properties
        SheetProperties properties = new SheetProperties();
        properties.setTitle(sheetName);

        // Define the request to create the new sheet
        AddSheetRequest addSheetRequest = new AddSheetRequest();
        addSheetRequest.setProperties(properties);

        // Define the overall request to batch update the spreadsheet
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateRequest.setRequests(Collections.singletonList(new Request().setAddSheet(addSheetRequest)));

        // Execute the batch update request to create the new sheet
        try {
            service.spreadsheets().batchUpdate(spreadSheetId, batchUpdateRequest).execute();
        } catch (IOException e) {
            logger.error("create sheet failed,{}", e.getMessage());
        }
    }

    private String getGoogleSheetIdBySql() {
        SpreadSheetTableExample example = new SpreadSheetTableExample();
        SpreadSheetTableExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        List<SpreadSheetTable> list = spreadSheetTableMapper.selectByExample(example);
        if (list == null || list.size() == 0 || list.get(0).getSpreadsheetid().isEmpty()) {
            throw new ConfigException("未配置 google sheet id");
        }
        return list.get(0).getSpreadsheetid();
    }

    private List<List<Object>> getNeedUpdateData(List<Object> diffDateList, DataResponse dataResponse) {
        return dataResponse.getRows().stream()
                .filter(list -> diffDateList.contains(list.get(0)))
                .collect(Collectors.toList());

    }

    private List<List<Object>> getNeedAppendDate(List<Object> appendDataList, DataResponse dataResponse) {
        return dataResponse.getRows().stream()
                .filter(list -> appendDataList.contains(list.get(0)))
                .collect(Collectors.toList());
    }


    @Override
    public void save(DataResponse dataResponse, String sheetName) {
        // 获取远端表格数据
        List<List<Object>> remoteList = get(spreadSheetId, sheetName + rangeAll, sheetName);
        List<List<Object>> appendList = new ArrayList<>();
        List<List<Object>> updateList = new ArrayList<>();
        Integer index = null;

        if (remoteList == null || remoteList.size() <= 1) {
            if (remoteList == null || remoteList.size() == 0) {
                appendList.add(dataResponse.getColumns());
            }
            appendList.addAll(dataResponse.getRows());
        } else {
            List<Object> existDateList = new ArrayList<>();
            List<Object> uploadDateList = new ArrayList<>();

            remoteList.remove(0);
            remoteList.forEach(list -> existDateList.add(list.get(0)));
            dataResponse.getRows().forEach(list -> uploadDateList.add(list.get(0)));

            index = existDateList.size();
            List<Object> updateDateList = new ArrayList<>();
            for (Object existDate : existDateList) {
                if (uploadDateList.contains(existDate)) {
                    updateDateList.add(existDate);
                    index--;
                }
            }

            List<Object> appendDateList = uploadDateList.stream()
                    .filter(obj -> !updateDateList.contains(obj))
                    .collect(Collectors.toList());

            updateList.addAll(getNeedUpdateData(updateDateList, dataResponse));
            appendList.addAll(getNeedAppendDate(appendDateList, dataResponse));

        }
        // 进行更新操作
        if (index != null) {
            String range = sheetName + rangeRowTemplate.replace("${rows}", String.valueOf(index + 2));
            update(spreadSheetId, range, updateList);
        }
        // 添加操作
        if (appendList.size() > 0) {
            append(spreadSheetId, sheetName + rangeAll, appendList);
        }
    }

}