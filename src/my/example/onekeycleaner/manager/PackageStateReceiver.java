package my.example.onekeycleaner.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import my.example.onekeycleaner.util.Constants;

public class PackageStateReceiver extends BroadcastReceiver {

    private static final String TAG = "PackageStateReceiver";
    private static final boolean DEBUG =  Constants.IS_DEBUG && true;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String packageName = intent.getDataString();
        packageName = packageName.substring(packageName.indexOf(':') + 1);

        if (DEBUG) {
            Log.d(TAG, "onReceive Action: " + action + " PackageName: " + packageName);
        }

        if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {

            AppInstallManager.getInstance(context).addPackage(packageName);

        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {

            AppInstallManager.getInstance(context).removePackage(packageName);

        } else if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {

            AppInstallManager.getInstance(context).updatePackage(packageName);

        } else if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) {

            AppInstallManager.getInstance(context).updatePackage(packageName);

        }
    }
}
