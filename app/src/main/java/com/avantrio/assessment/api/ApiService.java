package com.avantrio.assessment.api;

import com.avantrio.assessment.model.LoginRequest;
import com.avantrio.assessment.model.LoginResponse;
import com.avantrio.assessment.model.UserLogsMain;
import com.avantrio.assessment.model.UserResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("users")
    Call<ArrayList<UserResponse>> getUser(@Header("Authorization") String token);

    @GET("user/{userid}/logs")
    Call<UserLogsMain> getUserLogs(@Header("Authorization") String token, @Path("userid") String channel);
}
