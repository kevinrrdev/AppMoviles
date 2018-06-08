package com.example.kevin.appmoviles.api;


import com.example.kevin.appmoviles.api.request.LoginRequest;
import com.example.kevin.appmoviles.api.request.UsuarioRequest;
import com.example.kevin.appmoviles.api.response.LoginResponse;
import com.example.kevin.appmoviles.api.response.UsuarioResponse;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {




    @POST("api/login.php")
    Call<LoginResponse> doLogin(@Body LoginRequest request);

    @POST("api/register.php")
    Call<UsuarioResponse> doRegister(@Body UsuarioRequest request);

}