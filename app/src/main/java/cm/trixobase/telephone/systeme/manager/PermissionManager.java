package cm.trixobase.telephone.systeme.manager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import cm.trixobase.telephone.R;

/**
 * Created by noumianguebissie on 12/24/18.
 */

public class PermissionManager {

    private static Activity instanceActivity;
    private static View contain;

    private static int REQUEST_CODE_PERMISSION = 100001;

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1758;
    public static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 2575;
    public static final int PERMISSIONS_REQUEST_WRITE_CALL_LOG = 3475;
    public static final int PERMISSIONS_REQUEST_CALL_PHONE = 4857;

    public static class Builder {

        protected Builder(Activity activity) {
            instanceActivity = activity;
        }

        public Builder withView(View view) {
            contain = view;
            return this;
        }

        public boolean checkRequiredPermissions(String permission) {
            switch (permission) {
                case Manifest.permission.CALL_PHONE:
                    REQUEST_CODE_PERMISSION = PERMISSIONS_REQUEST_CALL_PHONE;
                    break;
                case Manifest.permission.READ_CONTACTS:
                    REQUEST_CODE_PERMISSION = PERMISSIONS_REQUEST_READ_CONTACTS;
                    break;
                case Manifest.permission.WRITE_CONTACTS:
                    REQUEST_CODE_PERMISSION = PERMISSIONS_REQUEST_WRITE_CONTACTS;
                    break;
                case Manifest.permission.WRITE_CALL_LOG:
                    REQUEST_CODE_PERMISSION = PERMISSIONS_REQUEST_WRITE_CALL_LOG;
                    break;
            }
            return checkPermission(permission);
        }
    }

    public static Builder builder(Activity activity) {
        return new Builder(activity);
    }

    private static boolean checkPermission(String permission) {
        boolean answer = true;
        if (ActivityCompat.checkSelfPermission(instanceActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            answer = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && instanceActivity.shouldShowRequestPermissionRationale(permission))
                explainPermission(instanceActivity, contain, permission);
            else askForPermission(instanceActivity, permission);
        }
        return answer;
    }

    public static void explainPermission(final Activity activity, View view, final String permission) {
        Snackbar.make(view, activity.getBaseContext().getString(R.string.error_permission), Snackbar.LENGTH_LONG).setAction(activity.getBaseContext().getString(R.string.label_text_activate), new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                askForPermission(activity, permission);
            }
        }).show();
    }

    private static void askForPermission(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            activity.requestPermissions(new String[] { permission }, REQUEST_CODE_PERMISSION);
    }

}
