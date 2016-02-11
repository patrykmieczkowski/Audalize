package com.mieczkowskidev.audalize;

import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mieczkowskidev.audalize.API.RestAPI;
import com.mieczkowskidev.audalize.API.RestClientMultipart;
import com.mieczkowskidev.audalize.fragment.AllFilesListFragment;
import com.mieczkowskidev.audalize.fragment.ProfileFragment;
import com.mieczkowskidev.audalize.utils.FragmentSwitcher;
import com.mieczkowskidev.audalize.utils.LoginManager;

import java.io.File;

import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareMenu();

        showStartingFragment();

//        postItemOnServer("/storage/emulated/0/Music/audalize/CALL_10-22:30:21.643.mp4", "first englisch speach");

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_files_list) {
            FragmentSwitcher.switchToFragment(this, AllFilesListFragment.newInstance(), R.id.main_activity_placeholder);

        } else if (id == R.id.nav_profile) {
            FragmentSwitcher.switchToFragment(this, ProfileFragment.newInstance(), R.id.main_activity_placeholder);

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareMenu() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showStartingFragment() {
        Log.d(TAG, "showStartingFragment()");

        FragmentSwitcher.switchToFragment(this, AllFilesListFragment.newInstance(), R.id.main_activity_placeholder);
    }

    public void postItemOnServer(String path, String fileName) {
        Log.d(TAG, "postItemOnServer()");

        RestClientMultipart restClientMultipart = new RestClientMultipart();
        RestAPI restAPI = restClientMultipart.getRestMultipartAdapter().create(RestAPI.class);

        TypedFile typedFile = new TypedFile("file:", new File(path));


        restAPI.addAudio(LoginManager.getTokenFromShared(this), typedFile, fileName)
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        Log.d(TAG, "call: success " + response.getReason());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "call: " + throwable.getMessage());
                    }
                });
    }
}
