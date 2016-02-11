package com.mieczkowskidev.audalize.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mieczkowskidev.audalize.API.RestAPI;
import com.mieczkowskidev.audalize.API.RestClient;
import com.mieczkowskidev.audalize.LoginActivity;
import com.mieczkowskidev.audalize.R;
import com.mieczkowskidev.audalize.model.DataProfile;
import com.mieczkowskidev.audalize.utils.LoginManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.client.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Patryk Mieczkowski on 06.02.16.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = ProfileFragment.class.getSimpleName();

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Bind(R.id.textViewName)
    TextView usernameText;

    @Bind(R.id.textViewMail)
    TextView mailText;

    @Bind(R.id.logout_button)
    Button logoutButton;

    @Bind(R.id.delete_button)
    Button deleteButton;

    Subscription subscriptionProfile, subscriptionLogout, subscriptionDelete;
    RestAPI restAPI;

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

        createRestClient();
        getProfileInfo();
    }

    private void createRestClient() {
        Log.d(TAG, "createRestClient()");

        RestClient restClient = new RestClient();
        restAPI = restClient.getRestAdapter().create(RestAPI.class);
    }

    private void getProfileInfo() {
        Log.d(TAG, "getProfileInfo()");

        subscriptionProfile = restAPI.getProfile(LoginManager.getTokenFromShared(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataProfile>() {
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
                        usernameText.setText(dataProfile.getUsername());
                        mailText.setText(dataProfile.getEmail());
                    }
                });
    }

    @OnClick(R.id.logout_button)
    public void logoutUserDialog() {
        Log.d(TAG, "logoutUserDialog()");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to logout?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        logoutUser();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void logoutUser() {
        Log.d(TAG, "logoutUser()");

        subscriptionLogout = restAPI.logoutUser(LoginManager.getTokenFromShared(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError() " + e.getMessage());
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.d(TAG, "onNext()");

                    }
                });
    }

    @OnClick(R.id.delete_button)
    public void deleteUserDialog() {
        Log.d(TAG, "deleteUserDialog()");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete an account and all the erase all the data from server?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        deleteUser();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteUser() {
        Log.d(TAG, "deleteUser()");

        subscriptionDelete = restAPI.unregisterUser(LoginManager.getTokenFromShared(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        Log.d(TAG, "call() " + response.getReason());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "call" + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() ProfileFragment");
        super.onDestroy();

        if (subscriptionProfile != null && !subscriptionProfile.isUnsubscribed()) {
            subscriptionProfile.unsubscribe();
        }

        if (subscriptionLogout != null && !subscriptionLogout.isUnsubscribed()) {
            subscriptionLogout.unsubscribe();
        }

        if (subscriptionDelete != null && !subscriptionDelete.isUnsubscribed()) {
            subscriptionDelete.unsubscribe();
        }
    }
}
