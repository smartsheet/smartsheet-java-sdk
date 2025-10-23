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

package com.smartsheet.api.integrationtest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.Account;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.User;
import com.smartsheet.api.models.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;
    User user;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testGetCurrentUser() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getCurrentUser();
        Account account = user.getAccount();
        assertThat(user).isNotNull();
    }

    @Test
    void testGetUser() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getUser(smartsheet.userResources().getCurrentUser().getId());
        assertThat(user).isNotNull();
    }

    @Test
    void testListUsers() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();

        PagedResult<User> userWrapper = smartsheet
                .userResources()
                .listUsers(new HashSet(Arrays.asList("aditi.nioding@gmail.com")), parameters);
        List<User> users = userWrapper.getData();

        //assertTrue(users.size() > 0);
    }

    @Test
    void testAddProfileImage() throws SmartsheetException, IOException {
        UserProfile me = smartsheet.userResources().getCurrentUser();
        assertThat(me).isNotNull();
        smartsheet.userResources().addProfileImage(me.getId(), "src/test/resources/exclam.png", "image/png");
        me = smartsheet.userResources().getCurrentUser();
        assertThat(me.getProfileImage()).isNotNull();
        final Long squareProfileImageSize = 1050L;
        assertThat(me.getProfileImage().getWidth()).isEqualTo(squareProfileImageSize);
        assertThat(me.getProfileImage().getHeight()).isEqualTo(squareProfileImageSize);
    }

    @Test
    //not executed in test due to low permission
    public void testAddUser() throws IOException, SmartsheetException {
        // User user = new User.AddUserBuilder()
        //     .setAdmin(false)
        //     .setEmail("aditi.nioding@gmail.com")
        //     .setFirstName("Aditi").setLastName("N")
        //     .setLicensedSheetCreator(true)
        //     .build();
        // User newUser = smartsheet.userResources().addUser(user);
        // String name = newUser.getFirstName();
        // assertTrue(name.equals("Aditi"));
        // testUpdateUser(newUser.getId());
    }

    //not executed in test due to low permission
    public void testUpdateUser(long userId) throws SmartsheetException, IOException {
        // User user = new User.UpdateUserBuilder()
        //     .setAdmin(true)
        //     .setUserId(userId)
        //     .setFirstName("Adi")
        //     .setLicensedSheetCreator(true)
        //     .build();
        // User updatedUser = smartsheet.userResources().updateUser(user);
        // assertThat(updatedUser).isNotNull();
    }

    @Test
    void testListOrgSheets() throws SmartsheetException, IOException {
        //PagedResult<Sheet> sheets = smartsheet.userResources().listOrgSheets();
        //not executed in test due to low permission
        //assertThat(sheets).isNotNull();
    }

    @Test
    //not executed in test due to low permission
    public void testDeleteUser() throws IOException, SmartsheetException {
        // User user = new User.AddUserBuilder()
        //     .setAdmin(false).setEmail("test@test.com")
        //     .setFirstName("Aditi")
        //     .setLastName("N")
        //     .setLicensedSheetCreator(true)
        //     .build();
        // User newUser = smartsheet.userResources().addUser(user);
        // Long toId = newUser.getId();
        //
        // DeleteUserParameters parameters = new DeleteUserParameters(toId, true, true);
    }
}
