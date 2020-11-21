package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class FragmentBurning extends Fragment {
    Activity activity;
    boolean isStop = true;
    double currTime = -0.00001; // compute current time
    long second = -1; // storing previous time in second
    Timer timer;
    TimerTask timerTask;

    Button start, pause, reset, calc;
    ImageView back;
    TextView title, time;
    View v;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ACTIVITY = "activity";
    public static final String PAUSETIME = "pausetime";
    public static final String SUTIME = "sumtime";
    public static final String ISSTOP = "isstop";
    public static final String ISEXPIRED = "isexpired";

    private FragmentSummaryList fragmentSummaryList;

    public FragmentBurning() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer = new Timer();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_burning, container, false);

        title = v.findViewById(R.id.title);
        time = v.findViewById(R.id.time);

        fragmentSummaryList = new FragmentSummaryList();

        back = v.findViewById(R.id.back);
        back.setOnClickListener(f2 -> {
            if (timerTask != null) timerTask.cancel();
            updateSharedPreference();
            second = 0;
            currTime = 0.0;
            getActivity().onBackPressed();
        });

        start = v.findViewById(R.id.start);
        start.setOnClickListener(f3 -> {
            updateSharedPreference();
            if (isStop) {
                //start
                getActivity().startService(new Intent(getActivity(), MusicService.class));
                if (second == -1 && currTime < 0) {
                    isStop = false;
                    second = 0;
                    currTime = 0.0;
                    startTimer();
                }
                //resume
                else {
                    isStop = false;
                    startTimer();
                }
            }
        });

        pause = v.findViewById(R.id.pause);
        pause.setOnClickListener(f4 -> {
            if (!isStop) {
                isStop = true;
                timerTask.cancel();
                updateSharedPreference();
                getActivity().stopService(new Intent(getActivity(), MusicService.class));
            }
        });

        reset = v.findViewById(R.id.reset);
        reset.setOnClickListener(f5 -> {
            if (timerTask != null) timerTask.cancel();
            isStop = true;
            currTime = 0.0;
            second = 0;
            getActivity().stopService(new Intent(getActivity(), MusicService.class));
            updateSharedPreference();
            setTextTime(0, 0, 0);
        });

        calc = v.findViewById(R.id.calculate);
        calc.setOnClickListener(f6 -> {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            if (currTime < 0) {
                currTime = 0;
            }

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean(ISEXPIRED, true);
            editor.putBoolean(ISSTOP, true);
            editor.putLong(PAUSETIME, 0);
            editor.putLong(SUTIME, 0);

            Bundle bundle = new Bundle();
            bundle.putString("activity", activity.getTitle());
            bundle.putString("date_time", formatter.format(date));
            bundle.putInt("calorie", activity.getCalorie());
            bundle.putLong("second", second + (Math.round(currTime) % 86400));
            fragmentSummaryList.setArguments(bundle);

            getActivity().stopService(new Intent(getActivity(), MusicService.class));
            if (timerTask != null) timerTask.cancel();
            isStop = true;
            second = 0;
            currTime = 0.0;

            boolean x = editor.commit();

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentSummaryList)
                    .commit();
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currTime++;
                        updateView();
                    }
                });
                updateSharedPreference();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void updateView() {
        if (!isStop || second > 0) {
            long sec = second + (Math.round(currTime) % 86400);
            long hour = sec / 3600;
            sec %= 3600;
            long min = sec / 60;
            sec %= 60;
            setTextTime(hour, min, sec);
        } else setTextTime(0, 0, 0);
    }

    private void setTextTime(long hour, long min, long sec) {
        String S = String.format("%02d : %02d : %02d", hour, min, sec);
        time.setText(S);
    }

    private void loadData() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String json = sharedPreferences.getString(ACTIVITY, "");
        activity = gson.fromJson(json, Activity.class);

        long pauseTime = sharedPreferences.getLong(PAUSETIME, -1);

        if (pauseTime <= 0) second = sharedPreferences.getLong(SUTIME, -1);
        else
            second = sharedPreferences.getLong(SUTIME, -1) +
                    (System.currentTimeMillis() - pauseTime) / 1000;

        isStop = sharedPreferences.getBoolean(ISSTOP, true);

        if (sharedPreferences.getBoolean(ISEXPIRED, true)) {
            SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
            viewModel.getSelected().observe(getViewLifecycleOwner(), item -> {
                activity = item;
                title.setText(activity.getTitle());
            });
        } else {
            title.setText(activity.getTitle());
            Toast.makeText(this.getActivity(), getResources().getString(R.string.warning), Toast.LENGTH_SHORT).show();
            updateView();
        }

        if (!isStop) startTimer();
        else updateView();
    }

    private void updateSharedPreference() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(activity);
        editor.putString(ACTIVITY, json);

        if (second + (Math.round(currTime) % 86400) < 0) editor.putLong(SUTIME, 0);
        else editor.putLong(SUTIME, second + (Math.round(currTime) % 86400));

        editor.putBoolean(ISSTOP, isStop);

        if (isStop) editor.putLong(PAUSETIME, 0);
        else editor.putLong(PAUSETIME, System.currentTimeMillis());

        editor.putBoolean(ISEXPIRED, false);
        editor.apply();
    }

}