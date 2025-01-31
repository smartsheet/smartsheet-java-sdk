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
import com.smartsheet.api.models.Group;
import com.smartsheet.api.models.GroupMember;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GroupResourcesIT extends ITResourcesImpl {
    static final String GROUP_NAME = "Test Group";

    Smartsheet smartsheet;
    Long groupId;
    long groupMemberId;
    PagedResult<Group> groups;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testGroupMethods() throws SmartsheetException, IOException {
        removeExistingGroup();
        testCreateGroup();
        testListGroups();
        testGetGroupById();
        // This test was consistently failing because a group with the same name already exists
        // testUpdateGroup();
        testAddMembersToGroup();
        testRemoveMemberFromGroup();
        testDeleteGroup();
    }

    public void testCreateGroup() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getCurrentUser();

        if (user.getGroupAdmin()) {
            GroupMember member = new GroupMember.AddGroupMemberBuilder().setEmail("aditi.nioding@gmail.com").build();

            Group group = new Group.CreateGroupBuilder()
                    .setName(GROUP_NAME)
                    .setDescription("Test group")
                    .setMembers(Arrays.asList(member))
                    .build();

            group = smartsheet.groupResources().createGroup(group);
            assertThat(group.getId()).isNotNull();
        }
    }

    private void removeExistingGroup() throws SmartsheetException, IOException {
        // Make sure group doesn't exist before trying to create it
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        groups = smartsheet.groupResources().listGroups(parameters);

        for (Group group : groups.getData()) {
            if (group.getName().equals(GROUP_NAME)) {
                groupId = group.getId();
                break;
            }
        }

        if (groupId != null) {
            // Remove group if exists
            testDeleteGroup();
        }
    }

    public void testListGroups() throws SmartsheetException, IOException {

        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        groups = smartsheet.groupResources().listGroups(parameters);

        assertThat(groups).isNotNull();

        for (int i = 0; i < groups.getData().size(); i++) {
            if (groups.getData().get(i).getName().equals(GROUP_NAME)) {
                groupId = groups.getData().get(i).getId();
                break;
            }
        }
    }

    public void testGetGroupById() throws SmartsheetException, IOException {

        Group group = smartsheet.groupResources().getGroup(groupId);
        assertThat(group).isNotNull();
    }

    //not executed in test due to permission issue
    public void testUpdateGroup() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getCurrentUser();

        if (user.getGroupAdmin()) {
            Group groupUpdated = new Group.UpdateGroupBuilder()
                    .setName("Renamed Group")
                    .setId(groupId)
                    .setDescription("Some description")
                    .build();
            assertThat(smartsheet.groupResources().updateGroup(groupUpdated)).isNotNull();
        }
    }

    //not executed in test due to permission issue
    public void testDeleteGroup() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getCurrentUser();

        if (user.getGroupAdmin()) {
            smartsheet.groupResources().deleteGroup(groupId);
        }
    }

    //not executed in test due to permission issue
    public void testAddMembersToGroup() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getCurrentUser();

        if (user.getGroupAdmin()) {
            GroupMember member = new GroupMember.AddGroupMemberBuilder().setEmail("jane.doe@smartsheet.com").build();

            List<GroupMember> addedMembers = smartsheet.groupResources().memberResources().addGroupMembers(groupId, Arrays.asList(member));
            assertThat(addedMembers).isNotEmpty();
            groupMemberId = addedMembers.get(0).getId();
        }
    }

    //not executed in test due to permission issue
    public void testRemoveMemberFromGroup() throws SmartsheetException, IOException {
        UserProfile user = smartsheet.userResources().getCurrentUser();

        if (user.getGroupAdmin()) {
            smartsheet.groupResources().memberResources().deleteGroupMember(groupId, groupMemberId);
        }
    }
}
