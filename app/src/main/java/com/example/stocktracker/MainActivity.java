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
                .addToBackStack(null).commit();

        chart = new ChartFragment();
        chart.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, chart)
                .addToBackStack(null).commit();
    }

    public void chartNav(View view){
        chart = new ChartFragment();
        chart.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, chart)
                .addToBackStack(null).commit();
    }
    public void searchNav(View view){
        search = new SearchFragment();
        search.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, search)
                .addToBackStack(null).commit();

    }
    public void newsNav(View view){
        news = new NewsFragment();
        news.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, news)
                .addToBackStack(null).commit();

    }
    public void settingsNav(View view){
        settings = new SettingsFragment();
        settings.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, settings)
                .addToBackStack(null).commit();

    }
    public void helpNav(View view){
        help = new HelpFragment();
        help.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, help)
                .addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 2)
            getSupportFragmentManager().popBackStack();
        else
            finish();    // Finish the activity
    }
}