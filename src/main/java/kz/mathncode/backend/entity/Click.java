package kz.mathncode.backend.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Click {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private URLResource resource;

    @Column
    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime createdAt;

    public Click() {
    }

    public Click(UUID id, URLResource resource, String ipAddress, ZonedDateTime createdAt) {
        this.id = id;
        this.resource = resource;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public URLResource getResource() {
        return resource;
    }

    public void setResource(URLResource resource) {
        this.resource = resource;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
        Click click = (Click) o;
        return Objects.equals(id, click.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Click{" +
                "id=" + id +
                ", resource=" + resource +
                ", ipAddress='" + ipAddress + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
