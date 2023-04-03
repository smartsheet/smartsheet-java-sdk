package com.smartsheet.api.sdk_test;
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


import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.SourceInclusion;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;


class SheetTest {


	@Test
	void ListSheets_NoParams()
	{
		try {
			Smartsheet ss = HelperFunctions.SetupClient("List Sheets - No Params");
			PagedResult<Sheet> sheets = ss.sheetResources().listSheets(null, null, null);
			assertThat(sheets.getData().get(0).getName()).isEqualTo("Copy of Sample Sheet");
		}catch(Exception ex){
			HelperFunctions.ExceptionMessage(ex.getMessage(), ex.getCause());
		}
	}

	@Test
	void ListSheets_IncludeOwnerInfo()
	{
		try{
			Smartsheet ss =  HelperFunctions.SetupClient("List Sheets - Include Owner Info");
			PagedResult<Sheet> sheets = ss.sheetResources().listSheets(EnumSet.of(SourceInclusion.OWNERINFO), null, null);
		assertThat(sheets.getData().get(0).getOwner()).isEqualTo("john.doe@smartsheet.com");
		}catch(Exception ex){
			HelperFunctions.ExceptionMessage(ex.getMessage(), ex.getCause());
		}
	}

	@Test
	void CreateSheet__Invalid_NoColumns()
	{
		try {
			Smartsheet ss = HelperFunctions.SetupClient("Create Sheet - Invalid - No Columns");

			Sheet sheetA = new Sheet();
			sheetA.setName("New Sheet");
			sheetA.setColumns(new ArrayList<>());
			ss.sheetResources().createSheet(sheetA);

		}catch(SmartsheetException ex){
		assertThat(ex.getMessage()).isEqualTo("The new sheet requires either a fromId or columns.");

		}catch(Exception ex){
			HelperFunctions.ExceptionMessage(ex.getMessage(), ex.getCause());
		}
	}
}
