package xinmei.report.data.provider.druid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xinmei.report.data.bean.PartnerInformation;
import xinmei.report.data.bean.PartnerInformationExample;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.enums.RTBType;
import xinmei.report.data.enums.State;
import xinmei.report.data.enums.TimeZone;
import xinmei.report.data.mapper.PartnerInformationMapper;
import xinmei.report.data.utils.DateUtil;

import java.sql.*;
import java.util.*;

@Service
public class DruidAgent {
    @Value(("${report-data.druid.user}"))
    private String user;
    @Value(("${report-data.druid.password}"))
    private String password;
    @Value(("${report-data.druid.url}"))
    private String url;

    @Autowired
    PartnerInformationMapper partnerInformationMapper;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<Object> titles = Arrays.asList("Date", "sum_bid_count", "sum_win_count", "sum_imp_count",
            "sum_imp_raw_count", "win_rate", "sum_win_price", "sum_revenue");
    private static final String utcQueryTemplate =
            "select\n" +
                    "time_format(__time, 'yyyy-MM-dd ','+00:00') as t,\n" +
                    "sum(bid_count) as sum_bid_count,\n" +
                    "sum(win_count) as sum_win_count,\n" +
                    "sum(imp_count) as sum_imp_count,\n" +
                    "sum(imp_raw_count) as sum_imp_raw_count,\n" +
                    "truncate(1.0*sum(win_count)/sum(bid_count),3) as win_rate,\n" +
                    "TRUNCATE(sum(win_price),3) as sum_win_price,\n" +
                    "TRUNCATE(sum(revenue),3) as sum_revenue,\n" +
                    "1\n" +
                    "from cpm_log\n" +
                    "where 1=1\n" +
                    "and __time >=TIME_PARSE ('%s 00', 'yyyy-M-d H','+00:00')  and __time < TIME_PARSE('%s 00', 'yyyy-M-d H','+00:00')\n" +
                    "and %s\n" +
                    "group by\n" +
                    "time_format(__time, 'yyyy-MM-dd','+00:00'),\n" +
                    "1\n" +
                    "order by t\n" +
                    "limit 1500";

    private static final String utc8QueryTemplate =
            "select\n" +
                    "time_format(__time, 'yyyy-MM-dd ','+08:00') as t,\n" +
                    "sum(bid_count) as sum_bid_count,\n" +
                    "sum(win_count) as sum_win_count,\n" +
                    "sum(imp_count) as sum_imp_count,\n" +
                    "sum(imp_raw_count) as sum_imp_raw_count,\n" +
                    "truncate(1.0*sum(win_count)/sum(bid_count),3) as win_rate,\n" +
                    "TRUNCATE(sum(win_price),3) as sum_win_price,\n" +
                    "TRUNCATE(sum(revenue),3) as sum_revenue,\n" +
                    "1\n" +
                    "from cpm_log\n" +
                    "where 1=1\n" +
                    "and __time >=TIME_PARSE ('%s 00', 'yyyy-M-d H','+08:00')  and __time < TIME_PARSE('%s 00', 'yyyy-M-d H','+08:00')\n" +
                    "and %s\n" +
                    "group by\n" +
                    "time_format(__time, 'yyyy-MM-dd','+08:00'),\n" +
                    "1\n" +
                    "order by t\n" +
                    "limit 1500";


    public Map<String, DataResponse> getDataByDruid(String startDate, String endDate) {
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", user);
        connectionProperties.setProperty("password", password);

        HashMap<String, DataResponse> resultMap = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(url, connectionProperties)) {
            List<PartnerInformation> partnerInformationList = getPartnerInformationListBySql();
            if (partnerInformationList == null || partnerInformationList.size() == 0) {
                return null;
            }

            for (PartnerInformation partnerInformation : partnerInformationList) {
                List<List<Object>> rowsDateList = new ArrayList<>();
                DataResponse dataResponse = new DataResponse();

                String name = partnerInformation.getName();
                String appKey = partnerInformation.getAppKey();
                String appKey2 = partnerInformation.getAppKey2();
                TimeZone utc = partnerInformation.getUtc();
                RTBType type = partnerInformation.getType();

                String partnerCondition = type == RTBType.DSP ? String.format("ad_source = '%s' ", name) :
                        appKey2 == null ? String.format("app_key in ('%s') ", appKey) :
                                String.format("app_key in ('%s','%s') ", appKey, appKey2);
                String executeSql = utc == TimeZone.UTC ? String.format(utcQueryTemplate, startDate, endDate, partnerCondition) :
                        String.format(utc8QueryTemplate, startDate, endDate, partnerCondition);

                try (
                        final Statement statement = connection.createStatement();
                        final ResultSet rs = statement.executeQuery(executeSql)
                ) {
                    while (rs.next()) {
                        List<Object> list = new ArrayList<>();
                        list.add(rs.getString("t"));
                        list.add(rs.getDouble("sum_bid_count"));
                        list.add(rs.getDouble("sum_win_count"));
                        list.add(rs.getDouble("sum_imp_count"));
                        list.add(rs.getDouble("sum_imp_raw_count"));
                        list.add(rs.getDouble("win_rate"));
                        list.add(rs.getDouble("sum_win_price"));
                        list.add(rs.getDouble("sum_revenue"));
                        rowsDateList.add(list);
                    }

                    rowsDateList.sort(DateUtil.comparator);
                    dataResponse.setRows(rowsDateList);
                    dataResponse.setColumns(titles);
                }
                resultMap.put(name, dataResponse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultMap;
    }


    /**
     * 数据库中获取需要查询的合作伙伴信息
     */
    private List<PartnerInformation> getPartnerInformationListBySql() {

        PartnerInformationExample partnerInformationExample = new PartnerInformationExample();
        PartnerInformationExample.Criteria criteria = partnerInformationExample.createCriteria();
        criteria.andIdIsNotNull().andStateEqualTo(State.ACTIVE);
        List<PartnerInformation> partnerInformationList = partnerInformationMapper.selectByExample(partnerInformationExample);

        if (partnerInformationList == null || partnerInformationList.size() <= 0) {
            logger.warn("未配置，无法使用使用内部druid查询");
            return null;
        }

        return partnerInformationList;
    }
}
