package cc.interstellar;

import java.util.Objects;

public class App {

    private final String name;
    private final String gaiaUrl;

    public App(String name, String gaiaUrl) {
        this.name = name;
        this.gaiaUrl = gaiaUrl;
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
        return Objects.equals(name, app.name) &&
                Objects.equals(gaiaUrl, app.gaiaUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gaiaUrl);
    }

    @Override
    public String toString() {
        return "App{" +
                "name='" + name + '\'' +
                ", gaiaUrl='" + gaiaUrl + '\'' +
                '}';
    }
}
