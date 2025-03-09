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

package com.smartsheet.api.models;

import com.smartsheet.api.models.enums.DestinationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@ToString
@Jacksonized
@Builder
public class ContainerDestination {

    /**
     * Represents the destination type when a Sheet or Folder is moved, or when a Sheet, Folder, or Workspace is copied..
     */
    private DestinationType destinationType;

    /**
     * Represents the destination id when a Sheet or Folder is moved, or when a Sheet, Folder, or Workspace is copied..
     */
    private Long destinationId;

    /**
     * Represents the new name when a Sheet or Folder is moved, or when a Sheet, Folder, or Workspace is copied..
     */
    private String newName;
}
