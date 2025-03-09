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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// We need to have a constructor with no arguments for the subclasses of this class to work
@NoArgsConstructor
// We need to have a constructor with all arguments for Lombok Builder to work
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Hyperlink {
    /**
     * When the hyperlink is a URL link, this property will contain the URL value.
     * When the hyperlink is a Sheet/Report link (i.e. sheetId or reportId is non-null),
     * this property will contain the permalink to the Sheet or Report.
     */
    private String url;

    /**
     * If non-null, this hyperlink is a link to the Sheet with this ID..
     */
    private Long sheetId;

    /**
     * If non-null, this hyperlink is a link to the Report with this ID.
     */
    private Long reportId;

    /**
     * If non-null, this hyperlink is a link to the Sort with this ID.
     */
    private Long sightId;

    /**
     * Checks if the url, reportId, sightId and sheetId ID in the Hyperlink are all null
     *
     * @return boolean based on whether the fields in the cell link are null or not
     */
    @JsonIgnore
    public boolean isNull() {
        return this.sightId == null &&
                this.url == null &&
                this.reportId == null &&
                this.sheetId == null;
    }
}
