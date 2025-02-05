/*
 * Copyright (C) 2025 Smartsheet
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.Result;
import com.smartsheet.api.models.User;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JacksonJsonSerializerTest {
    JacksonJsonSerializer jjs = new JacksonJsonSerializer();

    @Test
    void testSerialize() throws JSONSerializerException, IOException {
        // Illegal Argument due to null
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThatThrownBy(() -> jjs.serialize(null, outputStream))
                .isInstanceOf(IllegalArgumentException.class);

        // Illegal Argument due to null
        Object emptyObject = new Object();
        assertThatThrownBy(() -> jjs.serialize(emptyObject, null))
                .isInstanceOf(IllegalArgumentException.class);

        // Illegal Argument due to null
        assertThatThrownBy(() -> jjs.serialize(null, null))
                .isInstanceOf(IllegalArgumentException.class);

        // Mapping Exception. Can't serialize to an object and can't create an empty bean serializer
        assertThatThrownBy(() -> jjs.serialize(emptyObject, outputStream))
                .isInstanceOf(JSONSerializerException.class);

        // Test successful serialization
        User user = new User();
        user.setEmail("test@test.com");
        assertThatNoException().isThrownBy(() -> jjs.serialize(user, outputStream));

        // Test id field is ignored.
        User user1 = new User();
        user1.setId(123L);
        user1.setEmail("test@test.com");
        String serializedString = jjs.serialize(user1);
        // The id field should not be serialized. Instead the id is used in the url and not sent as part of the body.
        assertThat(serializedString).doesNotContain("id");

        // Test IOException
        File tempFile = null;
        tempFile = File.createTempFile("json_test", ".tmp");
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.close();
        assertThatThrownBy(() -> jjs.serialize(user, fos))
                .isInstanceOf(JSONSerializerException.class);
    }

    @Test
    void testDeserialize() throws JSONSerializerException, JsonParseException, JsonMappingException, IOException {
        // Illegal argument due to null
        assertThatThrownBy(() -> jjs.deserialize(null, null))
                .isInstanceOf(IllegalArgumentException.class);

        // Illegal argument due to null
        assertThatThrownBy(() -> jjs.deserialize(User.class, null))
                .isInstanceOf(IllegalArgumentException.class);

        // Illegal argument due to null
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        assertThatThrownBy(() -> jjs.deserialize(null, inputStream))
                .isInstanceOf(IllegalArgumentException.class);

        // Test Successful deserialize of a serialized user back to a User Object

        // Serialize User
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        User originalUser = new User();
        originalUser.setFirstName("Test");
        originalUser.setId(123L);
        jjs.serialize(originalUser, b);

        // Deserialize User from a byte array
        User user = jjs.deserialize(User.class, new ByteArrayInputStream(b.toByteArray()));

        assertThat(user.getFirstName()).isEqualTo(originalUser.getFirstName());
        assertThat(user.getId()).isNotEqualTo(originalUser.getId());
    }

    @Test
    void testDeserializeMap() throws JSONSerializerException, FileNotFoundException, IOException {
        // Test null pointer exceptions
        assertThatThrownBy(() -> jjs.deserializeMap(null))
                .isInstanceOf(IllegalArgumentException.class);

        // Parse Exception / invalid json
        String str = "test";
        assertThatThrownBy(() -> jjs.deserializeMap(new ByteArrayInputStream(str.getBytes())))
                .isInstanceOf(JSONSerializerException.class);

        // Mapping Exception. Can't deserialize a JSON array to a Map object as the key would be an int
        String arrayStr = "[\"test\",\"test1\"]";
        assertThatThrownBy(() -> jjs.deserializeMap(new ByteArrayInputStream(arrayStr.getBytes())))
                .isInstanceOf(JSONSerializerException.class);

        // IO Exception
        FileInputStream fis = new FileInputStream(File.createTempFile("json_test", ".tmp"));
        fis.close();
        assertThatThrownBy(() -> jjs.deserializeMap(fis))
                .isInstanceOf(JSONSerializerException.class);

        // Valid deserialize
        String jsonMapString = "{'key':'value'},{'key':'value'}".replace("'", "\"");
        jjs.deserializeMap(new ByteArrayInputStream(jsonMapString.getBytes()));
    }

    @Test
    void testDeserializeList() throws JsonParseException, IOException, JSONSerializerException {
        // Test null pointer exceptions
        assertThatThrownBy(() -> jjs.deserializeList(null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> jjs.deserializeList(ArrayList.class, null))
                .isInstanceOf(IllegalArgumentException.class);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        assertThatThrownBy(() -> jjs.deserializeList(null, inputStream))
                .isInstanceOf(IllegalArgumentException.class);

        // Test JsonParseException. Can't convert an invalid json array to a list.
        ByteArrayInputStream inputStreamBrokenJson = new ByteArrayInputStream("[broken jason".getBytes());
        assertThatThrownBy(() -> jjs.deserializeList(List.class, inputStreamBrokenJson))
                .isInstanceOf(JSONSerializerException.class);

        // Serialize a User and fail since it is not an ArrayList
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        User originalUser = new User();
        // b has the user in json format in a byte array
        jjs.serialize(originalUser, b);

        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(b.toByteArray());
        assertThatThrownBy(() -> jjs.deserializeList(ArrayList.class, inputStream1))
                .isInstanceOf(JSONSerializerException.class);

        // Test serializing/deserializing a simple ArrayList
        jjs = new JacksonJsonSerializer();
        List<String> originalList = new ArrayList<>();
        originalList.add("something");
        originalList.add("something-else");
        b = new ByteArrayOutputStream();
        jjs.serialize(originalList, b);
        List<String> newList = jjs.deserializeList(String.class, new ByteArrayInputStream(b.toByteArray()));
        // Verify that the serialized/deserialized object is equal to the original object.
        assertThat(newList).isEqualTo(originalList);

        // Test JSONSerializerException

        // Test IOException
        FileInputStream fis = new FileInputStream(File.createTempFile("json_test", ".tmp"));
        fis.close();
        assertThatThrownBy(() -> jjs.deserializeList(List.class, fis))
                .isInstanceOf(JSONSerializerException.class);
    }

    @Test
    void testDeserializeResult() throws JSONSerializerException, IOException {
        assertThatThrownBy(() -> jjs.deserializeResult(null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> jjs.deserializeResult(User.class, null))
                .isInstanceOf(IllegalArgumentException.class);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        assertThatThrownBy(() -> jjs.deserializeResult(null, inputStream))
                .isInstanceOf(IllegalArgumentException.class);

        Result<Folder> result = new Result<>();
        result.setMessage("Test Result");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Test successful deserialization
        jjs.serialize(result, outputStream);
        jjs.deserializeResult(Result.class, new ByteArrayInputStream(outputStream.toByteArray()));

        // Test JSONMappingException - Test Mapping a list back to one object
        outputStream = new ByteArrayOutputStream();
        ArrayList<User> users = new ArrayList<>();
        jjs.serialize(users, outputStream);
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(outputStream.toByteArray());
        assertThatThrownBy(() -> jjs.deserializeResult(Result.class, inputStream1))
                .isInstanceOf(JSONSerializerException.class);

        // Test IOException
        FileInputStream fis = null;
        fis = new FileInputStream(File.createTempFile("json_test", ".tmp"));
        fis.close();
        FileInputStream finalFis = fis;
        assertThatThrownBy(() -> jjs.deserializeResult(Result.class, finalFis))
                .isInstanceOf(JSONSerializerException.class);

        // Test JsonParseException
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream("{oops it's broken".getBytes());
        assertThatThrownBy(() -> jjs.deserializeResult(Result.class, inputStream2))
                .isInstanceOf(JSONSerializerException.class);
    }

    @Test
    void testDeserializeListResult() throws IOException, JSONSerializerException {
        assertThatThrownBy(() -> jjs.deserializeListResult(null, null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> jjs.deserializeListResult(User.class, null))
                .isInstanceOf(IllegalArgumentException.class);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        assertThatThrownBy(() -> jjs.deserializeListResult(null, inputStream))
                .isInstanceOf(IllegalArgumentException.class);

        Result<ArrayList<Object>> result = new Result<>();
        result.setMessage("Test Message");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Test successful deserialization
        jjs.serialize(result, outputStream);
        jjs.deserializeListResult(Result.class, new ByteArrayInputStream(outputStream.toByteArray()));

        // Test IOException
        FileInputStream fis = null;
        fis = new FileInputStream(File.createTempFile("json_test", ".tmp"));
        fis.close();
        FileInputStream finalFis = fis;
        assertThatThrownBy(() -> jjs.deserializeListResult(Result.class, finalFis))
                .isInstanceOf(JSONSerializerException.class);

        // Test JSONMappingException - Test Mapping a list back to one object
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        ArrayList<User> users = new ArrayList<>();
        jjs.serialize(users, outputStream);
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(outputStream1.toByteArray());
        assertThatThrownBy(() -> jjs.deserializeListResult(Result.class, inputStream1))
                .isInstanceOf(JSONSerializerException.class);

        // Test JsonParseException
        ByteArrayInputStream inputStreamBadJson = new ByteArrayInputStream("{bad json".getBytes());
        assertThatThrownBy(() -> jjs.deserializeListResult(Result.class, inputStreamBadJson))
                .isInstanceOf(JSONSerializerException.class);
    }

}
