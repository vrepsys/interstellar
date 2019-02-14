package cc.interstellar;

import java.util.Objects;

public class UserDetails {
    private final String username;
    private final String profileUrl;

    public UserDetails(String username, String profileUrl) {
        this.username = username;
        this.profileUrl = profileUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetails that = (UserDetails) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(profileUrl, that.profileUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, profileUrl);
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "username='" + username + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }
}
