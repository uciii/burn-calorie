package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import id.ac.ui.cs.mobileprogramming.yama.burncalorie.databinding.FragmentActivityDetailBinding;

public class FragmentActivityDetail extends Fragment {
    private FragmentActivityDetailBinding binding;
    private FragmentBurning fragmentBurning = new FragmentBurning();
    Button remind, start;
    ImageView back;

    private Resources res;

    public FragmentActivityDetail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createNotifyChannel();
        res = getResources();
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_activity_detail,
                container,
                false);
        View v = binding.getRoot();

        remind = v.findViewById(R.id.remind);
        remind.setOnClickListener(f -> {
            Intent intent = new Intent(getActivity(), ReminderBroadcast.class);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            long buttonClicked = System.currentTimeMillis();
            long oneMinuteInMillis = 1000 * 10;

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    buttonClicked + oneMinuteInMillis,
                    pendingIntent);

            Toast.makeText(this.getActivity(), res.getString(R.string.reminderToast), Toast.LENGTH_SHORT).show();
        });

        start = v.findViewById(R.id.start);
        start.setOnClickListener(f3 -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentBurning)
                    .addToBackStack(null)
                    .commit();
        });

        back = v.findViewById(R.id.back);
        back.setOnClickListener(f2 -> {
            getActivity().onBackPressed();
        });

        return v;
    }

    private void createNotifyChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "BurnCalorieReminderChannel";
            String description = "Channel for Burn Out Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel =
                    new NotificationChannel("notifyWorkOut", name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager =
                    this.getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(), item -> {
            String title = item.getTitle();
            String calorie = res.getString(R.string.burn)
                    +" "+ item.getCalorie() +" "+ res.getString((R.string.cal_hour));
            String desc = item.getDescription();
            String type = item.getType();
            String url = item.getUrl();

            binding.description.setText(desc);
            binding.title.setText(title);
            binding.type.setText(type);
            binding.url.setText(url);
            binding.calorie.setText(calorie);
        });

    }
}