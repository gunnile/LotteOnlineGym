package com.lotte15.lotteonlinegym.retrofit;

import com.lotte15.lotteonlinegym.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hyeongpil on 2018. 7. 31..
 */

public class UserApiService {

    public interface GetUserInfoInterface {
//        @Headers("Content-Type: application/json")
//        @GET("login2.jsp")
//        Call<UserModel>get_userInfo(@Query("Id") String id, @Query("Pw") String pwd);
            @FormUrlEncoded
            @POST("/login")
            Call<UserModel> get_userInfo(@Field("Id")String id, @Field("Pw")String pwd);
    }


}
