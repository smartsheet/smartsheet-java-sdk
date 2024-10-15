/*
 * Copyright (C) 2024 Smartsheet
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

package com.smartsheet.api.internal.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.smartsheet.api.internal.util.Util;
import com.smartsheet.api.models.BulkItemResult;
import com.smartsheet.api.models.CopyOrMoveRowResult;
import com.smartsheet.api.models.EventResult;
import com.smartsheet.api.models.Hyperlink;
import com.smartsheet.api.models.IdentifiableModel;
import com.smartsheet.api.models.IdentifiableModelMixin;
import com.smartsheet.api.models.ObjectValue;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PrimitiveObjectValue;
import com.smartsheet.api.models.Recipient;
import com.smartsheet.api.models.Result;
import com.smartsheet.api.models.WidgetContent;
import com.smartsheet.api.models.format.Format;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * This is the Jackson based JsonSerializer implementation.
 * <p>
 * Thread Safety: This class is thread safe because it is immutable and the underlying Jackson ObjectMapper is thread
 * safe as long as it is not re-configured.
 */
public class JacksonJsonSerializer implements JsonSerializer {
    /**
     * Represents the ObjectMapper used to serialize/de-serialize JSON.
     * <p>
     * It will be initialized in a static initializer and will not change afterwards.
     * <p>
     * Because ObjectMapper is thread-safe as long as it's not reconfigured, a static final class-level ObjectMapper is
     * used to achieve best performance.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // Allow deserialization if there are properties that can't be deserialized
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        // Only include non-null properties in when serializing java beans
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);

        // Use toString() method on enums to serialize and deserialize
        OBJECT_MAPPER.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        OBJECT_MAPPER.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        OBJECT_MAPPER.setDateFormat(df);

        // Add a custom deserializer that will convert a string to a Format object.
        SimpleModule module = new SimpleModule("FormatDeserializerModule", Version.unknownVersion());
        module.addDeserializer(Format.class, new FormatDeserializer());

        // Add custom mixin to ignore getId() for the IdentifiableModel class
        module.setMixInAnnotation(IdentifiableModel.class, IdentifiableModelMixin.class);
        OBJECT_MAPPER.registerModule(module);

        module = new SimpleModule("ObjectValueDeserializerModule", Version.unknownVersion());
        module.addDeserializer(ObjectValue.class, new ObjectValueDeserializer());
        OBJECT_MAPPER.registerModule(module);

        module = new SimpleModule("PrimitiveObjectValueSerializerModule", Version.unknownVersion());
        module.addSerializer(PrimitiveObjectValue.class, new PrimitiveObjectValueSerializer());
        OBJECT_MAPPER.registerModule(module);

        module = new SimpleModule("RecipientDeserializerModule", Version.unknownVersion());
        module.addDeserializer(Recipient.class, new RecipientDeserializer());
        OBJECT_MAPPER.registerModule(module);

        module = new SimpleModule("WidgetContentDeserializerModule", Version.unknownVersion());
        module.addDeserializer(WidgetContent.class, new WidgetContentDeserializer());
        OBJECT_MAPPER.registerModule(module);

        module = new SimpleModule("HyperlinkSerializerModule", Version.unknownVersion());
        module.addSerializer(Hyperlink.class, new HyperlinkSerializer());
        OBJECT_MAPPER.registerModule(module);

        module = new SimpleModule("CellSerializerModule", Version.unknownVersion());
        module.setSerializerModifier(new CellSerializerModifier());
        OBJECT_MAPPER.registerModule(module);

        module = new SimpleModule("ErrorDetailDeserializerModule", Version.unknownVersion());
        module.addDeserializer(com.smartsheet.api.models.Error.class, new ErrorDeserializer());
        OBJECT_MAPPER.registerModule(module);
    }

    /**
     * Sets if the OBJECT MAPPER should ignore unknown properties or fail when de-serializing the JSON data.
     *
     * @param value true if it should fail, false otherwise.
     */
    public static void setFailOnUnknownProperties(boolean value) {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, value);
    }

    /**
     * Constructor.
     * <p>
     * Parameters: None
     * <p>
     * Exceptions: None
     */
    public JacksonJsonSerializer() {
    }

    /**
     * Serialize an object to JSON.
     * <p>
     * Parameters:
     * object : the object to serialize
     * outputStream : the output stream to which the JSON will be written
     * <p>
     * Returns: None
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param outputStream the output stream to write the deserialized object to
     * @param object       object to serialize
     * @throws JSONSerializerException thrown for any serialization exception we catch
     */
    // @Override
    public <T> void serialize(T object, java.io.OutputStream outputStream) throws JSONSerializerException {
        Util.throwIfNull(object, outputStream);

        try {
            OBJECT_MAPPER.writeValue(outputStream, object);
        } catch (JsonGenerationException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }
    }

    /**
     * Serialize an object to JSON.
     * <p>
     * Parameters:
     * object : the object to serialize
     * outputStream : the output stream to which the JSON will be written
     * <p>
     * Returns: None
     * <p>
     * Exceptions: - IllegalArgumentException : if any argument is null - JSONSerializerException : if there is any
     * other error occurred during the operation
     *
     * @param object the object to serialized
     * @return a string with the deserialized object
     * @throws JSONSerializerException thrown for any serialization exception we catch
     */
    public <T> String serialize(T object) throws JSONSerializerException {
        Util.throwIfNull(object);
        String value;

        try {
            value = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }
        return value;
    }

    /**
     * De-serialize an object from JSON.
     * <p>
     * Returns: the de-serialized object
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is null
     * - JSONSerializerException : if there is any other error occurred during the operation
     *
     * @param inputStream the input stream from which the JSON will be read
     * @param objectClass the class of the object to de-serialize
     */
    // @Override
    public <T> T deserialize(Class<T> objectClass, java.io.InputStream inputStream) throws IOException {
        Util.throwIfNull(objectClass, inputStream);

        return OBJECT_MAPPER.readValue(inputStream, objectClass);
    }

    /**
     * De-serialize an object list from JSON.
     * <p>
     * Returns: the de-serialized list
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is null
     * - JSONSerializerException : if there is any other error occurred during the operation
     *
     * @param inputStream the input stream from which the JSON will be read
     * @param objectClass the class of the object (of the list) to de-serialize
     */
    // @Override
    public <T> List<T> deserializeList(Class<T> objectClass, java.io.InputStream inputStream)
            throws JSONSerializerException {
        Util.throwIfNull(objectClass, inputStream);

        List<T> list = null;

        try {
            // Read the json input stream into a List.
            list = OBJECT_MAPPER.readValue(inputStream,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, objectClass));
        } catch (JsonParseException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }

        return list;
    }

    /**
     * De-serialize to a PagedResult (holds pagination info) from JSON
     */
    @Override
    public <T> PagedResult<T> deserializeDataWrapper(
            Class<T> objectClass,
            java.io.InputStream inputStream
    ) throws JSONSerializerException {
        Util.throwIfNull(objectClass, inputStream);

        PagedResult<T> rw = null;

        try {
            // Read the json input stream into a List.
            rw = OBJECT_MAPPER.readValue(
                    inputStream,
                    OBJECT_MAPPER
                            .getTypeFactory()
                            .constructParametrizedType(PagedResult.class, PagedResult.class, objectClass)
            );
        } catch (JsonParseException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }

        return rw;
    }

    /**
     * De-serialize to a map from JSON.
     */
    // @Override
    public Map<String, Object> deserializeMap(InputStream inputStream) throws JSONSerializerException {
        Util.throwIfNull(inputStream);

        Map<String, Object> map = null;

        try {
            map = OBJECT_MAPPER.readValue(inputStream, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonParseException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }

        return map;
    }

    /**
     * De-serialize a Result object from JSON.
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is null
     * - JSONSerializerException : if there is any other error occurred during the operation
     *
     * @param inputStream the input stream from which the JSON will be read
     * @param objectClass the class of the object (of the Result) to de-serialize
     * @return the de-serialized result
     */
    // @Override
    public <T> Result<T> deserializeResult(Class<T> objectClass, java.io.InputStream inputStream)
            throws JSONSerializerException {
        Util.throwIfNull(objectClass, inputStream);

        Result<T> result = null;

        try {
            result = OBJECT_MAPPER.readValue(inputStream,
                    OBJECT_MAPPER.getTypeFactory().constructParametrizedType(Result.class, Result.class, objectClass));
        } catch (JsonParseException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }

        return result;
    }

    /**
     * De-serialize a List Result object from JSON.
     * <p>
     * Parameters: - objectClass :  - inputStream :
     * <p>
     * Returns: the de-serialized result
     * <p>
     * Exceptions:
     * - IllegalArgumentException : if any argument is null
     * - JSONSerializerException : if there is any other error occurred during the operation
     *
     * @param inputStream the input stream from which the JSON will be read
     * @param objectClass the class of the object (of the Result) to de-serialize
     */
    // @Override
    public <T> Result<List<T>> deserializeListResult(Class<T> objectClass, java.io.InputStream inputStream)
            throws JSONSerializerException {
        Util.throwIfNull(objectClass, inputStream);

        Result<List<T>> result = null;

        try {
            result = OBJECT_MAPPER.readValue(
                    inputStream,
                    OBJECT_MAPPER.getTypeFactory().constructParametrizedType(Result.class, Result.class,
                            OBJECT_MAPPER.getTypeFactory().constructParametrizedType(List.class, List.class, objectClass)));
        } catch (JsonParseException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }
        return result;
    }

    @Override
    public <T> BulkItemResult<T> deserializeBulkItemResult(Class<T> objectClass, InputStream inputStream)
            throws JSONSerializerException {
        BulkItemResult<T> result = null;
        try {
            result = OBJECT_MAPPER.readValue(inputStream,
                    OBJECT_MAPPER.getTypeFactory().constructParametrizedType(BulkItemResult.class, BulkItemResult.class, objectClass));
        } catch (JsonParseException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }
        return result;
    }

    /**
     * De-serialize to a CopyOrMoveRowResult object from JSON
     */
    @Override
    public CopyOrMoveRowResult deserializeCopyOrMoveRow(
            java.io.InputStream inputStream
    ) throws JSONSerializerException {
        Util.throwIfNull(inputStream);

        CopyOrMoveRowResult rw = null;

        try {
            // Read the json input stream into a List.
            rw = OBJECT_MAPPER.readValue(inputStream, CopyOrMoveRowResult.class);
        } catch (JsonParseException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }

        return rw;
    }

    /**
     * De-serialize to a EventResult (holds pagination info) from JSON
     */
    @Override
    public EventResult deserializeEventResult(java.io.InputStream inputStream) throws JSONSerializerException {
        Util.throwIfNull(inputStream);

        EventResult rw = null;

        try {
            // Read the json input stream into a List.
            rw = OBJECT_MAPPER.readValue(inputStream, EventResult.class);
        } catch (JsonParseException e) {
            throw new JSONSerializerException(e);
        } catch (JsonMappingException e) {
            throw new JSONSerializerException(e);
        } catch (IOException e) {
            throw new JSONSerializerException(e);
        }

        return rw;
    }
}
