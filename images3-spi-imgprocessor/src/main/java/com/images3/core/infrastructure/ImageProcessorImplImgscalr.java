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
package com.images3.core.infrastructure;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.imgscalr.Scalr;

import com.images3.common.ImageDimension;
import com.images3.common.ImageFormat;
import com.images3.common.ImageMetadata;
import com.images3.common.ResizingConfig;
import com.images3.common.ResizingUnit;
import com.images3.core.infrastructure.spi.ImageProcessor;

public class ImageProcessorImplImgscalr implements ImageProcessor {
    
    private static final byte[] JPG_MAGIC_NUMBER = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0};
    private static final byte[] PNG_MAGIC_NUMBER = new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47};
    private static final byte[] BMP_MAGIC_NUMBER = new byte[]{(byte) 0x42, (byte) 0x4D};
    
    private final String tempDir;
    
    public ImageProcessorImplImgscalr(String tempDir) {
        this.tempDir = tempDir;
    }

    @Override
    public boolean isSupportedFormat(File imageFile) {
        return (null != getImageFormat(imageFile));
    }

    public ImageMetadata readImageMetadata(File imageFile) {
        ImageMetadata metadata = null;
        ImageInputStream imageInputStream = null;
        ImageReader imageReader = null;
        try {
            imageInputStream = ImageIO.createImageInputStream(imageFile);
            imageReader = getImageReader(imageFile, imageInputStream);
            metadata = createImageMetadata(imageFile, imageReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != imageInputStream) {
                try {
                    imageInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (null != imageReader) {
                imageReader.dispose();
            }
        }
        return metadata;
    }
    
    private ImageReader getImageReader(File imageFile, ImageInputStream imageInputStream) {
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
        if (!imageReaders.hasNext()) {
            throw new IllegalArgumentException(imageFile.getAbsolutePath());
        }
        ImageReader imageReader = imageReaders.next();
        imageReader.setInput(imageInputStream);
        return imageReader;
    }
    
    private ImageMetadata createImageMetadata(File imageFile, ImageReader imageReader) throws IOException {
        ImageDimension dimension = new ImageDimension(imageReader.getWidth(0), imageReader.getHeight(0));
        ImageFormat format = ImageFormat.valueOf(imageReader.getFormatName().toUpperCase());
        return new ImageMetadata(dimension, format, imageFile.length());
    }

    public File resizeImage(ImageMetadata metadata, File imageFile, ResizingConfig resizingConfig) {
        File resizedImageFile = null;
        try {
            BufferedImage originalImage = ImageIO.read(
                    new BufferedInputStream(Files.newInputStream(imageFile.toPath())));
            resizingConfig = getResizingConfig(metadata, resizingConfig);
            BufferedImage resizedImage = resizeImage(originalImage, resizingConfig);
            String fileName = UUID.randomUUID().toString();
            resizedImageFile = prepareImageFile(tempDir + File.separator + fileName);
            ImageIO.write(
                    resizedImage,
                    getImageFormat(imageFile).toString(), 
                    new BufferedOutputStream(Files.newOutputStream(resizedImageFile.toPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resizedImageFile;
    }
    
    private ResizingConfig getResizingConfig(ImageMetadata metadata, ResizingConfig resizingConfig) {
        if (resizingConfig.getUnit() == ResizingUnit.PERCENT) {
            return new ResizingConfig(
                    ResizingUnit.PIXEL, 
                    metadata.getDimension().getWidth() * (resizingConfig.getWidth() / 100), 
                    metadata.getDimension().getHeight() * (resizingConfig.getHeight() / 100),
                    resizingConfig.isKeepProportions());
        }
        return resizingConfig;
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, ResizingConfig resizingConfig) {
        BufferedImage resizedImage = null;
        if (resizingConfig.isKeepProportions()) {
            resizedImage = Scalr.resize(
                    originalImage, Scalr.Method.SPEED, Scalr.Mode.AUTOMATIC, 
                    resizingConfig.getWidth(), resizingConfig.getHeight(), Scalr.OP_ANTIALIAS);
        } else {
            resizedImage = Scalr.resize(
                    originalImage, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, 
                    resizingConfig.getWidth(), resizingConfig.getHeight(), Scalr.OP_ANTIALIAS);
        }
        return resizedImage;
    }
    
    private File prepareImageFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            throw new IllegalArgumentException("Image file, " + file.getName() + " has already exist!");
        } else {
            file.createNewFile();
        }
        return file;
    }
    
    public ImageFormat getImageFormat(File imageFile) {
        InputStream imageInputStream = null;
        try {
            imageInputStream = Files.newInputStream(imageFile.toPath());
            byte[] magicBytes = new byte[4];
            imageInputStream.read(magicBytes);
            if (isJPEG(magicBytes)) {
                return ImageFormat.JPEG;
            }
            if (isPNG(magicBytes)) {
                return ImageFormat.PNG;
            }
            if (isBMP(magicBytes)) {
                return ImageFormat.BMP;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != imageInputStream) {
                try {
                    imageInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
    
    private static boolean isJPEG(byte[] content) {
        if(content.length < 4) return false;
        return (content[0] == JPG_MAGIC_NUMBER[0]
                        && content[1] == JPG_MAGIC_NUMBER[1]
                        && content[2] == JPG_MAGIC_NUMBER[2]
                        && content[3] == JPG_MAGIC_NUMBER[3]);
    }

    private static boolean isPNG(byte[] content) {
            if(content.length < 4) return false;
            return (content[0] == PNG_MAGIC_NUMBER[0]
                            && content[1] == PNG_MAGIC_NUMBER[1]
                            && content[2] == PNG_MAGIC_NUMBER[2]
                            && content[3] == PNG_MAGIC_NUMBER[3]);
    }
    
    private static boolean isBMP(byte[] content) {
            if(content.length < 2) return false;
            return (content[0] == BMP_MAGIC_NUMBER[0]
                            && content[1] == BMP_MAGIC_NUMBER[1]);
    }

}
