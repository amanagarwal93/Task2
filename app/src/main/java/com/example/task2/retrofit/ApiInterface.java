package com.example.task2.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("interview.php")
    @FormUrlEncoded
    Call<ResponseData> getData(@Field("email_id") String email);
}
