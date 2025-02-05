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

import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.models.Favorite;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.PaginationParameters;
import com.smartsheet.api.models.enums.FavoriteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class FavoriteResourcesImplTest extends ResourcesImplBase {
    private FavoriteResourcesImpl favoriteResources;

    @BeforeEach
    public void setUp() throws Exception {
        favoriteResources = new FavoriteResourcesImpl(new SmartsheetImpl("http://localhost:9090/1.1/",
                "accessToken", new DefaultHttpClient(), serializer));
    }

    @Test
    void testAddFavorites() throws Exception {
        server.setResponseBody(new File("src/test/resources/addFavorites.json"));
        List<Favorite> favoritesToAdd = new Favorite.AddFavoriteBuilder().addFavorite(8400677765441412L, FavoriteType.SHEET).build();
        List<Favorite> addedFavorites = favoriteResources.addFavorites(favoritesToAdd);
        assertThat(addedFavorites).hasSize(1);
        assertThat(addedFavorites.get(0).getType()).isEqualTo(FavoriteType.SHEET);
        assertThat(addedFavorites.get(0).getObjectId()).isEqualTo(8400677765441412L);
    }

    @Test
    void testListFavorites() throws Exception {
        server.setResponseBody(new File("src/test/resources/listFavorites.json"));
        PaginationParameters parameters = new PaginationParameters(false, 1, 1);
        PagedResult<Favorite> favorites = favoriteResources.listFavorites(parameters);
        assertThat(favorites.getData().size()).isEqualTo(2);
        assertThat(favorites.getData().get(0).getType()).isNotNull();
        assertThat(favorites.getData().get(1).getType()).isNotNull();
        assertThat(favorites.getData().get(0).getObjectId()).isNotNull();
        assertThat(favorites.getData().get(1).getObjectId()).isNotNull();
    }

    @Test
    void testIsFavorite() throws Exception {
        server.setResponseBody(new File("src/test/resources/isFavorite.json"));
        Favorite isFavorite = favoriteResources.isFavorite(FavoriteType.SHEET, 5897312590423940L);
        assertThat(isFavorite.getObjectId()).isEqualTo(5897312590423940L);
        assertThat(isFavorite.getType()).isEqualTo(FavoriteType.SHEET);
    }

    @Test
    void testRemoveFavorites() throws Exception {
        server.setResponseBody(new File("src/test/resources/removeFavorites.json"));
        Set<Long> folderIds = new HashSet<>();
        folderIds.add(123L);
        folderIds.add(345L);

        assertThatCode(() -> favoriteResources.removeFavorites(FavoriteType.FOLDER, folderIds)).doesNotThrowAnyException();
    }
}
