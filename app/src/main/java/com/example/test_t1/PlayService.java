package com.example.test_t1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telecom.Call;
import android.view.textclassifier.TextLinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlayService extends Service
{
    public PlayService()
    {
        System.out.println("생성자 생성");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        new Thread(new Runnable()
        {
            int num=0;
            @Override
            public void run()
            {
                //실행할 코드 작성
//                String id = "";
//                OkHttpClient client =new OkHttpClient();
//                RequestBody body = new FormBody.Builder().add("Id",id).build();
//
//                Request request = new Request.Builder().url(" ").post(body).build();
//
////                client.newCall(request).enqueue(callback);
//                client.newCall(request).execute();
                try
                {
                    URL text = new URL(" ");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) text.openConnection();

                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
                    StringBuffer sb= new StringBuffer();
                    String inputLine;

                    while((inputLine =in.readLine()) !=null)
                    {
                        sb.append(inputLine);
                    }

                    String result = sb.toString();
                }
                catch (Exception e)
                {

                }




            }
        }).start();

        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



}