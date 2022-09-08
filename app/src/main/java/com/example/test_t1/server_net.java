package com.example.test_t1;

import retrofit2.Call;
import retrofit2.http.*;


public interface server_net
{
    @GET("ocr/connect")
    Call<String> connect(@Query("userEmail")String email);

}
