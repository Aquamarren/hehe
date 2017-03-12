package com.example.marrenmatias.trynavdrawer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton badge,settings;
    TextView un;
    /** The total number of menu items in the {@link NavigationView} */
    private static final int MENU_ITEMS = 7;
    //** Contains the {@link MenuItem} views in the {@link NavigationView} */
    private final ArrayList<View> mMenuItems = new ArrayList<>(MENU_ITEMS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));

        View header = navigationView.getHeaderView(0);
        un = (TextView) header.findViewById(R.id.username);
        final Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        un.setTypeface(myCustomFont);


        final Menu navMenu = navigationView.getMenu();

        navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remember to remove the installed OnGlobalLayoutListener
                navigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Loop through and find each MenuItem View
                for (int i = 0, length = MENU_ITEMS; i < length; i++) {
                    final String id = "nav" + (i + 1);
                    final MenuItem item = navMenu.findItem(getResources().getIdentifier(id, "id", getPackageName()));
                    navigationView.findViewsWithText(mMenuItems, item.getTitle(), View.FIND_VIEWS_WITH_TEXT);
                }
                // Loop through each MenuItem View and apply your custom Typeface
                for (final View menuItem : mMenuItems) {
                    ((TextView) menuItem).setTypeface(myCustomFont, Typeface.BOLD);
                }
            }

        });

        Intent i = getIntent();
        String fragmentName = i.getStringExtra("to");
        Log.d("Test1", "Test1" + fragmentName);
        if (fragmentName != null && fragmentName.equals("ViewExpense")) {
            setTitle("Expenses");
            ViewExpense fragment = new ViewExpense();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment3");
            fragmentTransaction.commit();
        }
        else if (fragmentName != null && fragmentName.equals("ViewSavings")) {
            setTitle("Savings");
            ViewSavings fragment = new ViewSavings();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment5");
            fragmentTransaction.commit();
        }
        else if (fragmentName != null && fragmentName.equals("ViewBudget")) {
            setTitle("Budget");
            ViewBudget fragment = new ViewBudget();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment2");
            fragmentTransaction.commit();
        }
        else if (fragmentName != null && fragmentName.equals("ViewGoals")) {
            setTitle("Goals");
            ViewGoalsPage fragment = new ViewGoalsPage();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment6");
            fragmentTransaction.commit();
        }


        badge = (ImageButton) header.findViewById(R.id.badgePage);
        settings = (ImageButton) header.findViewById(R.id.settingsPage);

        badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewBadges.class);
                startActivity(i);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewSettings.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav1) {

            setTitle("Income");
            AddIncome fragment = new AddIncome();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment1");
            fragmentTransaction.commit();

        } else if (id == R.id.nav2) {

            setTitle("Budget");
            ViewBudget fragment = new ViewBudget();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment2");
            fragmentTransaction.commit();

        } else if (id == R.id.nav3) {

            setTitle("Expenses");
            ViewExpense fragment = new ViewExpense();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment3");
            fragmentTransaction.commit();

        } else if (id == R.id.nav4) {

            setTitle("Reports");
            ViewReports fragment = new ViewReports();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment4");
            fragmentTransaction.commit();

        } else if (id == R.id.nav5) {

            setTitle("Savings");
            ViewSavings fragment = new ViewSavings();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment5");
            fragmentTransaction.commit();

        } else if (id == R.id.nav6) {

            setTitle("Goals");
            ViewGoalsPage fragment = new ViewGoalsPage();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment6");
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav7) {

            setTitle("Community");
            ViewCommunity fragment = new ViewCommunity();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame,fragment,"fragment7");
            fragmentTransaction.commit();


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

