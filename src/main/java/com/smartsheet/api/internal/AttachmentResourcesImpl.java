package com.smartsheet.api.internal;

/*
 * Smartsheet SDK for Java
 * Copyright (C) 2023 Smartsheet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.smartsheet.api.AttachmentResources;
import com.smartsheet.api.models.Attachment;

import java.io.File;
import java.io.InputStream;

/**
 * This is the implementation of the AttachmentResources.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and its base class is thread safe.
 * @deprecated As of release 2.0
 */
@Deprecated
public class AttachmentResourcesImpl extends AbstractResources implements AttachmentResources {

    /**
     * Constructor.
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null
     *
     * @param smartsheet the smartsheet
     */
    public AttachmentResourcesImpl(SmartsheetImpl smartsheet) {
        super(smartsheet);
    }

    /**
     * @return the attachment
     * @deprecated As of release 2.0
     */
    @Deprecated
    public Attachment attachNewVersion(long attachmentId, File file, String contentType) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the attachment
     * @deprecated As of release 2.0
     */
    @Deprecated
    public Attachment attachNewVersion(
            long attachmentId,
            InputStream inputStream,
            String contentType,
            long contentLength,
            String attachmentName
    ) {
        throw new UnsupportedOperationException();
    }
}
