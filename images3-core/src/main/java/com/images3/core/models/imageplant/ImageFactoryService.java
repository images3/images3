package com.images3.core.models.imageplant;

import java.io.File;
import java.util.Date;

import com.images3.DuplicateImageVersionException;
import com.images3.ImageIdentity;
import com.images3.ImageMetadata;
import com.images3.UnknownImageFormatException;
import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.VersionOS;
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageProcessor;

public class ImageFactoryService {
    
    private ImageAccess imageAccess;
    private ImageProcessor imageProcessor;
    
    public ImageFactoryService(ImageAccess imageAccess, ImageProcessor imageProcessor) {
        this.imageAccess = imageAccess;
        this.imageProcessor = imageProcessor;
    }

    public ImageEntity generateImage(ImagePlantRoot imagePlant, File imageContent, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        String id = imageAccess.generateImageId(imagePlant.getObjectSegment());
        if (!imageProcessor.isSupportedFormat(imageContent)) {
            throw new UnknownImageFormatException(id);
        }
        TemplateEntity template = (TemplateEntity) imagePlant.getMasterTemplate();
        Version version = new Version(template, null);
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageContent);
        File resizedContent = imageProcessor.resizeImage(
                metadata, 
                imageContent, 
                version.getTemplate().getResizingConfig());
        return generateImage(
                imagePlant, 
                resizedContent,
                version,
                imageRepository, 
                templateRepository);
    }
    
    public ImageEntity generateImage(ImagePlantRoot imagePlant, ImageEntity originalImage, 
            TemplateEntity template, ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository) {
        if (imageAccess.isDuplicateVersion(
                imagePlant.getId(), new VersionOS(template.getName(), originalImage.getId()))) {
            throw new DuplicateImageVersionException(
                    template.getName(), originalImage.getId());
        }
        Version version = new Version(template, originalImage);
        File resizedContent = imageProcessor.resizeImage(
                originalImage.getObjectSegment().getMetadata(), 
                originalImage.getContent(), 
                version.getTemplate().getResizingConfig());
        return generateImage(
                imagePlant,  
                resizedContent, 
                version,
                imageRepository,
                templateRepository);
    }
    
    private ImageEntity generateImage(ImagePlantRoot imagePlant, File imageContent, Version version,
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        VersionOS versionOS = getVersionOS(version);
        String imageId = imageAccess.generateImageId(imagePlant.getObjectSegment());
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageContent);
        ImageOS objectSegment = generateImageOS(
                imageId, metadata, imagePlant.getObjectSegment(), versionOS);
        ImageEntity entity = reconstituteImage(
                imagePlant, objectSegment, imageContent, imageRepository, templateRepository, version);
        entity.markAsNew();
        return entity;
    }
    
    private VersionOS getVersionOS(Version version) {
        Template template = version.getTemplate();
        Image originalImage = version.getOriginalImage();
        VersionOS versionOS = null;
        if (null != originalImage) {
            versionOS = new VersionOS(template.getName(), originalImage.getId());
        } else {
            versionOS = new VersionOS(template.getName(), null);
        }
        return versionOS;
    }
    
    private ImageOS generateImageOS(String id, ImageMetadata metadata, 
            ImagePlantOS imagePlantOS, VersionOS versionOS) {
        Date dateTime = new Date(System.currentTimeMillis());
        ImageOS objectSegment = new ImageOS(
                new ImageIdentity(imagePlantOS.getId(), id), dateTime, metadata, versionOS);
        return objectSegment;
    }
    
    public ImageEntity reconstituteImage(ImagePlantRoot imagePlant, ImageOS objectSegment, File imageContent,
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository, Version version) {
        if (null == objectSegment) {
            return null;
        }
        return new ImageEntity(
                imagePlant, objectSegment, imageContent, imageRepository, templateRepository, version);
    }
    
}
