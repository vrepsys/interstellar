package cc.interstellar;

import java.time.Instant;
import java.util.Objects;

public class BlockstackIdentity {

    private String username;

    private Instant createdAt;

    private Instant profileUpdatedAt;

    public BlockstackIdentity(String username, Instant createdAt, Instant profileUpdatedAt) {
        this.username = username;
        this.createdAt = createdAt;
        this.profileUpdatedAt = profileUpdatedAt;
    }

    public BlockstackIdentity(String username, Instant createdAt) {
        this.username = username;
        this.createdAt = createdAt;
        this.profileUpdatedAt = null;
    }

    public String getUsername() {
        return username;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getProfileUpdatedAt() {
        return profileUpdatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockstackIdentity that = (BlockstackIdentity) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(profileUpdatedAt, that.profileUpdatedAt);
    }

    @Override
    public String toString() {
        return "BlockstackIdentity{" +
                "username='" + username + '\'' +
                ", createdAt=" + createdAt +
                ", profileUpdatedAt=" + profileUpdatedAt +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, createdAt, profileUpdatedAt);
    }
}
