package cc.interstellar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BlockstackCoreClient {

    private String nodeHost;
    private StringDownloader downloader;

    private static final Type LIST_OF_STRNIGS = new TypeToken<ArrayList<String>>() { }.getType();

    public BlockstackCoreClient(String nodeHost, StringDownloader downloader) {
        this.downloader = downloader;
        this.nodeHost = nodeHost;
    }

    public List<String> downloadAllNames() {
        return downloadPagedArray(String.format("%s/names", nodeHost));
    }

    public List<String> downloadAllSubdomains() {
        return downloadPagedArray(String.format("%s/subdomains", nodeHost));
    }

    public UserDetails downloadUserDetails(String username) {
        String details = downloader.download(String.format("%s/names/%s", nodeHost, username));
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(details).getAsJsonObject();
        String zonefile = obj.get("zonefile").getAsString();
        return new UserDetails(username, parseProfileUrl(zonefile).orElse("na"));
    }

    public List<App> getAppsFromProfile(String profileUrl) {
        String profile = downloader.download(profileUrl);
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(profile);
        if (json.isJsonArray()) {
            JsonObject appsJson = json.getAsJsonArray().get(0).getAsJsonObject()
                    .getAsJsonObject("decodedToken")
                    .getAsJsonObject("payload")
                    .getAsJsonObject("claim")
                    .getAsJsonObject("apps");

            return appsJson.entrySet().stream()
                    .map(entry -> new App(entry.getKey(), entry.getValue().getAsString()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private Optional<String> parseProfileUrl(String zoneFile) {
        Pattern p = Pattern.compile("^_https?._tcp.*\"(http.*)\"$", Pattern.MULTILINE);
        Matcher m = p.matcher(zoneFile);
        if (m.find()) {
            return Optional.of(m.group(1));
        }
        return Optional.empty();
    }

    private List<String> downloadPagedArray(String url) {
        List<String> allNames = new ArrayList<>();
        for (int page = 1; page < 100000; page++) {
            String namesJson = downloader.download(url, page);
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
