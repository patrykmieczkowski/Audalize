package com.mieczkowskidev.audalize.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mieczkowskidev.audalize.API.RestAPI;
import com.mieczkowskidev.audalize.API.RestClient;
import com.mieczkowskidev.audalize.R;
import com.mieczkowskidev.audalize.model.DataProfile;
import com.mieczkowskidev.audalize.utils.LoginManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Patryk Mieczkowski on 06.02.16.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = ProfileFragment.class.getSimpleName();

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Bind(R.id.textViewName)
    TextView textView;

    Subscription subscriptionProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle("Profile");
        }

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getProfileInfo();
    }

    private void getProfileInfo() {
        Log.d(TAG, "getProfileInfo()");

        RestClient restClient = new RestClient();
        RestAPI restAPI = restClient.getRestAdapter().create(RestAPI.class);

        subscriptionProfile = restAPI.getProfile(LoginManager.getTokenFromShared(getActivity()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataProfile>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError :( " + e.getMessage());

                    }

                    @Override
                    public void onNext(DataProfile dataProfile) {
                        Log.d(TAG, "onNext DataProfile");
                        textView.setText(dataProfile.getUsername());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscriptionProfile != null) {
            if (!subscriptionProfile.isUnsubscribed()) {
                subscriptionProfile.unsubscribe();
            }
        }
    }
}
