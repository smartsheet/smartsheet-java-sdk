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

import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.SystemColumnType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReportColumnTest {

    @Test
    void testAddReportColumnBuilder() {
        AutoNumberFormat autoNumberFormat = new AutoNumberFormat();
        autoNumberFormat.setPrefix("TASK-");

        ReportColumn column = new ReportColumn.AddReportColumnBuilder()
                .setTitle("Sheet name")
                .setType(ColumnType.TEXT_NUMBER)
                .setIndex(5)
                .setSheetNameColumn(true)
                .setOptions(List.of("a", "b"))
                .setSymbol(null)
                .setSystemColumnType(SystemColumnType.AUTO_NUMBER)
                .setAutoNumberFormat(autoNumberFormat)
                .setWidth(150)
                .setPrimary(false)
                .setFormat(null)
                .setValidation(false)
                .build();

        assertThat(column.getTitle()).isEqualTo("Sheet name");
        assertThat(column.getType()).isEqualTo(ColumnType.TEXT_NUMBER);
        assertThat(column.getIndex()).isEqualTo(5);
        assertThat(column.getSheetNameColumn()).isTrue();
        assertThat(column.getOptions()).containsExactly("a", "b");
        assertThat(column.getSystemColumnType()).isEqualTo(SystemColumnType.AUTO_NUMBER);
        assertThat(column.getAutoNumberFormat()).isEqualTo(autoNumberFormat);
        assertThat(column.getWidth()).isEqualTo(150);
        assertThat(column.getPrimary()).isFalse();
        assertThat(column.getValidation()).isFalse();
    }

    @Test
    void testAddReportColumnBuilderDefaultsSheetNameColumnFalse() {
        ReportColumn column = new ReportColumn.AddReportColumnBuilder()
                .setTitle("Item selected")
                .setType(ColumnType.CHECKBOX)
                .build();

        assertThat(column.getSheetNameColumn()).isFalse();
    }

    @Test
    void testAddReportColumnBuilderThrowsWhenTitleMissing() {
        assertThrows(InstantiationError.class, () ->
                new ReportColumn.AddReportColumnBuilder()
                        .setType(ColumnType.CHECKBOX)
                        .build());
    }

    @Test
    void testAddReportColumnBuilderThrowsWhenTypeMissing() {
        assertThrows(InstantiationError.class, () ->
                new ReportColumn.AddReportColumnBuilder()
                        .setTitle("Item selected")
                        .build());
    }
}
