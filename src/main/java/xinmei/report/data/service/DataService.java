package xinmei.report.data.service;

import org.springframework.stereotype.Service;

@Service
public interface DataService {
    String exec(String startDate, String endDate,int requesterFlag);
}
