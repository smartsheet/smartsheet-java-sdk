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

package com.smartsheet.api.integrationtest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.BooleanObjectValue;
import com.smartsheet.api.models.BulkItemResult;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Result;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.SheetSummary;
import com.smartsheet.api.models.SummaryField;
import com.smartsheet.api.models.enums.ColumnType;
import com.smartsheet.api.models.enums.SummaryFieldExclusion;
import com.smartsheet.api.models.enums.SummaryFieldInclusion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SheetSummaryResourcesIT extends ITResourcesImpl {

    Smartsheet smartsheet;
    Sheet sheet;
    List<SummaryField> asf;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();

        // create sheet object
        Sheet sheetHome = createSheetObject();

        //create sheet
        sheet = smartsheet.sheetResources().createSheet(sheetHome);
        assertThat(sheet.getColumns()).hasSameSizeAs(sheetHome.getColumns());
    }

    @AfterEach
    public void Teardown() throws Exception {
        smartsheet.sheetResources().deleteSheet(sheet.getId());
    }

    @Test
    void testSheetSummaryMethods() throws SmartsheetException, IOException {
        testAddSheetSummaryFields();
        testGetSheetSummary();
        testAddSheetSummaryFieldsWithPartialSuccess();
        testGetSheetSummaryFields();
        testUpdateSheetSummaryFields();
        testUpdateSheetSummaryFieldsWithPartialSuccess();
        testAddSheetSummaryFieldImage();
        testDeleteSheetSummaryFields();
    }

    public void testAddSheetSummaryFields() throws SmartsheetException {
        SummaryField sf = new SummaryField();
        sf.setTitle("Hello World");
        sf.setType(ColumnType.CHECKBOX);
        sf.setObjectValue(new BooleanObjectValue().setValue(true));

        SummaryField sf1 = new SummaryField();
        sf1.setTitle("Checkbox");
        sf1.setType(ColumnType.CHECKBOX);
        sf1.setObjectValue(new BooleanObjectValue().setValue(false));

        asf = smartsheet.sheetResources().summaryResources().addSheetSummaryFields(
                sheet.getId(), Arrays.asList(sf, sf1), true);

        assertThat(asf).hasSize(2);
    }

    public void testGetSheetSummary() throws SmartsheetException {
        SheetSummary sheetSummary = smartsheet.sheetResources().summaryResources().getSheetSummary(
                sheet.getId(), EnumSet.of(SummaryFieldInclusion.WRITERINFO, SummaryFieldInclusion.FORMAT),
                EnumSet.of(SummaryFieldExclusion.DISPLAYVALUE));
        assertThat(sheetSummary.getFields()).hasSize(2);
    }

    public void testAddSheetSummaryFieldsWithPartialSuccess() throws SmartsheetException {
        SummaryField sf = new SummaryField();
        sf.setTitle("Hello World");
        sf.setType(ColumnType.CHECKBOX);
        sf.setObjectValue(new BooleanObjectValue().setValue(true));

        SummaryField sf1 = new SummaryField();
        sf1.setTitle("Eeck!");
        sf1.setType(ColumnType.CHECKBOX);
        sf1.setObjectValue(new BooleanObjectValue().setValue(false));

        BulkItemResult<SummaryField> asf = smartsheet.sheetResources().summaryResources().addSheetSummaryFieldsWithPartialSuccess(
                sheet.getId(), Arrays.asList(sf, sf1), null);

        assertThat(asf.getMessage()).isEqualTo("PARTIAL_SUCCESS");
        assertThat(asf.getResult())
                .isNotNull()
                .hasSize(1);
        assertThat(asf.getFailedItems())
                .isNotNull()
                .hasSize(1);
    }

    public void testGetSheetSummaryFields() throws SmartsheetException {
        PagedResult<SummaryField> fields = smartsheet.sheetResources().summaryResources().getSheetSummaryFields(
                sheet.getId(), EnumSet.of(SummaryFieldInclusion.WRITERINFO),
                EnumSet.of(SummaryFieldExclusion.DISPLAYVALUE), null);
        assertThat(fields.getData()).hasSize(3);
    }

    public void testUpdateSheetSummaryFields() throws SmartsheetException {
        SummaryField sf = new SummaryField();
        sf.setId(asf.get(0).getId());
        sf.setTitle("Hellooo World!");

        SummaryField sf1 = new SummaryField();
        sf1.setId(asf.get(1).getId());
        sf1.setTitle("Eeek!");

        List<SummaryField> usf = smartsheet.sheetResources().summaryResources().updateSheetSummaryFields(
                sheet.getId(), Arrays.asList(sf, sf1), null);
    }

    public void testUpdateSheetSummaryFieldsWithPartialSuccess() throws SmartsheetException {
        SummaryField sf = new SummaryField();
        sf.setId(asf.get(0).getId());
        sf.setTitle("Hellooo World!");

        SummaryField sf1 = new SummaryField();
        sf1.setId(123L);
        sf1.setTitle("Eeek!");

        BulkItemResult<SummaryField> usf = smartsheet.sheetResources().summaryResources().updateSheetSummaryFieldsWithPartialSuccess(
                sheet.getId(), Arrays.asList(sf, sf1), null);

        assertThat(usf.getMessage()).isEqualTo("PARTIAL_SUCCESS");
        assertThat(usf.getResult())
                .isNotNull()
                .hasSize(1);
        assertThat(usf.getFailedItems())
                .isNotNull()
                .hasSize(1);
    }

    public void testAddSheetSummaryFieldImage() throws SmartsheetException {
        Result<SummaryField> rsf = null;
        try {
            rsf = smartsheet.sheetResources().summaryResources().addSheetSummaryFieldImage(
                    sheet.getId(), asf.get(0).getId(),
                    "src/test/resources/exclam.png", null, "alt text");

            File file = new File("src/test/resources/exclam.png");
            rsf = smartsheet.sheetResources().summaryResources().addSheetSummaryFieldImage(
                    sheet.getId(), asf.get(0).getId(), file, "image/png", "alt text");

            rsf = smartsheet.sheetResources().summaryResources().addSheetSummaryFieldImage(
                    sheet.getId(), asf.get(0).getId(), new FileInputStream(file), "image/png", file.length(), "alt text");

        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThat(rsf.getResultCode()).isZero();
    }

    public void testDeleteSheetSummaryFields() throws SmartsheetException {
        Set<Long> setOfIds = new HashSet<>();
        setOfIds.add(asf.get(0).getId());
        setOfIds.add(123L);
        List<Long> delIds = smartsheet.sheetResources().summaryResources().deleteSheetSummaryFields(
                sheet.getId(), setOfIds, true);
        assertThat(delIds).hasSize(1);
    }
}

