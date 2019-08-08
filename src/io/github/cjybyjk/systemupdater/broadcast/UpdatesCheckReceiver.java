package io.github.cjybyjk.systemupdater.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.Set;

import io.github.cjybyjk.systemupdater.R;
import io.github.cjybyjk.systemupdater.helper.UpdatesDbHelper;
import io.github.cjybyjk.systemupdater.model.Update;
import io.github.cjybyjk.systemupdater.service.CheckUpdateService;
import io.github.cjybyjk.systemupdater.utils.NotificationUtils;
import io.github.cjybyjk.systemupdater.utils.SystemInfoUtils;

public class UpdatesCheckReceiver extends BroadcastReceiver {

    private final static String TAG = "UpdatesCheckReceiver";
    // 这个Receiver的通知id
    private final static int NotificationId = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != "CHECK_DONE") {
            Intent checkUpdateService = new Intent(context, CheckUpdateService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(checkUpdateService);
            } else {
                context.startService(checkUpdateService);
            }
        } else {
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            UpdatesDbHelper mUpdatesDbHelper = new UpdatesDbHelper(context);
            List<Update> tUpdatesList = mUpdatesDbHelper.getUpdates();
            Update tUpdate = tUpdatesList.get(0);
            Set<String> tAutoDlSet = mSharedPreferences.getStringSet("auto_download",null);
            if (tAutoDlSet != null) {
                if (tAutoDlSet.contains(SystemInfoUtils.getNetworkType(context))) {
                    downloadUpdate(context, tUpdate);
                    return;
                }
            }
            String title = context.getString(R.string.notification_title_new_update_found);
            String message = tUpdate.getName() + " " + tUpdate.getVersion();
            NotificationUtils.showNotification(context, "update_found", NotificationId, title, message);
        }
    }

    private void downloadUpdate(Context context, Update update) {
        final Intent DownloadManageService = new Intent(context, io.github.cjybyjk.systemupdater.service.DownloadManageService.class);
        DownloadManageService.putExtra("sha1", update.getFileSHA1());
        DownloadManageService.setAction("add_start_resume");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(DownloadManageService);
        } else {
            context.startService(DownloadManageService);
        }
    }

}
