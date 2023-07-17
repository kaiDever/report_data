package xinmei.report.data.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import xinmei.report.data.bean.ProviderAgent;
import xinmei.report.data.bean.ProviderAgentExample;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.enums.State;
import xinmei.report.data.mapper.ProviderAgentMapper;
import xinmei.report.data.provider.druid.DruidAgent;
import xinmei.report.data.provider.Provider;
import xinmei.report.data.requester.Requester;
import xinmei.report.data.requester.impl.GoogleSheetRequester;
import xinmei.report.data.requester.impl.WorkSheetRequester;
import xinmei.report.data.service.DataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ProviderAgentMapper providerAgentMapper;
    @Autowired
    private GoogleSheetRequester googleSheetRequester;
    @Autowired
    private WorkSheetRequester workSheetRequester;
    @Autowired
    DruidAgent innerAgent;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String exec(String startDate, String endDate, int requesterFlag) {
        Requester requester = requesterFlag == 1 ? googleSheetRequester : workSheetRequester;
        updateHasReportApiAgentData(startDate, endDate, requester);
        updateReportByDruid(startDate, endDate, requester);

        return "写入成功";
    }

    private void updateReportByDruid(String startDate, String endDate, Requester requester) {
        Map<String, DataResponse> dataResponseMap = innerAgent.getDataByDruid(startDate, endDate);
        if (dataResponseMap == null || dataResponseMap.size() <= 0) {
            return;
        }
        for (String sheetName : dataResponseMap.keySet()) {
            requester.save(dataResponseMap.get(sheetName), sheetName);
        }
    }

    private void updateHasReportApiAgentData(String startDate, String endDate, Requester requester) {
            for (String providerAgent : getProviderAgentBySql()) {
            Provider provider = getProvider(providerAgent);
            if (provider == null) {
                logger.error("config provider error,[{}]", providerAgent);
                continue;
            }
            // 获取并转换为内部数据
            DataResponse dataResponse = provider.requestData(startDate, endDate);

            if (dataResponse == null) {
                continue;
            }

            // 输出数据
            String sheetName = providerAgent.replaceFirst("Agent", "");
            requester.save(dataResponse, sheetName);
        }
    }

    private Provider getProvider(String providerName) {
        try {
            return (Provider) applicationContext.getBean(providerName);
        } catch (NoSuchBeanDefinitionException e) {
            logger.info("provider bean [{}] not found. Try to create one.", providerName);
            return null;
        } catch (BeansException e) {
            logger.error("getProvider error", e);
            return null;
        }
    }

    /**
     * 数据库中获取提供数据方的代理信息
     */
    private List<String> getProviderAgentBySql() {
        ArrayList<String> result = new ArrayList<>();

        ProviderAgentExample providerAgentExample = new ProviderAgentExample();
        ProviderAgentExample.Criteria criteria = providerAgentExample.createCriteria();
        criteria.andIdIsNotNull().andStateEqualTo(State.ACTIVE);
        List<ProviderAgent> providerAgents = providerAgentMapper.selectByExample(providerAgentExample);

        if (providerAgents == null || providerAgents.size() <= 0) {
            logger.warn("未配置provider agent信息，无法正常使用其功能");
        }

        for (ProviderAgent providerAgent : providerAgents) {
            result.add(providerAgent.getName());
        }
        return result;
    }


}
