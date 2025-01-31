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

package com.smartsheet.api.sdktest;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Sight;
import com.smartsheet.api.models.SightPublish;
import com.smartsheet.api.models.enums.DestinationType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SightsTest {

    @Test
    void listSights() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("List Sights");
        PagedResult<Sight> sights = ss.sightResources().listSights(null, null);
        assertThat(sights.getTotalCount()).isEqualTo(6);
    }

    @Test
    void getSight() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Sight");
        Sight sight = ss.sightResources().getSight(52);
        assertThat(sight.getId()).isEqualTo(52L);
    }

    @Disabled("destination types differ because of case")
    @Test
    void copySight() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Copy Sight");
        ContainerDestination dest = new ContainerDestination();
        dest.setDestinationType(DestinationType.FOLDER);
        dest.setDestinationId(484L);
        dest.setNewName("new sight");
        Sight sight = ss.sightResources().copySight(52, dest);
    }

    @Test
    void updateSight() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Update Sight");
        Sight sight = new Sight();
        sight.setId(812L);
        sight.setName("new new sight");
        ss.sightResources().updateSight(sight);
    }

    @Test
    void setPublishStatus() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Set Sight Publish Status");
        SightPublish publish = new SightPublish();
        publish.setReadOnlyFullEnabled(true);
        publish.setReadOnlyFullAccessibleBy("ALL");
        ss.sightResources().setPublishStatus(812, publish);
    }

    @Test
    void getPublishStatus() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Get Sight Publish Status");
        SightPublish publish = ss.sightResources().getPublishStatus(812L);
        assertThat(publish.getReadOnlyFullEnabled()).isTrue();
    }

    @Test
    void deleteSight() throws SmartsheetException {
        Smartsheet ss = HelperFunctions.SetupClient("Delete Sight");
        ss.sightResources().deleteSight(700L);
    }
}
