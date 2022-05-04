package com.example.stocktracker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/*
 * @author: Hector Beltran & Marin Maksutaj
 * @description: This is the MainActivity class. It is used to display the main screen
 *              of the app. It displays the different fragments that are used in the app.
 */
public class MainActivity extends AppCompatActivity {
    NavTabBarFragment nav = null;
    ChartFragment chart = null;
    SearchFragment search = null;
    NewsFragment news = null;
    SettingsFragment settings = null;
    HelpFragment help = null;
    SharedViewModel model =null;

    @Override
    /*
    * The onCreate method for the class. It is used to initialize the class.
    */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        model = new ViewModelProvider( this ).get(SharedViewModel.class);
        model.setFrom(0);
        model.setTo(0);
        nav = new NavTabBarFragment();
        nav.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.navigationContainer, nav)
                .commit();
        chart = new ChartFragment();
        chart.setContainerActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, chart)
                .commit();
    }

    /*
    * The chartNav method for the class. It is used to display the chart fragment.
    */
    public void chartNav(View view){
        chart = new ChartFragment();
        chart.setContainerActivity(this);
        model.setTo(0);
        //avoids redraw to save resources
        if(model.getFrom().getValue() == model.getTo().getValue() ) return ;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.mainContainer, chart).commit();
    }
    /*
    * The searchNav method for the class. It is used to display the search fragment.
    */
    public void searchNav(View view){
        search = new SearchFragment();
        search.setContainerActivity(this);
        model.setTo(1);
        if(model.getFrom().getValue() == model.getTo().getValue() ) return ;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.mainContainer, search).commit();

    }
    /*
    * The newsNav method for the class. It is used to display the news fragment.
    */
    public void newsNav(View view){
        news = new NewsFragment();
        news.setContainerActivity(this);
        model.setTo(2);
        if(model.getFrom().getValue() == model.getTo().getValue() ) return ;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.mainContainer, news).commit();

    }
    /*
    * The settingsNav method for the class. It is used to display the settings fragment.
    */
    public void settingsNav(View view){
        settings = new SettingsFragment();
        settings.setContainerActivity(this);
        model.setTo(3);
        if(model.getFrom().getValue() == model.getTo().getValue() ) return ;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.mainContainer, settings).commit();

    }
    /*
    * The helpNav method for the class. It is used to display the help fragment.
    */
    public void helpNav(View view){
        help = new HelpFragment();
        help.setContainerActivity(this);
        model.setTo(4);
        if(model.getFrom().getValue() == model.getTo().getValue() ) return ;
        getSupportFragmentManager().beginTransaction().
                replace(R.id.mainContainer, help).commit();

    }

    /*
    * The makeTransparent method for the class. It is
    * used to animate the transition between fragments.
    */
    private void makeTransition(){
        int from = model.getFrom().getValue();
        if(from == 3) settings.animateExit();
        if(from == 4) help.animateExit();
        int to = model.getTo().getValue();
    }

}