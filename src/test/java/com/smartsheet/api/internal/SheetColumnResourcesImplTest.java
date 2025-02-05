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
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.ColumnInclusion;
import com.smartsheet.api.models.enums.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SheetColumnResourcesImplTest extends ResourcesImplBase {

    private SheetColumnResourcesImpl sheetColumnResourcesImpl;

    @BeforeEach
    public void setUp() throws Exception {
        sheetColumnResourcesImpl = new SheetColumnResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testListColumns() throws SmartsheetException, IOException {

        server.setResponseBody(new File("src/test/resources/listColumns.json"));
        PaginationParameters paginationParameters = new PaginationParameters(true, 1, 1);
        PagedResult<Column> wrapper = sheetColumnResourcesImpl.listColumns(
                1234L,
                EnumSet.allOf(ColumnInclusion.class),
                paginationParameters
        );
        List<Column> columns = wrapper.getData();
        assertThat(columns).hasSize(3);
        assertThat(columns.get(0).getType()).hasToString("CHECKBOX");
        assertThat(columns.get(0).getSymbol()).hasToString("STAR");
        assertThat(columns.get(0).isLocked()).isTrue();
        assertThat(columns.get(0).isLockedForUser()).isFalse();
        assertThat(columns.get(2).getTitle()).isEqualTo("Status");
    }

    @Test
    void testAddColumn() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/addColumn.json"));
        List<Column> columnsToCreate = new ArrayList<>();

        List<Column> addedColumns = sheetColumnResourcesImpl.addColumns(12345L, columnsToCreate);
        assertThat(addedColumns).hasSize(3);
        assertThat(addedColumns.get(0).getType()).hasToString("PICKLIST");
        assertThat(addedColumns.get(1).getType()).hasToString("DATE");
        assertThat(addedColumns.get(2).getType()).hasToString("PICKLIST");
    }

    @Test
    void testUpdateColumn() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/updateColumn.json"));

        Column col = new Column();
        col.setId(5005385858869124L);
        col.setIndex(0);
        col.setTitle("First Column");
        col.setType(ColumnType.PICKLIST);

        Column updatedColumn = sheetColumnResourcesImpl.updateColumn(123456789L, col);

        assertThat(updatedColumn).isNotNull();
        assertThat(updatedColumn.getTitle()).isEqualTo("First Column");

        assertThatThrownBy(() -> sheetColumnResourcesImpl.updateColumn(123456789L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testDeleteColumn() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/deleteColumn.json"));
        sheetColumnResourcesImpl.deleteColumn(123456789L, 987654321L);
    }

    @Test
    void testGetColumn() throws SmartsheetException, IOException {
        server.setResponseBody(new File("src/test/resources/getColumn.json"));
        Column col = new Column();
        col.setIndex(2);
        col.setTitle("Favorite");
        col.setType(ColumnType.CHECKBOX);

        Column newCol = sheetColumnResourcesImpl.getColumn(123L, 456L, EnumSet.of(ColumnInclusion.FILTERS));
        assertThat(newCol).isNotNull();
        assertThat(newCol.getTitle()).isEqualTo(col.getTitle());
    }
}
