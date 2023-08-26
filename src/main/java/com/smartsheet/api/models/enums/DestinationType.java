package com.smartsheet.api.models.enums;

/*
 * Smartsheet SDK for Java
 * Copyright (C) 2023 Smartsheet
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

/**
 * Represents the type of the destination container when a Sheet or Folder is moved, or when a Sheet, Folder, or Workspace is copied..
 */
public enum DestinationType {
    /** Represents the home destination container. */
    HOME,

    /** Represents the workspace destination container. */
    WORKSPACE,

    /** Represents the folder destination container. */
    FOLDER,
}
