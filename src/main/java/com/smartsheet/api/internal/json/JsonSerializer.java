package com.smartsheet.api.internal.json;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.smartsheet.api.models.BulkItemResult;
import com.smartsheet.api.models.CopyOrMoveRowResult;
import com.smartsheet.api.models.EventResult;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Result;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * This interface defines methods to handle JSON serialization/de-serialization.
 * <p>
 * Thread Safety: Implementation of this interface must be thread safe.
 */
public interface JsonSerializer {

    /**
     * Serialize an object to JSON.
     * <p>
     * Parameters: - object : the object to serialize - outputStream : the output stream to which the JSON will be
     * written
     * <p>
     * Returns: None
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param <T> the generic type
     * @param object the object
     * @param outputStream the output stream
     * @throws JSONSerializerException the JSON serializer exception
     */
    <T> void serialize(T object, java.io.OutputStream outputStream) throws JSONSerializerException;

    /**
     * Serialize an object to JSON.
     * <p>
     * Parameters: - object : the object to serialize - outputStream : the output stream to which the JSON will be
     * written
     * <p>
     * Returns: None
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param <T> the generic type
     * @param object the object
     * @throws JSONSerializerException the JSON serializer exception
     */
    <T> String serialize(T object) throws JSONSerializerException;

    /**
     * De-serialize json to PagedResult.
     * @param objectClass the object class
     * @param inputStream the input stream
     * @param <T> the generic type
     * @return the PagedResult containing a list of type T
     */
    <T> PagedResult<T> deserializeDataWrapper(Class<T> objectClass, java.io.InputStream inputStream) throws JSONSerializerException;

    /**
     * De-serialize an object from JSON.
     * <p>
     * Parameters: - objectClass : the class of the object to de-serialize - inputStream : the input stream from which
     * the JSON will be read
     * <p>
     * Returns: the de-serialized object
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param <T> the generic type
     * @param objectClass the object class
     * @param inputStream the input stream
     * @return the t
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    <T> T deserialize(Class<T> objectClass, java.io.InputStream inputStream) throws JsonParseException,
            JsonMappingException, IOException;

    /**
     * De-serialize an object list from JSON.
     * <p>
     * Parameters: - objectClass : the class of the object (of the list) to de-serialize - inputStream : the input
     * stream from which the JSON will be read
     * <p>
     * Returns: the de-serialized list
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param <T> the generic type
     * @param objectClass the object class
     * @param inputStream the input stream
     * @return the list
     * @throws JSONSerializerException the JSON serializer exception
     */
    <T> List<T> deserializeList(Class<T> objectClass, java.io.InputStream inputStream) throws JSONSerializerException;

    /**
     * De-serialize an object list from JSON to a Map.
     *
     * @param inputStream the input stream
     * @return the map
     * @throws JSONSerializerException the JSON serializer exception
     */
    Map<String, Object> deserializeMap(InputStream inputStream) throws JSONSerializerException;

    /**
     * De-serialize a Result object from JSON.
     * <p>
     * Parameters: - objectClass : the class of the object (of the Result) to de-serialize - inputStream : the input
     * stream from which the JSON will be read
     * <p>
     * Returns: the de-serialized result
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param <T> the generic type
     * @param objectClass the object class
     * @param inputStream the input stream
     * @return the result
     * @throws JSONSerializerException the JSON serializer exception
     */
    <T> Result<T> deserializeResult(Class<T> objectClass, java.io.InputStream inputStream)
            throws JSONSerializerException;

    /**
     * De-serialize a List Result object from JSON.
     * <p>
     * Parameters: - objectClass : the class of the object (of the Result) to de-serialize - inputStream : the input
     * stream from which the JSON will be read
     * <p>
     * Returns: the de-serialized result
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param <T> the generic type
     * @param objectClass the object class
     * @param inputStream the input stream
     * @return the result
     * @throws JSONSerializerException the JSON serializer exception
     */
    <T> Result<List<T>> deserializeListResult(Class<T> objectClass, java.io.InputStream inputStream)
            throws JSONSerializerException;

    /**
     * De-serialize a BulkItemResult object from JSON.
     * <p>
     * Parameters:
     * - inputStream : the input stream from which the JSON will be read
     * <p>
     * Returns: the de-serialized result
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param <T> the generic type
     * @param objectClass the object class
     * @param inputStream the input stream
     * @return the result
     * @throws JSONSerializerException the JSON serializer exception
     */
    <T> BulkItemResult<T> deserializeBulkItemResult(Class<T> objectClass, java.io.InputStream inputStream)
            throws JSONSerializerException;

    /**
     * De-serialize a Result object from JSON.
     * <p>
     * Parameters: - objectClass : the class of the object (of the Result) to de-serialize - inputStream : the input
     * stream from which the JSON will be read
     * <p>
     * Returns: the de-serialized result
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param inputStream the input stream
     * @return the result
     * @throws JSONSerializerException the JSON serializer exception
     */
    CopyOrMoveRowResult deserializeCopyOrMoveRow(java.io.InputStream inputStream)
            throws JSONSerializerException;

    /**
     * De-serialize json to EventResult.
     * @param inputStream the input stream
     * @return the EventResult containing a list of Event
     */
    EventResult deserializeEventResult(java.io.InputStream inputStream)
            throws JSONSerializerException;

}
