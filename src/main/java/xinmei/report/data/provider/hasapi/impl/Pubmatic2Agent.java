package xinmei.report.data.provider.hasapi.impl;

import org.springframework.stereotype.Service;

@Service
public class Pubmatic2Agent extends PubmaticAgent {
    Pubmatic2Agent() {
        this.setReportApi("http://api.pubmatic.com/v1/analytics/data/publisher/161816");
    }

}
