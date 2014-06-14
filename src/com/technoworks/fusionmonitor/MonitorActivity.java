package com.technoworks.fusionmonitor;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.technoworks.fusionmonitor.widgets.TextWidget;
import com.technoworks.fusionmonitor.widgetSystem.WidgetsManager;

/**
 * Created by lgor on 14.06.14.
 */
public class MonitorActivity extends Activity {

    private WidgetsManager mWidgets = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        RelativeLayout rootLayout = new RelativeLayout(this);
        ShapeDrawable background = new ShapeDrawable(new RectShape());
        background.getPaint().setColor(0xFFAAAAAA);
        rootLayout.setBackground(background);
        setContentView(rootLayout);

        if (mWidgets == null) {
            mWidgets = new WidgetsManager();
        }
        mWidgets.reload(this, rootLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(1).setTitle(getEditModeStatus());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_empty:
                addWidget();
                break;
            case R.id.edit_mode:
                mWidgets.mEditMode = !mWidgets.mEditMode;
                item.setTitle(getEditModeStatus());
                break;
            default:
                throw new Error("Unknown menu option");
        }
        return true;
    }

    private void addWidget() {
        if (!mWidgets.addWidget(new TextWidget())){
            Toast.makeText(this, "No place for new widget", Toast.LENGTH_SHORT).show();
        }
    }

    private String getEditModeStatus() {
        return mWidgets.mEditMode ? "edit mode on" : "edit mode off";
    }
}
