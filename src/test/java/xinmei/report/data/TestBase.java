package xinmei.report.data;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by haokuixi on 2017/6/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataApplication.class)
@ActiveProfiles("local")
@Ignore
public class TestBase {
}