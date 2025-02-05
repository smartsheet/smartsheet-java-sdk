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

package com.smartsheet.api.models.enums;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EventActionTest {

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValueOfTests {
        @ParameterizedTest
        @MethodSource("valuesArguments")
        void valueOf(EventAction expectedEventAction, String value) {
            // Act
            EventAction result = EventAction.valueOf(value);

            // Assert
            assertThat(result).isEqualTo(expectedEventAction);

            // This will cause the test to fail if we ever add a new value.
            // Please remember to add the new value in the method below
            assertThat(EventAction.values()).hasSize(55);
        }

        private Stream<Arguments> valuesArguments() {
            return Stream.of(
                    Arguments.of(EventAction.CREATE, "CREATE"),
                    Arguments.of(EventAction.UPDATE, "UPDATE"),
                    Arguments.of(EventAction.LOAD, "LOAD"),
                    Arguments.of(EventAction.DELETE, "DELETE"),
                    Arguments.of(EventAction.PURGE, "PURGE"),
                    Arguments.of(EventAction.RESTORE, "RESTORE"),
                    Arguments.of(EventAction.RENAME, "RENAME"),
                    Arguments.of(EventAction.ACTIVATE, "ACTIVATE"),
                    Arguments.of(EventAction.DEACTIVATE, "DEACTIVATE"),
                    Arguments.of(EventAction.EXPORT, "EXPORT"),
                    Arguments.of(EventAction.MOVE, "MOVE"),
                    Arguments.of(EventAction.MOVE_ROW, "MOVE_ROW"),
                    Arguments.of(EventAction.COPY_ROW, "COPY_ROW"),
                    Arguments.of(EventAction.SAVE_AS_NEW, "SAVE_AS_NEW"),
                    Arguments.of(EventAction.SAVE_AS_TEMPLATE, "SAVE_AS_TEMPLATE"),
                    Arguments.of(EventAction.ADD_PUBLISH, "ADD_PUBLISH"),
                    Arguments.of(EventAction.REMOVE_PUBLISH, "REMOVE_PUBLISH"),
                    Arguments.of(EventAction.ADD_SHARE, "ADD_SHARE"),
                    Arguments.of(EventAction.REMOVE_SHARE, "REMOVE_SHARE"),
                    Arguments.of(EventAction.ADD_SHARE_MEMBER, "ADD_SHARE_MEMBER"),
                    Arguments.of(EventAction.REMOVE_SHARE_MEMBER, "REMOVE_SHARE_MEMBER"),
                    Arguments.of(EventAction.ADD_WORKSPACE_SHARE, "ADD_WORKSPACE_SHARE"),
                    Arguments.of(EventAction.REMOVE_WORKSPACE_SHARE, "REMOVE_WORKSPACE_SHARE"),
                    Arguments.of(EventAction.ADD_MEMBER, "ADD_MEMBER"),
                    Arguments.of(EventAction.REMOVE_MEMBER, "REMOVE_MEMBER"),
                    Arguments.of(EventAction.TRANSFER_OWNERSHIP, "TRANSFER_OWNERSHIP"),
                    Arguments.of(EventAction.CREATE_CELL_LINK, "CREATE_CELL_LINK"),
                    Arguments.of(EventAction.REMOVE_SHARES, "REMOVE_SHARES"),
                    Arguments.of(EventAction.TRANSFER_OWNED_GROUPS, "TRANSFER_OWNED_GROUPS"),
                    Arguments.of(EventAction.TRANSFER_OWNED_ITEMS, "TRANSFER_OWNED_ITEMS"),
                    Arguments.of(EventAction.DOWNLOAD_SHEET_ACCESS_REPORT, "DOWNLOAD_SHEET_ACCESS_REPORT"),
                    Arguments.of(EventAction.DOWNLOAD_USER_LIST, "DOWNLOAD_USER_LIST"),
                    Arguments.of(EventAction.DOWNLOAD_LOGIN_HISTORY, "DOWNLOAD_LOGIN_HISTORY"),
                    Arguments.of(EventAction.DOWNLOAD_PUBLISHED_ITEMS_REPORT, "DOWNLOAD_PUBLISHED_ITEMS_REPORT"),
                    Arguments.of(EventAction.UPDATE_MAIN_CONTACT, "UPDATE_MAIN_CONTACT"),
                    Arguments.of(EventAction.IMPORT_USERS, "IMPORT_USERS"),
                    Arguments.of(EventAction.BULK_UPDATE, "BULK_UPDATE"),
                    Arguments.of(EventAction.LIST_SHEETS, "LIST_SHEETS"),
                    Arguments.of(EventAction.REQUEST_BACKUP, "REQUEST_BACKUP"),
                    Arguments.of(EventAction.CREATE_RECURRING_BACKUP, "CREATE_RECURRING_BACKUP"),
                    Arguments.of(EventAction.UPDATE_RECURRING_BACKUP, "UPDATE_RECURRING_BACKUP"),
                    Arguments.of(EventAction.DELETE_RECURRING_BACKUP, "DELETE_RECURRING_BACKUP"),
                    Arguments.of(EventAction.REMOVE_FROM_GROUPS, "REMOVE_FROM_GROUPS"),
                    Arguments.of(EventAction.SEND_AS_ATTACHMENT, "SEND_AS_ATTACHMENT"),
                    Arguments.of(EventAction.SEND_ROW, "SEND_ROW"),
                    Arguments.of(EventAction.SEND, "SEND"),
                    Arguments.of(EventAction.SEND_COMMENT, "SEND_COMMENT"),
                    Arguments.of(EventAction.SEND_INVITE, "SEND_INVITE"),
                    Arguments.of(EventAction.DECLINE_INVITE, "DECLINE_INVITE"),
                    Arguments.of(EventAction.ACCEPT_INVITE, "ACCEPT_INVITE"),
                    Arguments.of(EventAction.SEND_PASSWORD_RESET, "SEND_PASSWORD_RESET"),
                    Arguments.of(EventAction.REMOVE_FROM_ACCOUNT, "REMOVE_FROM_ACCOUNT"),
                    Arguments.of(EventAction.ADD_TO_ACCOUNT, "ADD_TO_ACCOUNT"),
                    Arguments.of(EventAction.AUTHORIZE, "AUTHORIZE"),
                    Arguments.of(EventAction.REVOKE, "REVOKE")
            );
        }
    }
}
