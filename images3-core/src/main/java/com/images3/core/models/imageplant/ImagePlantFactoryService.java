package com.images3.core.models.imageplant;

import java.util.Date;

import com.images3.AmazonS3Bucket;
import com.images3.ResizingConfig;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantFactory;
import com.images3.core.Template;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;

public class ImagePlantFactoryService implements ImagePlantFactory {
    
    private final static String MASTER_TEMPLATE_NAME = "Master";
    
    private ImagePlantAccess imagePlantAccess;
    private TemplateFactoryService templateFactory;
    private ImageFactoryService imageFactory;
    
    public ImagePlantFactoryService(ImagePlantAccess imagePlantAccess,
            TemplateFactoryService templateFactory,
            ImageFactoryService imageFactory) {
        this.imagePlantAccess = imagePlantAccess;
        this.templateFactory = templateFactory;
        this.imageFactory = imageFactory;
    }

    @Override
    public ImagePlant generateImagePlant(String name, 
            AmazonS3Bucket amazonS3Bucket, ResizingConfig resizingConfig) {
        String id = imagePlantAccess.genertateImagePlantId();
        Date creationTime = new Date(System.currentTimeMillis());
        ImagePlantOS objectSegment =
                new ImagePlantOS(id, "", creationTime, amazonS3Bucket, MASTER_TEMPLATE_NAME);
        ImagePlantRoot root = reconstituteImagePlant(objectSegment, null, null);
        root.markAsNew();
        root.updateName(name);
        addMasterTemplate(root, resizingConfig);
        return root;
    }
    
    private void addMasterTemplate(ImagePlantRoot root, ResizingConfig resizingConfig) {
        Template masterTemplate = root.createTemplate(MASTER_TEMPLATE_NAME, resizingConfig);
        masterTemplate.setArchived(false); //bring to active immediately.
        root.updateTemplate(masterTemplate);
    }
    
    public ImagePlantRoot reconstituteImagePlant(ImagePlantOS objectSegment, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        if (null == objectSegment) {
            return null;
        }
        return new ImagePlantRoot(objectSegment,imagePlantAccess, imageFactory, 
                imageRepository, templateFactory, templateRepository);
    }

}