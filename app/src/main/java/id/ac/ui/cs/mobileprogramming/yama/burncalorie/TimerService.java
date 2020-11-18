package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

public class TimerService extends Service {
    private boolean isRunning = false;
    private long second = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        while(isRunning){
            sleepSecond();
            second++;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        second = 0;
        super.onDestroy();
    }

    private void sleepSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignore) {}
    }

    public boolean getIsRunning(){
        return isRunning;
    }

    public long getSecond(){
        return second;
    }
}
