package com.images3.core;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.images3.common.ImageReportType;

public class ImageReport {
    
    private ImagePlant imagePlant;
    private Template template;
    private List<Date> times;
    private List<Long> values;
    private TimeUnit scale;
    private ImageReportType type;
    
    public ImageReport(ImagePlant imagePlant, Template template,
            List<Date> times, List<Long> values, TimeUnit scale,
            ImageReportType type) {
        this.imagePlant = imagePlant;
        this.template = template;
        this.times = times;
        this.values = values;
        this.scale = scale;
        this.type = type;
    }
    public ImagePlant getImagePlant() {
        return imagePlant;
    }
    public Template getTemplate() {
        return template;
    }
    public List<Date> getTimes() {
        return times;
    }
    public List<Long> getValues() {
        return values;
    }
    public TimeUnit getScale() {
        return scale;
    }
    public ImageReportType getType() {
        return type;
    }
    
    
}
