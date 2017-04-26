package com.globe.jackbbb95.characters.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by bogle on 4/26/17.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

}
