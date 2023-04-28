package com.smartsheet.api.internal.http.service;

import com.smartsheet.api.models.*;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

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
public interface ProofingService {
    @GET("sheets/{sheetId}/proofs")
    Call<PagedResult<Proof>> listProofs(@Path("sheetId") long sheetId, @QueryMap Map<String, Object> params);

    @GET("sheets/{sheetId}/proofs/{proofId}")
    Call<Proof> getProof(@Path("sheetId") long sheetId, @Path("proofId") long proofId, @QueryMap Map<String, Object> params);

    @GET("sheets/{sheetId}/proofs/{proofId}/attachments")
    Call<PagedResult<Attachment>> listProofAttachments(@Path("sheetId") long sheetId, @Path("proofId") long proofId, @QueryMap Map<String, Object> params);

    @GET("sheets/{sheetId}/proofs/{proofId}/discussions")
    Call<PagedResult<Discussion>> listProofDiscussions(@Path("sheetId") long sheetId, @Path("proofId") long proofId, @QueryMap Map<String, Object> params);

    @GET("sheets/{sheetId}/proofs/{proofId}/requestactions")
    Call<PagedResult<ProofRequestAction>> listProofRequestActions(@Path("sheetId") long sheetId, @Path("proofId") long proofId, @QueryMap Map<String, Object> params);

    @DELETE("sheets/{sheetId}/proofs/{proofId}")
    Call<Result<Void>> deleteProof(@Path("sheetId") long sheetId, @Path("proofId") long proofId);

    @DELETE("sheets/{sheetId}/proofs/{proofId}/requests")
    Call<Result<Void>> deleteProofRequests(@Path("sheetId") long sheetId, @Path("proofId") long proofId);

    @Multipart
    @POST("sheets/{sheetId}/proofs/{proofId}/attachments")
    Call<Result<Attachment>> attachFileToProof(@Path("sheetId") long sheetId, @Path("proofId") long proofId, @PartMap Map<String, RequestBody> file);

    @POST("sheets/{sheetId}/proofs/{proofId}/discussions")
    Call<Result<Discussion>> createProofDiscussion(@Path("sheetId") long sheetId, @Path("proofId") long proofId, @Body Comment comment);

    @POST("sheets/{sheetId}/proofs/{proofId}/requests")
    Call<ProofRequest> createProofRequest(@Path("sheetId") long sheetId, @Path("proofId") long proofId);

    @PUT("sheets/{sheetId}/proofs/{proofId}")
    Call<Proof> updateProofStatus(@Path("sheetId") long sheetId, @Path("proofId") long proofId, @Body UpdateProofStatusRequest request);
}
