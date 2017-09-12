package org.maciejowka.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.maciejowka.R;
import org.maciejowka.events.EventsFragment;
import org.maciejowka.notices.NoticesFragment;
import org.maciejowka.publications.SinglePublication;
import org.maciejowka.schedule.ScheduleFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        setupDrawerContent(navigationView);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        MenuItem item = navigationView.getMenu().getItem(0);
        setFragment(EventsFragment.class);
        item.setChecked(true);
        setTitle(item.getTitle());
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                item.setChecked(true);
                setTitle(item.getTitle());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, 0);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_events:
                setFragment(EventsFragment.class);
                break;
            case R.id.nav_notices:
//                setFragment(NoticesFragment.class);
                Intent intent = new Intent(this, SinglePublication.class);
                startActivity(intent);
                break;
            case R.id.nav_schedule:
                setFragment(ScheduleFragment.class);
                break;
            case R.id.nav_contact:
//                setFragment(EventsFragment.class);
                break;
            case R.id.nav_about:
//                setFragment(EventsFragment.class);
                break;
        }
    }

    private void setFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_EXIT_MASK)
                .replace(R.id.main_activity_content, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstance) {
        super.onPostCreate(savedInstance);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
