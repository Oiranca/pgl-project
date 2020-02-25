package com.oiranca.pglproject.ui.tabfamily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.oiranca.pglproject.R;
import com.oiranca.pglproject.ui.myactivity.MyActivityFragment;
import com.oiranca.pglproject.ui.profile.ProfileFragment;
import com.oiranca.pglproject.ui.reportmonth.ReportMonthFragment;
import com.oiranca.pglproject.ui.reports.ReportsFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    static Fragment newInstance(int index) {
        Fragment fragment=null;


        switch (index){
            case 1:
                fragment = new MyActivityFragment();
                break;
            case 2:
                fragment= new ReportsFragment();
                break;
            case 3:
                fragment= new ProfileFragment();
                break;

        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PageViewModel pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_family, container, false);

        return root;
    }
}