package com.technoworks.fusionmonitor.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.technoworks.fusionmonitor.MonitorActivity;

import java.util.ArrayList;

/**
 * Created by Vsevolod on 11.07.2014.
 */
public class RemoveLayoutDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final ArrayList<Integer> toRemove = new ArrayList<Integer>();
        final String[] layouts = ((MonitorActivity) getActivity()).mLayouts.getAll().keySet().toArray(new String[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select layouts to remove")
                .setMultiChoiceItems(layouts, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
                    {
                        if(isChecked)
                            toRemove.add(which);
                        else
                            toRemove.remove(Integer.valueOf(which));
                    }
                })
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SharedPreferences.Editor editor = ((MonitorActivity) getActivity()).mLayouts.edit();
                        for(Integer index : toRemove)
                        {
                            editor.remove(layouts[index]);
                        }
                        editor.commit();
                        RemoveLayoutDialogFragment.this.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        RemoveLayoutDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
