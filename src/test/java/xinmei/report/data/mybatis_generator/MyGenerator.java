package xinmei.report.data.mybatis_generator;

import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.ShellRunner;

import java.io.File;
import java.io.IOException;

/**
 * Created by haokuixi on 2022/3/24.
 */
public class MyGenerator {
    @Test
    public void testGenerate() throws IOException {
        File configFile = Resources.getResourceAsFile("generatorConfig.xml");
        String[] arg = {"-configfile", configFile.getPath(), "-overwrite", "-verbose", "-tables",
                "partner_information"};
        ShellRunner.main(arg);
    }
}
