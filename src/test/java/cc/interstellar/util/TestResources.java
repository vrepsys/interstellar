package cc.interstellar.util;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestResources {

    public static String VAL_REPSYS_PROFILE = loadResource("fake_blockstack_node/valrepsys.id.blockstack.json");

    private static String loadResource(String path) {
        try {
            URL url = TestResources.class.getClassLoader().getResource(path);
            return Files.readString(Path.of(url.toURI()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
