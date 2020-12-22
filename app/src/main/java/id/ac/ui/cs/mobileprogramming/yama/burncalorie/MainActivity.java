package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class MainActivity extends AppCompatActivity {

    private FragmentActivityList foodListFragment = new FragmentActivityList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions
                (this, new String[]{ACCESS_NETWORK_STATE},
                        PackageManager.PERMISSION_GRANTED);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, foodListFragment)
                .commit();
    }
}