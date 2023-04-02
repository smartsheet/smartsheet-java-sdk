package com.smartsheet.api.models;

/*
 * #[license]
 * Smartsheet SDK for Java
 * %%
 * Copyright (C) 2023 Smartsheet
 * %%
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
 * %[license]
 */

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SheetTest {

    @Test
    void testGetRowByRowNumber() {
        Sheet sheet = new Sheet();
        sheet.setReadOnly(false);
        sheet.setDiscussions(new ArrayList<>());
        sheet.setAttachments(new ArrayList<>());

        List<Row> rows = new ArrayList<>();
        Row row = new Row();
        row.setRowNumber(5);
        row.setId(1234L);
        rows.add(row);
        sheet.setRows(rows);

        assertEquals(row,sheet.getRowByRowNumber(5));
        assertNull(sheet.getRowByRowNumber(20));
        assertNull(new Sheet().getRowByRowNumber(0));
    }

}
