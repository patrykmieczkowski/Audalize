package com.mieczkowskidev.audalize.utils;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Patryk Mieczkowski on 06.02.16
 */
public class FragmentSwitcher {

    public static void switchToFragment(AppCompatActivity activity, Fragment fragment, int placeHolderResource) {

        if (activity.getApplicationContext() != null && fragment != null) {

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(placeHolderResource, fragment)
                    .commit();
        }

    }
}
