package com.lotte15.lotteonlinegym.retrofit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lotte15.lotteonlinegym.model.UserModel;
import com.lotte15.lotteonlinegym.util.GlobalApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginThread extends Thread {
    final static String TAG = LoginThread.class.getSimpleName();
    private Handler handler;
    private UserModel userModel;
    private String id;
    private String pwd;

    public LoginThread(Handler handler, String id, String pwd){
        super();
        this.handler = handler;
        this.id = id;
        this.pwd = pwd;
    }

    @Override
    public void run() {
        super.run();
        userModel = new UserModel();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit client = new Retrofit.Builder().baseUrl(GlobalApplication.getGlobalApplicationContext().getBaseUrl())
                .client(GlobalApplication.getGlobalApplicationContext().createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        UserApiService.GetUserInfoInterface service = client.create(UserApiService.GetUserInfoInterface.class);
        Call<UserModel> call = service.get_userInfo(id,pwd);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()){
                    Log.e(TAG,"성공 onResponse.raw :"+response.raw());
                    userModel = response.body();
                    Log.e(TAG,"이름 : "+userModel.getName());
                    GlobalApplication.getGlobalApplicationContext().setCurrentUser(userModel);
                    setMessage("성공");
                }else{
                    Log.e(TAG,"실패 raw :"+response.raw());
                    setMessage("실패");
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e(TAG,"getUserInfo 실패");
                Log.e(TAG,"cause : " +t.getCause());
                setMessage("실패");
            }
        });

    }
    private void setMessage(String msg){
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
