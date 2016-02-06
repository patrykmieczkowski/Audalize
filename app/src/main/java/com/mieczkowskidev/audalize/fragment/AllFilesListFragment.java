package com.mieczkowskidev.audalize.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mieczkowskidev.audalize.R;
import com.mieczkowskidev.audalize.adapter.AllFilesListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Patryk Mieczkowski on 06.02.16.
 */
public class AllFilesListFragment extends Fragment {

    public static final String TAG = AllFilesListFragment.class.getSimpleName();

    public static AllFilesListFragment newInstance() {
        return new AllFilesListFragment();
    }

    @Bind(R.id.all_files_list_recycler)
    RecyclerView allFilesListRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_all_files_list, container, false);
        ButterKnife.bind(this, view);

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

    private void prepareList() {
        Log.d(TAG, "prepareList()");

        List<String> list = new ArrayList<>();
        String[] myArray = new String[]{"asdasd", "ajsdiasd", "uashdasd", "the fourth item"};
        list.addAll(Arrays.asList(myArray));

        AllFilesListAdapter allFilesListAdapter = new AllFilesListAdapter(getActivity(), list);
        allFilesListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        allFilesListRecycler.setAdapter(allFilesListAdapter);

    }
}
