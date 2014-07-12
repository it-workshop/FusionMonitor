package com.technoworks.fusionmonitor.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.technoworks.fusionmonitor.MonitorActivity;

/**
 * Created by Vsevolod on 11.07.2014.
 */
public class SaveLayoutDialogFragment extends DialogFragment
{
    EditText mName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        FrameLayout view = new FrameLayout(getActivity());
        mName = new EditText(getActivity());
        view.addView(mName);

        builder.setTitle("Enter a name for layout")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(((MonitorActivity) getActivity()).saveLayout(mName.getText().toString()))
                            SaveLayoutDialogFragment.this.dismiss();
                        else
                            Toast.makeText(getActivity(), "Layout with this name already exists", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SaveLayoutDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
