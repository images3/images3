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

import com.images3.common.ResizingConfig;
import com.images3.common.TemplateIdentity;

public class TemplateOS {

    private TemplateIdentity id;
    private boolean isArchived;
    private boolean isRemovable;
    private ResizingConfig resizingConfig;
    
    public TemplateOS(TemplateIdentity id, boolean isArchived,
            boolean isRemovable, ResizingConfig resizingConfig) {
        this.id = id;
        this.isArchived = isArchived;
        this.isRemovable = isRemovable;
        this.resizingConfig = resizingConfig;
    }
    
    public boolean isArchived() {
        return isArchived;
    }
    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }
    public boolean isRemovable() {
        return isRemovable;
    }
    public void setRemovable(boolean isRemovable) {
        this.isRemovable = isRemovable;
    }
    public TemplateIdentity getId() {
        return id;
    }
    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }
    
}
