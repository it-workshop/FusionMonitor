package com.technoworks.fusionmonitor.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.technoworks.fusionmonitor.CopterSettings;
import com.technoworks.fusionmonitor.MessagingHelper;

import java.util.LinkedHashSet;

/**
 * Created by loredan on 21.07.14.
 */
public class ChooseSettingsFieldDialogFragment extends DialogFragment
{
    private ChooseSettingsFieldDialogListener mListener;
    private String[] mNamesArray = CopterSettings.getFieldNames().toArray(new String[0]);

    public ChooseSettingsFieldDialogFragment(ChooseSettingsFieldDialogListener listener)
    {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose field to display and edit")
                .setItems(mNamesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mListener.onChoose(mNamesArray[which]);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        ChooseSettingsFieldDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public interface ChooseSettingsFieldDialogListener
    {
        public void onChoose(String name);
    }
}
