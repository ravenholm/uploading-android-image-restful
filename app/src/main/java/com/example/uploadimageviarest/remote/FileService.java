package com.example.uploadimageviarest.remote;

import com.example.uploadimageviarest.model.FileInfo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {
    //The upload below should match your API call upload
    @Multipart
    @POST("upload")
    Call<FileInfo> upload(@Part MultipartBody.Part file);
}
