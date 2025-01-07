package kz.mathncode.backend.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class URLResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String shortURL;

    @Column
    private String fullURL;

    @ManyToOne
    private User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime createdAt;

    public URLResource() {
    }

    public URLResource(UUID id, String shortURL, String fullURL, User createdBy, ZonedDateTime createdAt) {
        this.id = id;
        this.shortURL = shortURL;
        this.fullURL = fullURL;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public String getFullURL() {
        return fullURL;
    }

    public void setFullURL(String fullURL) {
        this.fullURL = fullURL;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        URLResource that = (URLResource) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "URLResource{" +
                "id=" + id +
                ", shortURL='" + shortURL + '\'' +
                ", fullURL='" + fullURL + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                '}';
    }
}
