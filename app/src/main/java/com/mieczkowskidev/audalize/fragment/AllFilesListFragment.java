package com.mieczkowskidev.audalize.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mieczkowskidev.audalize.R;

/**
 * Created by Patryk Mieczkowski on 06.02.16.
 */
public class AllFilesListFragment extends Fragment {

    public static AllFilesListFragment newInstance() {
        return new AllFilesListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_all_files_list, container, false);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setTitle("Files List");
        }

        return view;
    }
}
