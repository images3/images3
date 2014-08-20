package com.images3;

public class ImagePlantUpdateRequest {

    private String id;
    private String name;
    private AmazonS3Bucket bucket;
    
    public ImagePlantUpdateRequest() {}

    public ImagePlantUpdateRequest(String id, String name, AmazonS3Bucket bucket) {
        this.id = id;
        this.name = name;
        this.bucket = bucket;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AmazonS3Bucket getBucket() {
        return bucket;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        ImagePlantUpdateRequest other = (ImagePlantUpdateRequest) obj;
        if (bucket == null) {
            if (other.bucket != null)
                return false;
        } else if (!bucket.equals(other.bucket))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
}
