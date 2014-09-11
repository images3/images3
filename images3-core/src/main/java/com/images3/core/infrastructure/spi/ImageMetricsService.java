package com.images3.core.infrastructure.spi;

import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

import com.images3.common.TemplateIdentity;
import com.images3.common.TimeInterval;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.ImageMetricsOS;

public interface ImageMetricsService extends PaginatedResultDelegate<List<ImageMetricsOS>> {

    public void record(ImageOS image);
    
    public long calculateNumberOfImages(String imagePlantId);
    
    public long calculateNumberOfImages(TemplateIdentity templateId);
   
    public long calculateSizeOfImages(String imagePlantId);
    
    public long calculateSizeOfImages(TemplateIdentity templateId);
    
    public PaginatedResult<List<ImageMetricsOS>> retrieveStats(
            String imagePlantId, TimeInterval interval);
    
    public PaginatedResult<List<ImageMetricsOS>> retrieveStats(
            TemplateIdentity templateId, TimeInterval interval);
    
}
