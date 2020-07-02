package com.example.task2.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.task2.retrofit.ApiInterface;
import com.example.task2.retrofit.ResponseData;
import com.example.task2.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataViewModel extends ViewModel {

    private String TAG = DataViewModel.class.getSimpleName();

    private ApiInterface apiInterface;

    private MutableLiveData<ResponseData> responseDataMutableLiveData;
    private Call<ResponseData> call;

    public MutableLiveData<ResponseData> getResponseDataMutableLiveData() {
        return responseDataMutableLiveData;
    }

    public DataViewModel() {
        responseDataMutableLiveData = new MutableLiveData<>();
        apiInterface = RetrofitClient.getRetrofit().create(ApiInterface.class);
        call = apiInterface.getData("interview@maishainfotech.com");
        populateData();
    }

    private void populateData() {
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: not null " + response.body());
                        responseDataMutableLiveData.setValue(response.body());
                    } else {
                        Log.d(TAG, "onResponse:  is null" + response.body());
                    }
                } else {
                    Log.d(TAG, "onResponse: unsuccessful " + response.body());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseData> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
