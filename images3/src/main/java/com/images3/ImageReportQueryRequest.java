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
package com.images3;

import com.images3.common.ImageMetricsType;
import com.images3.common.TimeInterval;

public class ImageReportQueryRequest {
    
    private String imagePlantId;
    private String templateName;
    private TimeInterval interval;
    private ImageMetricsType[] types;
    
    public ImageReportQueryRequest(String imagePlantId,
            TimeInterval interval, ImageMetricsType[] types) {
        this(imagePlantId, null, interval, types);
    }

    public ImageReportQueryRequest(String imagePlantId, String templateName,
            TimeInterval interval, ImageMetricsType[] types) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
        this.interval = interval;
        this.types = types;
    }

    public String getImagePlantId() {
        return imagePlantId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public ImageMetricsType[] getTypes() {
        return types;
    }
    
    
}
