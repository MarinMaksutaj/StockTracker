package com.example.stocktracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    NavTabBarFragment nav = null;
    ChartFragment chart = null;
    SearchFragment search = null;
    NewsFragment news = null;
    SettingsFragment settings = null;
    HelpFragment help = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        nav = new NavTabBarFragment();
        nav.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.navigationContainer, nav)
                .commit();

        chart = new ChartFragment();
        chart.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, chart)
                .commit();
    }

    public void chartNav(View view){
        chart = new ChartFragment();
        chart.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, chart).commit();
    }
    public void searchNav(View view){
        search = new SearchFragment();
        search.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, search).commit();

    }
    public void newsNav(View view){
        news = new NewsFragment();
        news.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, news).commit();

    }
    public void settingsNav(View view){
        settings = new SettingsFragment();
        settings.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, settings).commit();

    }
    public void helpNav(View view){
        help = new HelpFragment();
        help.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, help).commit();

    }

}