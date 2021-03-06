/*******************************************************************************
 * Copyright 2014 Rui Sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.images3.data.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.images3.data.spi.ImageProcessor;

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
