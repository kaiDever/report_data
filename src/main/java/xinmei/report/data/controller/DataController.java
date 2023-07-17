package xinmei.report.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import xinmei.report.data.service.DataService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DataController {

    @Autowired
    DataService dataService;

    /**
     * 执行入口
     *
     * @param startDate     开始时间
     * @param endDate       结束时间
     * @param requesterFlag 0 WorkSheetRequester 1 GoogleSheetRequester 默认输出到本地excel表格
     */
    @RequestMapping("exec")
    public String exec(String startDate, String endDate, int requesterFlag) {
        return dataService.exec(startDate, endDate, requesterFlag);
    }

    /**
     * 周期性执行脚本程序
     */
    @Scheduled(cron = "0 0 5 * * *")
    public String periodicExec() {
        List<String> date = getDate();
        return dataService.exec(date.get(0), date.get(1), 1);
    }

    private List<String> getDate() {
        List<String> list = new ArrayList<>();
        LocalDate today = LocalDate.now(); // 获取今天的日期

        LocalDate sevenDaysAgo = today.minusDays(7); // 获取距离今天七天前的日期

        if (sevenDaysAgo.getMonth() != today.getMonth()) {
            // 如果七天前的日期在上个月，则使用本月的第一天
            sevenDaysAgo = LocalDate.of(today.getYear(), today.getMonth(), 1);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 定义日期格式

        String endDate = today.format(formatter); // 将日期对象转换为指定格式的字符串
        String startDate = sevenDaysAgo.format(formatter);

        list.add(startDate);
        list.add(endDate);
        return list;
    }
}
