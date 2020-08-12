package com.teknosols.a3orrsy.other.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerForDOB extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    DateSetListner dateSetListner;

    public interface DateSetListner{
        void onDateSet(android.widget.DatePicker view, int year, int month, int day);
    }

    public void setDateSetListner(DateSetListner dateSetListner){
        this.dateSetListner= dateSetListner;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
//        DatePickerDialog dialog= new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog, this, year,month, day);
//        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);
//        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
//        datePickerDialog.show();
        return datePickerDialog;
        // Create a new instance of DatePickerDialog and return it
//        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (dateSetListner != null)
            dateSetListner.onDateSet(view, year,month,day);

    }
}