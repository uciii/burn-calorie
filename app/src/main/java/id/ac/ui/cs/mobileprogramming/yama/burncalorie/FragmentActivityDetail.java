package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.ac.ui.cs.mobileprogramming.yama.burncalorie.databinding.FragmentActivityDetailBinding;

public class FragmentActivityDetail extends Fragment {
    private FragmentActivityDetailBinding binding;

    public FragmentActivityDetail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getSelected().observe(getViewLifecycleOwner(), item -> {
            String title = item.getTitle();
            String calorie = "Burn " + item.getCalorie() + " Calories / hour";
            String desc = item.getDescription();
            String page = "Activity Detail";
            String type = item.getType();
            String url = item.getUrl();

            binding.pageTitle.setText(page);
            binding.description.setText(desc);
            binding.title.setText(title);
            binding.type.setText(type);
            binding.url.setText(url);
            binding.calorie.setText(calorie);
        });
    }
}