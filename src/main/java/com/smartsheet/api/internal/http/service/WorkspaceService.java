package com.smartsheet.api.internal.http.service;

import com.smartsheet.api.models.ContainerDestination;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Workspace;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface WorkspaceService {
    @GET("workspaces")
    PagedResult<Workspace> listWorkspaces(@QueryMap Map<String, Object> params);

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
