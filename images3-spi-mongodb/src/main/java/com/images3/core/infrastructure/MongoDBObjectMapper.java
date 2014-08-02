package com.images3.core.infrastructure;

import java.util.Date;

import com.images3.AmazonS3Bucket;
import com.images3.ImageDimension;
import com.images3.ImageFormat;
import com.images3.ImageIdentity;
import com.images3.ImageMetadata;
import com.images3.ResizingConfig;
import com.images3.ResizingUnit;
import com.images3.VersionIdentity;
import com.mongodb.BasicDBObject;

public class MongoDBObjectMapper {

    public BasicDBObject mapToBasicDBObject(ImagePlantOS source) {
        return new BasicDBObject()
            .append("id", source.getId())
            .append("name", source.getName())
            .append("creationTime", source.getCreationTime().getTime())
            .append("bucket", mapToBasicDBObject(source.getAmazonS3Bucket()));
    }
    
    public ImagePlantOS mapToImagePlantOS(BasicDBObject source) {
        return new ImagePlantOS(
                source.getString("id"),
                source.getString("name"),
                new Date(source.getLong("creationTime")),
                mapToAmazonS3Bucket((BasicDBObject) source.get("bucket")));
    }
    
    public BasicDBObject mapToBasicDBObject(AmazonS3Bucket source) {
        return new BasicDBObject()
            .append("accessKey", source.getAccessKey())
            .append("secretKey", source.getSecretKey())
            .append("name", source.getName());
    }
    
    public AmazonS3Bucket mapToAmazonS3Bucket(BasicDBObject source) {
        return new AmazonS3Bucket(
                source.getString("accessKey"),
                source.getString("secretKey"),
                source.getString("name"));
    }
    
    public BasicDBObject mapToBasicDBObject(TemplateOS source) {
        return new BasicDBObject()
            .append("imagePlantId", source.getImagePlantId())
            .append("id", source.getId())
            .append("name", source.getName())
            .append("isArchived", source.isArchived())
            .append("isRemovable", source.isRemovable())
            .append("resizingConfig", mapToBasicDBObject(source.getResizingConfig()));
    }
    
    public TemplateOS mapToTemplateOS(BasicDBObject source) {
        return new TemplateOS(
                source.getString("imagePlantId"),
                source.getString("id"),
                source.getString("name"),
                source.getBoolean("isArchived"),
                source.getBoolean("isRemovable"),
                mapToResizingConfig((BasicDBObject) source.get("resizingConfig")));
    }
    
    public BasicDBObject mapToBasicDBObject(ResizingConfig source) {
        return new BasicDBObject()
            .append("height", source.getHeight())
            .append("width", source.getWidth())
            .append("unit", source.getUnit())
            .append("isKeepProportions", source.isKeepProportions());
    }
    
    public ResizingConfig mapToResizingConfig(BasicDBObject source) {
        return new ResizingConfig(
                ResizingUnit.valueOf(source.getString("unit")),
                source.getInt("width"),
                source.getInt("height"),
                source.getBoolean("isKeepProportions"));
    }
    
    public BasicDBObject mapToBasicDBObject(ImageOS source) {
        return new BasicDBObject()
            .append("imagePlantId", source.getId().getImagePlantId())
            .append("id", source.getId().getImageId())
            .append("dateTime", source.getDateTime().getTime())
            .append("metadata", mapToBasicDBObject(source.getMetadata()));
    }
    
    public ImageOS mapToImageOS(BasicDBObject source) {
        return new ImageOS(
                new ImageIdentity(
                        source.getString("imagePlantId"), 
                        source.getString("id")),
                new Date(source.getLong("dateTime")),
                mapToImageMetadata((BasicDBObject) source.get("metadata")));
    }
    
    public BasicDBObject mapToBasicDBObject(ImageMetadata source) {
        return new BasicDBObject()
            .append("dimension", mapToBasicDBObject(source.getDimension()))
            .append("format", source.getFormat())
            .append("size", source.getSize());
    }
    
    public ImageMetadata mapToImageMetadata(BasicDBObject source) {
        return new ImageMetadata(
                mapToImageDimension((BasicDBObject) source.get("dimension")),
                ImageFormat.valueOf(source.getString("format")),
                source.getLong("size"));
    }
    
    public BasicDBObject mapToBasicDBObject(ImageDimension source) {
        return new BasicDBObject()
            .append("height", source.getHeight())
            .append("width", source.getWidth());
    }
    
    public ImageDimension mapToImageDimension(BasicDBObject source) {
        return new ImageDimension(
                source.getInt("width"),
                source.getInt("height"));
    }
    
    public BasicDBObject mapToBasicDBObject(VersionOS source) {
        return new BasicDBObject()
            .append("imagePlantId", source.getId().getImageId().getImagePlantId())
            .append("imageId", source.getId().getImageId().getImageId())
            .append("templateId", source.getId().getTemplateId())
            .append("vesioningImageId", source.getVesioningImageId());
    }
    
    public VersionOS mapToVersionOS(BasicDBObject source) {
        return new VersionOS(
                new VersionIdentity(
                        new ImageIdentity(
                                source.getString("imagePlantId"), 
                                source.getString("imageId")),
                        source.getString("templateId")),
                source.getString("vesioningImageId"));
    }
    
}
