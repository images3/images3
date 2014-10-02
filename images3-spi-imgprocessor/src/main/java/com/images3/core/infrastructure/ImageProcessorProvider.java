package com.images3.core.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.images3.core.infrastructure.spi.ImageProcessor;

public class ImageProcessorProvider {
    
    private Properties config;
    
    public ImageProcessorProvider(String pathToConfig) {
        this(readConfigProperties(pathToConfig));
    }
    
    public ImageProcessorProvider(Properties config) {
        this.config = config;
    }
    
    private static Properties readConfigProperties(String pathToConfig) {
        Properties config = new Properties();
        try {
            InputStream in = new FileInputStream(new File(pathToConfig));
            config.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    public ImageProcessor getImageProcessor() {
        String dir = config.getProperty("image.processing.tempdir");
        return new ImageProcessorImplImgscalr(dir);
    }
}
