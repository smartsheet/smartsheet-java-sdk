package com.smartsheet.api;

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



import com.smartsheet.api.models.Error;

/**
 * <p>This is the exception to indicate a service unavailable error (possibly due to rate limiting) returned from the
 * Smartsheet REST API.</p>
 *
 * <p>Basically this exception will be thrown when the Smartsheet REST API responds with "503 SERVICE UNAVAILABLE".</p>
 *
 * <p>Thread safety: Exceptions are not thread safe.</p>
 */
public class ServiceUnavailableException extends SmartsheetRestException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param error the Error object from Smartsheet REST API
     */
    public ServiceUnavailableException(Error error) {
        super(error);
    }
}
