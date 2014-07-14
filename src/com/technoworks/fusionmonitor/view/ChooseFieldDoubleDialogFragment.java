package com.technoworks.fusionmonitor.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.technoworks.fusionmonitor.MessagingHelper;
import com.technoworks.fusionmonitor.view.widgets.Widget;

import java.util.LinkedHashMap;

/**
 * Created by Vsevolod on 13.07.2014.
 * Dialog for choosing a telemetry field to display.
 * Listener returns field signature.
 */
public class ChooseFieldDoubleDialogFragment extends DialogFragment
{
    private ChooseFieldDoubleDialogListener mListener;
    private LinkedHashMap<String, Integer> mNames = new LinkedHashMap<String, Integer>();
    String[] mNamesArray;

    public ChooseFieldDoubleDialogFragment(ChooseFieldDoubleDialogListener listener)
    {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        for(int key : MessagingHelper.DOUBLES.keySet())
            mNames.put(MessagingHelper.DOUBLES.get(key).mName, key);

        mNamesArray = mNames.keySet().toArray(new String[0]);

        builder.setTitle("Choose field to display")
                .setItems(mNamesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mListener.onChoose(mNames.get(mNamesArray[which]));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ChooseFieldDoubleDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public interface ChooseFieldDoubleDialogListener
    {
        public void onChoose(int signature);
    }
}
