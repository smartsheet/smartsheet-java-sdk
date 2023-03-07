package com.smartsheet.api.internal.http.service;

/*
 * #[license]
 * Smartsheet Java SDK
 * %%
 * Copyright (C) 2014 - 2023 Smartsheet
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

import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Workspace;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface WorkspaceService {
    @GET("workspaces")
    Call<PagedResult<Workspace>> listWorkspaces(@QueryMap Map<String, Object> params);

    @GET("workspaces/{id}")
    Call<Workspace> getWorkspace(@Path("id") long id, @QueryMap Map<String, Object> params);

    @POST("workspaces")
    Call<Workspace> createWorkspace(@Body Workspace workspace);

    @PUT("workspaces/{id}")
    Call<Workspace> updateWorkspace(@Path("id") long id);

    @DELETE("workspaces/{id}")
    Call<Void> deleteWorkspace(@Path("id") long id);

    @POST("workspaces/{id}/copy")
    Call<Workspace> copyWorkspace(
            @Path("id") long id,
            @QueryMap Map<String, Object> params,
            @Body ContainerDestination containerDestination
    );
}
