package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class FragmentSummaryList extends Fragment {
    RecyclerView recyclerView;

    List<SummaryData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    RecyclerViewAdapter2 mainAdapter;

    public FragmentSummaryList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summary_list, container, false);

        recyclerView = v.findViewById(R.id.recycler_view2);

        database = RoomDB.getInstance(getActivity());
        dataList = database.MainDao().getAll();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        mainAdapter = new RecyclerViewAdapter2(getActivity(), dataList);
        recyclerView.setAdapter(mainAdapter);



        return v;
    }
}
