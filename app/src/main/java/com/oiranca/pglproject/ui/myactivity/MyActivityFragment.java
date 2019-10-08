package com.oiranca.pglproject.ui.myactivity;

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

public class MyActivityFragment extends Fragment {

    private MyActivityViewModel myActivityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myActivityViewModel =
                ViewModelProviders.of(this).get(MyActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_activity, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        myActivityViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("Poner el Nombre del Usuario");
            }
        });
        return root;
    }
}