package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentBurning extends Fragment {
    Button back, start, pause, reset, calc;
    TextView time;

    private FragmentSummaryList fragmentSummaryList = new FragmentSummaryList();

    public FragmentBurning() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_burning, container, false);

        back = v.findViewById(R.id.back);
        back.setOnClickListener(f2 ->{
            getActivity().onBackPressed();
        });

        start = v.findViewById(R.id.start);

        pause = v.findViewById(R.id.pause);

        reset = v.findViewById(R.id.reset);

        calc = v.findViewById(R.id.calculate);
        calc.setOnClickListener(f2 ->{
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentSummaryList)
                    .addToBackStack(null)
                    .commit();
        });

        time = v.findViewById(R.id.time);



        return v;
    }
}
