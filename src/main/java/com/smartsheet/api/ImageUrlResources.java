/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */
package com.smartsheet.api;

import com.smartsheet.api.models.ImageUrl;
import com.smartsheet.api.models.ImageUrlMap;

import java.util.List;

public interface ImageUrlResources {
    /**
     * <p>Gets URLS that can be used to retrieve the specified cell images.</p>
     *
     * <p>It mirrors to the following Smartsheet REST API method: POST /imageurls</p>
     *
     * @param requestUrls array of requested Images and sizes.
     * @return the ImageUrlMap object (note that if there is no such resource, this method will throw
     * ResourceNotFoundException rather than returning null).
     * @throws IllegalArgumentException if any argument is null or empty string
     * @throws InvalidRequestException if there is any problem with the REST API request
     * @throws AuthorizationException if there is any problem with  the REST API authorization (access token)
     * @throws ResourceNotFoundException if the resource cannot be found
     * @throws ServiceUnavailableException if the REST API service is not available (possibly due to rate limiting)
     * @throws SmartsheetException if there is any other error during the operation
     */
    ImageUrlMap getImageUrls(List<ImageUrl> requestUrls) throws SmartsheetException;
}
