package com.oiranca.pglproject.ui.family;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oiranca.pglproject.R;


public class NewFamily extends Fragment {

    private NewFamilyViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(NewFamilyViewModel.class);
        View root = inflater.inflate(R.layout.new_family_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_newf);
        mViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("Poner el Nombre del Usuario");
            }
        });
        return root;
    }


}
