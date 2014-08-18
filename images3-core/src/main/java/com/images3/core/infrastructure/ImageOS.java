package com.images3.core.infrastructure;

import java.util.Date;

import com.images3.ImageIdentity;
import com.images3.ImageMetadata;

public class ImageOS {
    
    private ImageIdentity id;
    private Date dateTime;
    private ImageMetadata metadata;
    private VersionOS version;
    
    public ImageOS(ImageIdentity id, Date dateTime, ImageMetadata metadata,
            VersionOS version) {
        this.id = id;
        this.dateTime = dateTime;
        this.metadata = metadata;
        this.version = version;
    }

    public ImageIdentity getId() {
        return id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public ImageMetadata getMetadata() {
        return metadata;
    }

    public VersionOS getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((dateTime == null) ? 0 : dateTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((metadata == null) ? 0 : metadata.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImageOS other = (ImageOS) obj;
        if (dateTime == null) {
            if (other.dateTime != null)
                return false;
        } else if (!dateTime.equals(other.dateTime))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (metadata == null) {
            if (other.metadata != null)
                return false;
        } else if (!metadata.equals(other.metadata))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ImageOS [id=" + id + ", dateTime=" + dateTime + ", metadata="
                + metadata + ", version=" + version + "]";
    }
    
}
