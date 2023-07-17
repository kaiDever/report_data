package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.openx.oauthdemo.ox_pull_report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.Provider;
import xinmei.report.data.utils.DateUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期为[startDate,endDate)
 */
@Service
public class OpenxRTBAgent extends Provider {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    List<Object> titles = Arrays.asList("day", "revenue");

    @Override
    public String getDataByApi(String startDate, String endDate) {
        // 补全日期
        startDate = startDate + "T00";
        endDate = endDate + "T00";

        // 转换为utc时间
        startDate = DateUtil.coverTimeZone(startDate, false);
        endDate = DateUtil.coverTimeZone(endDate, false);

        // 转换为openx文档日期
        startDate = startDate.replace("-", "").replace("T", "");
        endDate = endDate.replace("-", "").replace("T", "");

        String result = null;
        try {
            result = ox_pull_report.getOpenxDataByApi(startDate, endDate);
        } catch (Exception e) {
            logger.error("get openx date fail {}", e.getMessage());
        }
        return result;
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        if (data == null) {
            return null;
        }
        List<List<Object>> resultList = new ArrayList<>();
        DataResponse dataResponse = new DataResponse();
        JSONObject dataJson = JSONObject.parseObject(data);
        Object reportData = dataJson.get("reportData");
        JSONArray jsonArray = (JSONArray) JSON.toJSON(reportData);
        processData(jsonArray, resultList);

        resultList.sort(DateUtil.comparator);
        dataResponse.setRows(resultList);
        dataResponse.setColumns(titles);
        return dataResponse;
    }

    public void processData(JSONArray jsonArray, List<List<Object>> resultList) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Object metadata : jsonArray) {
            Map<String, Object> map = new HashMap<>();
            JSONObject metaDataJson = (JSONObject) JSON.toJSON(metadata);
            map.put("hour", metaDataJson.get("hour"));
            map.put("marketPublisherRevenueInPCoin", metaDataJson.get("marketPublisherRevenueInPCoin"));
            dataList.add(map);
        }

        // 按天计算marketPublisherRevenueInPCoin的总和
        Map<String, Double> dailyRevenue = calculateDailyRevenue(dataList);

        // 输出每天的总和
        for (String day : dailyRevenue.keySet()) {
            List<Object> list = new ArrayList<>();
            list.add(day);
            list.add(dailyRevenue.get(day));
            resultList.add(list);
        }
    }

    public Map<String, Double> calculateDailyRevenue(List<Map<String, Object>> dataList) {
        Map<String, Double> dailyRevenue = new HashMap<>();

        for (Map<String, Object> data : dataList) {
            String hourString = (String) data.get("hour");
            Double revenue = Double.parseDouble(data.get("marketPublisherRevenueInPCoin").toString());

            // 将时间转换为UTC+8时间
            hourString = DateUtil.coverTimeZone(hourString, true);

            //获取当天日期
            String day = getDay(hourString);
            // 累加每天的revenue
            dailyRevenue.put(day, dailyRevenue.getOrDefault(day, 0.0) + revenue);
        }

        return dailyRevenue;
    }

    public String getDay(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
