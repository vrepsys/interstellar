package cc.interstellar;

import java.util.Objects;

public class App {

    private final String username;
    private final String name;
    private final String gaiaUrl;

    public App(String username, String name, String gaiaUrl) {
        this.username = username;
        this.name = name;
        this.gaiaUrl = gaiaUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getGaiaUrl() {
        return gaiaUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        App app = (App) o;
        return Objects.equals(username, app.username) &&
                Objects.equals(name, app.name) &&
                Objects.equals(gaiaUrl, app.gaiaUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, gaiaUrl);
    }

    @Override
    public String toString() {
        return "App{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", gaiaUrl='" + gaiaUrl + '\'' +
                '}';
    }
}
