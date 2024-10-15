/*
 * Copyright (C) 2024 Smartsheet
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
import com.smartsheet.api.models.DeleteUserParameters;
import com.smartsheet.api.models.Favorite;
import com.smartsheet.api.models.Folder;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.FavoriteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteResourcesIT extends ITResourcesImpl {
    Smartsheet smartsheet;

    @BeforeEach
    public void setUp() throws Exception {
        smartsheet = createAuthentication();
    }

    @Test
    void testAddFavorites() throws SmartsheetException, IOException {
        Sheet sheet = smartsheet.sheetResources().createSheet(createSheetObject());
        Folder folder = createFolder();

        List<Favorite> favoritesToAdd = new Favorite.AddFavoriteBuilder()
                .addFavorite(sheet.getId(), FavoriteType.SHEET).addFavorite(folder.getId(), FavoriteType.FOLDER)
                .build();
        List<Favorite> addedfavorites = smartsheet.favoriteResources().addFavorites(favoritesToAdd);
        assertThat(addedfavorites).hasSize(2);
        deleteFolder(folder.getId());
        deleteSheet(sheet.getId());
    }

    @Test
    void testListFavorites() throws SmartsheetException {
        PaginationParameters parameters = new PaginationParameters.PaginationParametersBuilder().setIncludeAll(true).build();
        PagedResult<Favorite> favorites = smartsheet.favoriteResources().listFavorites(parameters);
        assertThat(favorites).isNotNull();
    }

    @Test
    void testRemoveFavorites() throws Exception {

        Folder folder1 = createFolder();
        Folder folder2 = createFolder();

        List<Favorite> favoriteList = new Favorite.AddFavoriteBuilder()
                .addFavorite(folder1.getId(), FavoriteType.FOLDER)
                .addFavorite(folder2.getId(), FavoriteType.FOLDER)
                .build();

        smartsheet
                .favoriteResources()
                .addFavorites(favoriteList);

        smartsheet.favoriteResources().removeFavorites(FavoriteType.FOLDER, new HashSet(Arrays.asList(folder1.getId())));
        //assertThat(smartsheet.favoriteResources().listFavorites(null)).isNotNull();

        //clean up
        deleteFolder(folder1.getId());
        deleteFolder(folder2.getId());
    }

    @Test
    void testDeleteUser() {
        DeleteUserParameters parameters = new DeleteUserParameters(12345L, true, true);
    }
}
