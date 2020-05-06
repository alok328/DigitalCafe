package com.alok328raj.digitalcafe.API;

import com.alok328raj.digitalcafe.API.Model.BalanceResponse;
import com.alok328raj.digitalcafe.API.Model.LoginResponse;
import com.alok328raj.digitalcafe.API.Model.ProfileResponse;
import com.alok328raj.digitalcafe.API.Model.TransactionResponse;
import com.alok328raj.digitalcafe.API.Model.transaction.Transaction;
import com.alok328raj.digitalcafe.API.RequestBody.BalanceRequestBody;
import com.alok328raj.digitalcafe.API.RequestBody.LoginRequestBody;
import com.alok328raj.digitalcafe.API.RequestBody.SignupRequestBody;
import com.alok328raj.digitalcafe.API.RequestBody.TransactionsRequestBody;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiClient {

    /*@GET("items")
    Call<ItemModel> getListOfItems();

    @POST("item/{name}")
    Call<Item> createItem(@Path("name") String name, @Body PostBody postBody);*/

    @POST("user/login")
    Call<LoginResponse> login(@Body LoginRequestBody loginRequestBody);

    @POST("user/register")
    Call<JSONObject> signup(@Body SignupRequestBody signupRequestBody);

    @GET("user/{roll}/balance")
    Call<BalanceResponse> getBalance(@Header("Authorization") String authorization, @Path("roll") String roll);

    @POST("user/{roll}/transaction")
    Call<JSONObject> addTransaction(@Header("Authorization") String authorization, @Path("roll") String roll, @Body TransactionsRequestBody transactionsRequestBody);

    @GET("user/{roll}/transaction")
    Call<List<Transaction>> getTransaction(@Header("Authorization") String authorization, @Path("roll") String roll);

    @GET("user/{roll}/profile")
    Call<ProfileResponse> getProfile(@Header("Authorization") String authorization, @Path("roll") String roll);
}
