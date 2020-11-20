package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.ac.ui.cs.mobileprogramming.yama.burncalorie.databinding.FragmentActivityListBinding;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.parseInt;

public class FragmentActivityList extends Fragment {
    private FragmentActivityListBinding binding;
    private FragmentActivityDetail detailActivity = new FragmentActivityDetail();

    private Button language;
    private Resources res;

    public FragmentActivityList(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_activity_list, container,
                false);

        language = binding.getRoot().findViewById(R.id.lang);
        loadLocale();

        language.setOnClickListener(f->{
            String temp = language.getText().toString();
            if(temp.equals("EN")){
                setLocale("in");
                language.setText("ID");
                getActivity().recreate();
            }
            else{
                setLocale("en");
                language.setText("EN");
                getActivity().recreate();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        res = getResources();
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Activity> list = new ArrayList<>();
        String[] act1 = res.getStringArray(R.array.act1);
        String[] act2 = res.getStringArray(R.array.act2);
        String[] act3 = res.getStringArray(R.array.act3);
        list.add(new Activity(act1[0], act1[1], act1[2], act1[3], parseInt(act1[4])));
        list.add(new Activity(act2[0], act2[1], act2[2], act2[3], parseInt(act2[4])));
        list.add(new Activity(act3[0], act3[1], act3[2], act3[3], parseInt(act3[4])));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
        binding.recyclerView.setAdapter(adapter);
        adapter.setListener((v, position) -> {
            viewModel.setSelected(adapter.getItemAt(position));
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, detailActivity)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getActivity().getBaseContext().getResources().updateConfiguration
                (config, getActivity().getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SETTING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", lang);
        editor.apply();
    }

    private void loadLocale(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SETTING", MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang", "");
        if(lang.equals("en")){
            language.setText("EN");
        }
        else{
            language.setText("ID");
        }
        setLocale(lang);
    }
}