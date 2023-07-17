package xinmei.report.data.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xinmei.report.data.provider.hasapi.impl.PubmaticAgent;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnProperty(prefix = "report-data.api-token-update-worker", name = "enabled")

public class TokenWorker {
    @Autowired
    private PubmaticAgent pubmaticAgent;

    @PostConstruct
    private void init() {
        updateApiToken();
    }

    @Scheduled(cron = "0 0 0 */10 * ?")
    public void updateApiToken() {
        updatePubmaticToken();
    }

    public void updatePubmaticToken() {
        String response = pubmaticAgent.refreshToken();
        pubmaticAgent.updateApiTokenToSql(response);
    }


}
