package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.yama.burncalorie.databinding.FragmentActivityListBinding;

public class FragmentActivityList extends Fragment {
    private FragmentActivityListBinding binding;
    private FragmentActivityDetail detailActivity = new FragmentActivityDetail();

    private FragmentActivityList(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_list, container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Activity> list = new ArrayList<>();
        list.add(new Activity("title1", "desc1", "Outdoor1", "www1", 100));
        list.add(new Activity("title2", "desc2", "Outdoor2", "www2", 200));
        list.add(new Activity("title3", "desc3", "Outdoor3", "www3", 300));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
        binding.recyclerView.setAdapter(adapter);
        adapter.setListener((v, position) -> {
            viewModel.setSelected(adapter.getItemAt(position));
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}