package com.example.test_t1;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
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
    Handler handler = new Handler();

    public PlayService()
    {
        System.out.println("생성자 생성");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(receiver, new IntentFilter("com.example.PLAY_TO_SERVICE"));
        Intent intent = new Intent("com.example.PLAY_TO_SERVICE");
        intent.putExtra("mode","start");
        sendBroadcast(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        System.out.println("playservice동작");
        //서버 동작부분


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String mode = intent.getStringExtra("mode"); //extra에담긴 값을가져와서 그에 맞는 실행을 함
            if(mode !=null)
            {
                if(mode.equals("start"))
                {
                   System.out.println("리시버 동작");
                    new Thread(new Runnable()
                    {
                        int num=0;
                        @Override
                        public void run()
                        {
                            try
                            {
//                                URL text = new URL("https://localhost:8080/");
                                URL text = new URL("https://m.naver.com/");
                                HttpURLConnection conn = (HttpURLConnection) text.openConnection();
                                if(conn != null)
                                {
                                    conn.setConnectTimeout(3 *1000);
                                    conn.setRequestMethod("GET");
                                    conn.setDoInput(true);
                                    conn.setDoOutput(true);

                                    System.out.println("gdgd");
                                    int resCode = conn.getResponseCode(); //여기서 안댐

                                    if(resCode == HttpURLConnection.HTTP_OK)
                                    {
                                        System.out.println("ggd");
                                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                        StringBuffer sb= new StringBuffer();
                                        String inputLine = null;
                                        while((inputLine =in.readLine()) !=null)
                                        {
                                            System.out.println("찾는중....");
                                            sb.append(inputLine);
                                        }
                                        String result = sb.toString();
                                        System.out.println("결과: "+result);
                                    }
                                }
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    }).start();
                }
                else  if(mode.equals("stop"))
                {

                }
            }
        }
    };


}