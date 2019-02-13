package cc.interstellar;

import cc.interstellar.drivers.InterstellarDriver;
import cc.interstellar.fakes.FakeBlockstackNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static cc.interstellar.util.ExtendedAssertions.assertEqualsUnordered;

public class End2endTest {

    @Test
    void main() {
//        FakeBlockstackNode blockstackNode = new FakeBlockstackNode();
//        blockstackNode.start();
//
//        // import names and user profiles from core indexer
//        BlockstackCoreClient importer = new BlockstackCoreClient("localhost", new StringDownloader());
//
//        //imports names/name_details/profile.json
//        importer.importAllFromNode();
//
//        InterstellarDriver driver = new InterstellarDriver();
//
//        List<String> apps = driver.getApps();
//        assertEqualsUnordered(Arrays.asList(
//                "https://dpage.io",
//                "https://xorbrowser.com",
//                "https://www.stealthy.im"), apps);
    }

}
