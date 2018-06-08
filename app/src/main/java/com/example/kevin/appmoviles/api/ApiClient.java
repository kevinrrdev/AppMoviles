package com.example.kevin.appmoviles.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static ApiService apiService=null;

    public static ApiService getApiClient(){

        if(apiService==null){
            OkHttpClient okHttpClient= new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            Retrofit builder= new Retrofit.Builder()
                    .baseUrl("http://kechaval.000webhostapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            apiService = builder.create(ApiService.class);
        }

        return apiService;

    }
}