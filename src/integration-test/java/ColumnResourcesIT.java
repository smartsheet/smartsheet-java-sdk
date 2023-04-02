/*
 * #[license]
 * Smartsheet Java SDK
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

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.ColumnInclusion;
import com.smartsheet.api.models.enums.ColumnType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ColumnResourcesIT extends ITResourcesImpl{
    Smartsheet smartsheet;
    Sheet newSheet;
    Column addedColumn;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testColumnMethods() throws SmartsheetException, IOException {
        testAddColumn();
        testListColumns();
        testUpdateColumn();
        testGetColumn();
        testColumnFormula();
        testDeleteColumn();
    }

    public void testAddColumn() throws SmartsheetException, IOException {
        //create new sheet
        Sheet sheet = createSheetObject();
        newSheet = smartsheet.sheetResources().createSheet(sheet);

        List<String> options = Arrays.asList("Hello", "World!", "How", "Are", "You");

        //create columns
        Column column1 = new Column.AddColumnToSheetBuilder().setTitle("New Picklist Column 1").setType(ColumnType.PICKLIST).setIndex(4).setOptions(Arrays.asList("First", "Second", "Third")).build();
        Column column2 = new Column.AddColumnToSheetBuilder().setTitle("New Date Column").setType(ColumnType.DATE).setIndex(4).build();
        Column column3 = new Column.AddColumnToSheetBuilder().setTitle("New Picklist Column 2").setType(ColumnType.PICKLIST).setIndex(4).setOptions(Arrays.asList("1", "2", "3")).build();

        //add columns
        List<Column> addedColumns = smartsheet.sheetResources().columnResources().addColumns(newSheet.getId(), Arrays.asList(column1, column2, column3));
        assertEquals(3, addedColumns.size());
    }

    public void testGetColumn() throws SmartsheetException, IOException {
        Column newColumn = smartsheet.sheetResources().columnResources().getColumn(newSheet.getId(), addedColumn.getId(), EnumSet.allOf(ColumnInclusion.class));
        Column newColumn1 = smartsheet.sheetResources().columnResources().getColumn(newSheet.getId(), addedColumn.getId(), null);
        assertNotNull(newColumn);
    }

    public void testListColumns() throws SmartsheetException, IOException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Column> wrapper = smartsheet.sheetResources().columnResources().listColumns(newSheet.getId(), EnumSet.allOf(ColumnInclusion.class), parameters);

        PagedResult<Column> wrapper1= smartsheet.sheetResources().columnResources().listColumns(newSheet.getId(), null, null);

        List<Column> columns = wrapper.getData();
        addedColumn = columns.get(2);
        assertNotNull(columns);
    }

    public void testUpdateColumn() throws SmartsheetException, IOException {
        Column column1 = new Column.UpdateColumnBuilder().setColumnId(addedColumn.getId()).setTitle("First Column").setIndex(0).setType(ColumnType.PICKLIST).setOptions(Arrays.asList("One", "Two")).build();
        Column updatedColumn = smartsheet.sheetResources().columnResources().updateColumn(newSheet.getId(), column1);

        assertNotNull(updatedColumn);
    }

    public void testColumnFormula() throws SmartsheetException {
        Column col = new Column.AddColumnToSheetBuilder().setIndex(0).setTitle("colFormula").setType(ColumnType.DATE).build();
        col.setFormula("=TODAY()");
        List<Column> cols = smartsheet.sheetResources().columnResources().addColumns(newSheet.getId(), Arrays.asList(col));
        assertEquals(1, cols.size());
        assertNotNull(cols.get(0).getFormula());

        col.setFormula("");
        col.setId(cols.get(0).getId());
        Column updateColumn = smartsheet.sheetResources().columnResources().updateColumn(newSheet.getId(), col);
        assertNull(updateColumn.getFormula());
    }

    public void testDeleteColumn() throws SmartsheetException, IOException {
        smartsheet.sheetResources().columnResources().deleteColumn(newSheet.getId(), addedColumn.getId());
        //cleanup
        deleteSheet(newSheet.getId());
    }
}
