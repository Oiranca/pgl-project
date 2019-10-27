package com.oiranca.pglproject.ui.myactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.oiranca.pglproject.R;

public class MyActivityFragment extends Fragment {


    private MyActivityViewModel myActivityViewModel;
    String actadm = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myActivityViewModel =
                ViewModelProviders.of(this).get(MyActivityViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_my_activity, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        myActivityViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("Poner el Nombre del Usuario");
            }
        });


        final CheckBox chkMy = root.findViewById(R.id.checkBoxAdm);
        final CheckBox chkMybis = root.findViewById(R.id.checkBoxAdmBis);

// Aquí pondríamos los datos de las actividades en la base de datos al pulsar el calendario


        CalendarView calendar = root.findViewById(R.id.calendarMactivity);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


                String day = Integer.toString(dayOfMonth);
                String m = Integer.toString(month);
                String years = Integer.toString(year);
                actadm = day + "/" + m + "/" + years;
                chkMy.setText(getString(R.string.first_activity) + actadm);
                chkMybis.setText(getString(R.string.second_activity) + actadm);


            }
        });


        return root;
    }

    /*  private void datePresent() {
        SimpleDateFormat fechForm = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date fechaHoy = new Date();
        present = fechForm.format(fechaHoy);
    }*/

}