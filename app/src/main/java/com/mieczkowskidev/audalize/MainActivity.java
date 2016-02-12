package com.mieczkowskidev.audalize;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.mieczkowskidev.audalize.API.RestAPI;
import com.mieczkowskidev.audalize.API.RestClientMultipart;
import com.mieczkowskidev.audalize.fragment.AllFilesListFragment;
import com.mieczkowskidev.audalize.fragment.HistoryFragment;
import com.mieczkowskidev.audalize.fragment.ProfileFragment;
import com.mieczkowskidev.audalize.fragment.SettingsFragment;
import com.mieczkowskidev.audalize.model.MediaFile;
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

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_files_list:
                FragmentSwitcher.switchToFragment(this, AllFilesListFragment.newInstance(), R.id.main_activity_placeholder);
                break;
            case R.id.nav_profile:
                FragmentSwitcher.switchToFragment(this, ProfileFragment.newInstance(), R.id.main_activity_placeholder);
                break;
            case R.id.nav_history:
                FragmentSwitcher.switchToFragment(this, HistoryFragment.newInstance(), R.id.main_activity_placeholder);
                break;
//            case R.id.nav_settings:
//                FragmentSwitcher.switchToFragment(this, SettingsFragment.newInstance(), R.id.main_activity_placeholder);
//                break;
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

    public void showStartingFragment() {
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

    public void showDialogAndDeleteItem(final MediaFile mediaFile) {
        Log.d(TAG, "showDialogAndDeleteItem()");

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete an item?\n" + mediaFile.getTitle())
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        deleteItem(mediaFile);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteItem(MediaFile mediaFile) {
        Log.d(TAG, "deleteItem: " + mediaFile.getPath());

        File myFile = new File(mediaFile.getPath());
        boolean deleted = myFile.delete();
        Log.d(TAG, "deleteItem status: " + deleted);

        FragmentSwitcher.switchToFragment(this, AllFilesListFragment.newInstance(), R.id.main_activity_placeholder);
    }
}
