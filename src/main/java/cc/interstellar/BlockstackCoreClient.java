package cc.interstellar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BlockstackCoreClient {

    private String nodeHost;
    private StringDownloader downloader;

    private static final Type LIST_OF_STRNIGS = new TypeToken<ArrayList<String>>() { }.getType();

    public BlockstackCoreClient(String nodeHost, StringDownloader downloader) {
        this.downloader = downloader;
        this.nodeHost = nodeHost;
    }

    public void importAllFromNode() {
    }

    public List<String> downloadAllNames() {
        return downloadPagedArray(String.format("%s/names", nodeHost));
    }

    public List<String> downloadAllSubdomains() {
        return downloadPagedArray(String.format("%s/subdomains", nodeHost));
    }

    private List<String> downloadPagedArray(String url) {
        List<String> allNames = new ArrayList<>();
        for (int page = 1; page < 100000; page++) {
            String namesJson = downloader.downloadPage(url, page);
            List<String> namesInPage = new Gson().fromJson(namesJson, LIST_OF_STRNIGS);
            if (!namesInPage.isEmpty()) {
                allNames.addAll(namesInPage);
            } else {
                break;
            }
        }
        return allNames;
    }
}
