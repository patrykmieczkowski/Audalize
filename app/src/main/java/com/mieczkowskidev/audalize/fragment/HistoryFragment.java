package com.mieczkowskidev.audalize.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mieczkowskidev.audalize.API.RestAPI;
import com.mieczkowskidev.audalize.API.RestClient;
import com.mieczkowskidev.audalize.API.RestClientMultipart;
import com.mieczkowskidev.audalize.R;
import com.mieczkowskidev.audalize.adapter.AllFilesListAdapter;
import com.mieczkowskidev.audalize.model.DataResources;
import com.mieczkowskidev.audalize.model.MediaFile;
import com.mieczkowskidev.audalize.model.UserLogin;
import com.mieczkowskidev.audalize.utils.LoginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Patryk Mieczkowski on 11.02.16
 */
public class HistoryFragment extends Fragment {

    public static final String TAG = HistoryFragment.class.getSimpleName();

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle("History");
        }

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareHistoryList();
    }

    private void prepareHistoryList() {
        Log.d(TAG, "prepareHistoryList()");

        RestClient restClient = new RestClient();
        RestAPI restAPI = restClient.getRestAdapter().create(RestAPI.class);

        restAPI.getResources(LoginManager.getTokenFromShared(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DataResources>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError() " + e.getMessage());
                        Log.e(TAG, "onError() " + ((RetrofitError) e).getUrl());

                    }

                    @Override
                    public void onNext(List<DataResources> dataResources) {
                        Log.d(TAG, "onNext");

                        if (dataResources != null && !dataResources.isEmpty()) {
                            Log.d(TAG, "onNext size " + dataResources.size());
                            for (DataResources dataResource : dataResources) {
                                Log.d(TAG, "onNext: " + dataResource.getName());
                            }
                        }
                    }
                });
    }


}
