package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Activity> selected = new MutableLiveData<>();

    public void setSelected(Activity activity) {
        selected.setValue(activity);
    }

    public MutableLiveData<Activity> getSelected() {
        return selected;
    }
}
