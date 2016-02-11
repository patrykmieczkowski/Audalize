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
import com.mieczkowskidev.audalize.API.RestClientMultipart;
import com.mieczkowskidev.audalize.R;
import com.mieczkowskidev.audalize.adapter.AllFilesListAdapter;
import com.mieczkowskidev.audalize.model.MediaFile;
import com.mieczkowskidev.audalize.utils.LoginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Patryk Mieczkowski on 06.02.16.
 */
public class AllFilesListFragment extends Fragment {

    public static final String TAG = AllFilesListFragment.class.getSimpleName();
    @Bind(R.id.all_files_list_recycler)
    RecyclerView allFilesListRecycler;
    @Bind(R.id.upload_progress_bar)
    ProgressBar uploadProgressBar;
    @Bind(R.id.upload_counter_text)
    TextView uploadCounterText;
    @Bind(R.id.upload_size_text)
    TextView uploadSizeText;
    @Bind(R.id.upload_layout)
    RelativeLayout uploadLayout;
    List<MediaFile> mediaFileList = new ArrayList<>();

    public static AllFilesListFragment newInstance() {
        return new AllFilesListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_files_list, container, false);
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle("Files List");
        }

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_files_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_synchronize:
                Log.d(TAG, "onOptionsItemSelected: clicked Synchronization");
                if (mediaFileList != null && !mediaFileList.isEmpty()) {
                    startSynchronization();
                } else {
                    Log.e(TAG, "onOptionsItemSelected: sorry, list is empty cannot synchronize");
                }
                break;
        }

        return true;
    }

    private void prepareList() {
        Log.d(TAG, "prepareList()");
        mediaFileList.clear();


        File storageDirAlpha = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File storageDir = new File(storageDirAlpha, "/audalize");
        storageDir.mkdirs();

        for (File f : storageDir.listFiles()) {
            if (f.isFile()) {
                mediaFileList.add(new MediaFile(f.getAbsolutePath(), f.getName(), true));
            }
        }

        if (mediaFileList != null && !mediaFileList.isEmpty()) {
            uploadProgressBar.setScaleY(6f);
//            uploadProgressBar.getProgressDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            uploadProgressBar.setMax(mediaFileList.size());
            String sizeText = "/" + String.valueOf(mediaFileList.size());
            uploadSizeText.setText(sizeText);
            uploadCounterText.setText("1");
            AllFilesListAdapter allFilesListAdapter = new AllFilesListAdapter(getActivity(), mediaFileList);
            allFilesListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            allFilesListRecycler.setAdapter(allFilesListAdapter);
        }
    }

    private void startSynchronization() {
        Log.d(TAG, "startSynchronization(), items count: " + mediaFileList.size());

        uploadLayout.setVisibility(View.VISIBLE);
        final String AuthToken = LoginManager.getTokenFromShared(getActivity());
        RestClientMultipart restClientMultipart = new RestClientMultipart();
        final RestAPI restAPI = restClientMultipart.getRestMultipartAdapter().create(RestAPI.class);

        Observable.from(mediaFileList)
                .flatMap(new Func1<MediaFile, Observable<Response>>() {
                    @Override
                    public Observable<Response> call(MediaFile mediaFile) {
                        Log.d(TAG, "call: " + mediaFile.getTitle());
                        return restAPI.addAudio(AuthToken, new TypedFile("file:", new File(mediaFile.getPath())), mediaFile.getTitle());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");

                        showCompletedDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError() " + e.getMessage());

                    }

                    @Override
                    public void onNext(Response response) {
                        Log.d(TAG, "onNext: " + response.getReason());
                        int size = uploadProgressBar.getProgress() + 1;
                        uploadProgressBar.setProgress(size);
                        uploadCounterText.setText(String.valueOf(size));
                    }
                });

    }

    private void showCompletedDialog() {
        Log.d(TAG, "showCompletedDialog()");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Upload completed!\nVisit http://audalize.stanzas.co to see the analysis!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                        uploadLayout.setVisibility(View.GONE);
                        uploadCounterText.setText("");
                        uploadSizeText.setText("");
                        uploadProgressBar.setProgress(0);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
