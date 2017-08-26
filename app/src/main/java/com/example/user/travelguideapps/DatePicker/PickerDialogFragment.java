package com.example.user.travelguideapps.DatePicker;

/**
 * Created by User on 7/27/2017.
 */

import android.content.DialogInterface;

import com.example.user.travelguideapps.LocationDetails.Time_Selection_Fragment;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment;
public class PickerDialogFragment extends TimeDurationPickerDialogFragment {
    private long durationtime;
    private DialogInterface.OnDismissListener onDismissListener;

    @Override
    protected long getInitialDuration() {
        Time_Selection_Fragment.setPositive(false);

        return 0;
    }


    @Override
    protected int setTimeUnits() {
        return TimeDurationPicker.HH_MM;
    }



    @Override
    public void onDurationSet(TimeDurationPicker view, long duration) {
        durationtime=duration;
        Time_Selection_Fragment.setDuration(duration/1000);
        Time_Selection_Fragment.setPositive(true);


      //  DurationToast.show(getActivity(), duration);
    }
public long getDuration(){

    return durationtime;
}



    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

}