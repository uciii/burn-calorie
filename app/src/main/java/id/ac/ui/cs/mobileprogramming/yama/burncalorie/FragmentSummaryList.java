package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentSummaryList extends Fragment {
    RecyclerView recyclerView;
    private FragmentActivityList activityList = new FragmentActivityList();

    List<SummaryData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    RecyclerViewAdapter2 mainAdapter;

    String activity, date_time;
    long calorie, second;

    ImageView home, share;

    public FragmentSummaryList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = RoomDB.getInstance(getActivity());
        dataList = database.MainDao().getAll();
        mainAdapter = new RecyclerViewAdapter2(getActivity(), dataList);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            activity = bundle.getString("activity");
            date_time = bundle.getString("date_time");
            calorie = bundle.getInt("calorie");
            second = bundle.getLong("second");

            Log.d("TAG", "cal "+calorie);
            Log.d("TAG", "onCreate: "+second);
            calorie = (calorie * second)/3600;

            Log.d("TAG", "cal2 "+calorie);

            SummaryData data = new SummaryData();
            data.setActivity_name(activity);
            data.setCalorie((int) calorie);
            data.setDate_time(date_time);

            database.MainDao().insert(data);
            dataList.clear();
            dataList.addAll(database.MainDao().getAll());
            mainAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summary_list, container, false);

        home = v.findViewById(R.id.home);
        home.setOnClickListener(f->{
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, activityList)
                    .addToBackStack(null)
                    .commit();
        });
        share = v.findViewById(R.id.share);
        recyclerView = v.findViewById(R.id.recycler_view2);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mainAdapter);

        return v;
    }
}
