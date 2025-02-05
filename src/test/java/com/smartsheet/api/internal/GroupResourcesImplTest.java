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

package com.smartsheet.api.internal;

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Group;
import com.smartsheet.api.models.Group.CreateGroupBuilder;
import com.smartsheet.api.models.Group.UpdateGroupBuilder;
import com.smartsheet.api.models.GroupMember;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class GroupResourcesImplTest extends ResourcesImplBase {

    private GroupResourcesImpl groupResources;

    @BeforeEach
    public void setUp() throws Exception {
        groupResources = new GroupResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testGetGroups() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/listGroups.json"));

        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Group> groups = groupResources.listGroups(parameters);

        assertThat(groups.getData().get(0).getId()).isNotNull();
        assertThat(groups.getData().get(0).getName()).isNotNull();
        assertThat(groups.getData().get(0).getOwner()).isNotNull();
        assertThat(groups.getData().get(0).getOwnerId()).isNotNull();
        assertThat(groups.getData().get(0).getCreatedAt()).isNotNull();
        assertThat(groups.getData().get(0).getModifiedAt()).isNotNull();
        assertThat(groups.getData().get(0).getDescription()).isNotNull();
    }

    @Test
    void testGetGroupById() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getGroup.json"));

        Group group = groupResources.getGroup(123L);
        assertThat(group.getId()).isNotNull();
        assertThat(group.getName()).isNotNull();
        assertThat(group.getOwner()).isNotNull();
        assertThat(group.getOwnerId()).isNotNull();
        assertThat(group.getCreatedAt()).isNotNull();
        assertThat(group.getModifiedAt()).isNotNull();
        assertThat(group.getDescription()).isNotNull();
        assertThat(group.getId()).isNotNull();

        for (GroupMember member : group.getMembers()) {
            assertThat(member.getFirstName()).isNotNull();
            assertThat(member.getLastName()).isNotNull();
            assertThat(member.getId()).isNotNull();
            assertThat(member.getEmail()).isNotNull();
        }
    }

    @Test
    void testCreateGroup() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/createGroup.json"));

        CreateGroupBuilder builder = new CreateGroupBuilder();
        builder.setName("My Test Group")
                .setDescription("My awesome group")
                .setMembers(new ArrayList<>());

        builder.getMembers().add(new GroupMember.AddGroupMemberBuilder().setEmail("test@test.com").build());
        builder.getMembers().add(new GroupMember.AddGroupMemberBuilder().setEmail("test2@test.com").build());
        builder.getMembers().add(new GroupMember.AddGroupMemberBuilder().setEmail("test3@test.com").build());

        Group group = groupResources.createGroup(builder.build());
        assertThat(group.getId()).isNotNull();
        assertThat(group.getName()).isNotNull();
        assertThat(group.getOwner()).isNotNull();
        assertThat(group.getOwnerId()).isNotNull();
        assertThat(group.getCreatedAt()).isNotNull();
        assertThat(group.getModifiedAt()).isNotNull();
        assertThat(group.getDescription()).isNotNull();
        assertThat(group.getId()).isNotNull();

        for (GroupMember member : group.getMembers()) {
            assertThat(member.getFirstName()).isNotNull();
            assertThat(member.getLastName()).isNotNull();
            assertThat(member.getId()).isNotNull();
            assertThat(member.getEmail()).isNotNull();
        }
    }

    @Test
    void testUpdateGroup() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateGroup.json"));

        UpdateGroupBuilder builder = new UpdateGroupBuilder();
        builder.setName("My Test Group - renamed ")
                .setDescription("My awesome group- redecribed")
                .setId(123L);

        Group group = groupResources.updateGroup(builder.build());
        assertThat(group.getId()).isNotNull();
        assertThat(group.getName()).isNotNull();
        assertThat(group.getOwner()).isNotNull();
        assertThat(group.getOwnerId()).isNotNull();
        assertThat(group.getCreatedAt()).isNotNull();
        assertThat(group.getModifiedAt()).isNotNull();
        assertThat(group.getDescription()).isNotNull();
        assertThat(group.getId()).isNotNull();
    }

    @Test
    void testDeleteGroup() throws IOException {
        server.setResponseBody(new File("src/test/resources/deleteGroup.json"));
        assertThatCode(() -> groupResources.deleteGroup(1234L)).doesNotThrowAnyException();
    }

    @Test
    void testAddMembersToGroup() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/addGroupMembers.json"));
        List<GroupMember> newMembers = new ArrayList<>();
        newMembers.add(new GroupMember.AddGroupMemberBuilder().setEmail("test3@test.com").build());
        newMembers.add(new GroupMember.AddGroupMemberBuilder().setEmail("test4@test.com").build());
        List<GroupMember> addedMembers = groupResources.memberResources().addGroupMembers(1234L, newMembers);
        assertThat(addedMembers).isNotEmpty();

        for (GroupMember member : addedMembers) {
            assertThat(member.getEmail()).isNotNull();
            assertThat(member.getId()).isNotNull();
        }
    }

    @Test
    void testRemoveMemberFromGroup() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteMemberFromGroup.json"));
        groupResources.memberResources().deleteGroupMember(1234L, 1234L);
    }
}
