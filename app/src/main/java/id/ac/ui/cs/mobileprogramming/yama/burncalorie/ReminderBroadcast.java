package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyWorkOut")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getResources().getString(R.string.notifTitle))
                .setContentText(context.getResources().getString(R.string.notifText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200, builder.build());
    }
}
