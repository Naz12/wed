package com.teknosols.a3orrsy.other.util;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public  class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TimeSetListner timeSetListner;
    public interface TimeSetListner{
        void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute);
    }
    public void setTimeSetListner(TimeSetListner timeSetListner){
        this.timeSetListner= timeSetListner;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, hour, minute , false);
    }

    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        if (timeSetListner != null)
            timeSetListner.onTimeSet(view, hourOfDay,minute);

    }


}