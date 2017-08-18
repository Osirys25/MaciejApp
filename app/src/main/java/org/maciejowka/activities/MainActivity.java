package org.maciejowka.activities;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.maciejowka.R;
import org.maciejowka.fragments.EventsFragment;
import org.maciejowka.fragments.NoticesFragment;
import org.maciejowka.fragments.ScheduleFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerContent(navigationView);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        setFragment(EventsFragment.class);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragment(selectDrawerItem(item));
                item.setChecked(true);
                setTitle(item.getTitle());
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private Class selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_events:
                return EventsFragment.class;

            case R.id.nav_notices:
                return NoticesFragment.class;

            case R.id.nav_schedule:
                return ScheduleFragment.class;

            case R.id.nav_contact:
                return EventsFragment.class;

            case R.id.nav_about:
                return EventsFragment.class;

            default:
                return EventsFragment.class;
        }
    }

    private void setFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_activity_content, fragment).commit();
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
