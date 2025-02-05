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

import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import com.smartsheet.api.models.format.Format;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RowTest {

    @Test
    void testGetColumnByIndex() {
        Row row = new Row();
        Column col = new Column();
        col.setId(1234L);
        col.setIndex(2);
        List<Column> columns = new ArrayList<>();
        columns.add(col);
        row.setColumns(columns);
        //row.setParentRowNumber(1);
        row.setDiscussions(new ArrayList<>());
        row.setAttachments(new ArrayList<>());

        assertThat(row.getColumnById(1234L)).isEqualTo(col);
        assertThat(row.getColumnByIndex(2)).isEqualTo(col);
        assertThat(row.getColumnById(12345L)).isNull();
        assertThat(row.getColumnByIndex(22)).isNull();
        assertThat(new Row().getColumnById(213L)).isNull();
        assertThat(new Row().getColumnByIndex(33)).isNull();
        Row row1 = new Row();
        row1.setColumns(new ArrayList<>());
        assertThat(row1.getColumnById(1L)).isNull();
        assertThat(row1.getColumnByIndex(1)).isNull();
    }

    @Test
    void testInsertRowBuilder() {
        Format format = new Format("new format");
        List<Cell> cells = new ArrayList<>();
        Row row = new Row.AddRowBuilder().setToTop(true).setExpanded(false).setFormat(format).setCells(cells).build();

        assertThat(row.getToTop()).isTrue();
        assertThat(row.isExpanded()).isFalse();
        assertThat(row.getFormat()).isNotNull();
        assertThat(row.getCells()).isNotNull();
        assertThat(row.getToBottom()).isNull();
        assertThat(row.getParentId()).isNull();
        assertThat(row.getSiblingId()).isNull();
        assertThat(row.getAbove()).isNull();
    }

    @Test
    void testUpdateRowBuilder() {
        Format format = new Format("new format");
        List<Cell> cells = new ArrayList<>();
        Row row = new Row.UpdateRowBuilder().setToTop(true).setExpanded(false).setFormat(format).setCells(cells).setLocked(true).build();

        assertThat(row.getToTop()).isTrue();
        assertThat(row.isLocked()).isTrue();
        assertThat(row.isExpanded()).isFalse();
        assertThat(row.getFormat()).isNotNull();
        assertThat(row.getCells()).isNotNull();
        assertThat(row.getToBottom()).isNull();
        assertThat(row.getParentId()).isNull();
        assertThat(row.getSiblingId()).isNull();
        assertThat(row.getAbove()).isNull();
    }

    @Test
    void testJsonObjectExcludesRowIdAfterJsonIgnore() throws Exception {
        Row row = new Row();
        row.setId(1L);

        String json = new JacksonJsonSerializer().serialize(row);

        assertThat(json.contains("\"rowId\":1")).isFalse();
        assertThat(row.getRowId()).isEqualTo(1);

    }

}
