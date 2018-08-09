package com.example.alkautherwater.api;



import com.example.alkautherwater.model.Result;
import com.example.alkautherwater.model.Results;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {

    @GET("Alkautherlogin/productdetails_json.php")
    Call<Result> getProduct();

    @FormUrlEncoded
    @POST("Alkautherlogin/contact_json.php")
    Call<Results> contactUs(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("message") String message
    );
    @FormUrlEncoded
    @POST("BuyProduct")
    Call<Results> buyProduct(
            @Field("product_id") int product_id,
            @Field("quantity") String quantity,
            @Field("customer_name") String customer_name,
            @Field("phone") String phone,
            @Field("pincode") String pincode,
            @Field("address") String address


    );
}
