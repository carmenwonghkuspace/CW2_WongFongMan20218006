package com.example.cw2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("employees")
    Call<List<User>> getUsers();


}
