package com.example.test_t1;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int inter_num=0;

    ArrayList<issue_data> issueDataList;
    BottomNavigationView navigationView;
    AppBarConfiguration appBarConfiguration;
    NavController navController;

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = new Intent(this,PlayService.class);
        startService(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.nav_view);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_btn1, R.id.navigation_btn2, R.id.navigation_btn4).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController); //navigationUI가 객체가아님

    }

    public int get_inter_num()
    {
        return inter_num;
    }

    //메서드를 만들어서 할려니 인수로 받은값은 변형이안댐 그대로 유지되야함 (final)

    //네비 3선택
    public void selected3()
    {
        System.out.println("선택 메서드 작동");
        navigationView.setSelectedItemId(R.id.navigation_btn4); //해당항목을 누른것과 같은효과
    }






}