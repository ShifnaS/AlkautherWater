package com.example.alkautherwater.api;



import com.example.alkautherwater.model.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

public interface APIService {

    @GET("Alkautherlogin/productdetails_json.php")
    Call<Result> getProduct();

}
