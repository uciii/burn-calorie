package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentSummaryList extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter2 mainAdapter;

    String activity, date_time;
    long calorie, second;
    private FragmentActivityList activityList = new FragmentActivityList();

    List<SummaryData> dataList = new ArrayList<>();
    RoomDB database;

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

            calorie = calcCalorie(calorie, second);
            
            SummaryData data = new SummaryData();
            data.setActivity_name(activity);
            data.setCalorie((int) calorie);
            data.setDate_time(date_time);

            new insertDataAsyncTask().execute(data);
            mainAdapter.notifyDataSetChanged();
        }
    }

    public native long calcCalorie(long calorie, long second);

    static {
        System.loadLibrary("calorie-calculator");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summary_list, container, false);

        home = v.findViewById(R.id.home);
        home.setOnClickListener(f -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, activityList)
                    .addToBackStack(null)
                    .commit();
        });

        share = v.findViewById(R.id.share);
        share.setOnClickListener(f2 -> {
            if(ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestStoragePermission();
            else
                shareButton();
        });

        recyclerView = v.findViewById(R.id.recycler_view2);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mainAdapter);

        return v;
    }

    private void requestStoragePermission() {
        Resources res = getResources();
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(res.getString(R.string.permission))
                    .setMessage(res.getString(R.string.permissionDetail))
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shareButton() {
        View view1 = getActivity().getWindow().getDecorView().getRootView();
        view1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
        view1.setDrawingCacheEnabled(false);

        String filePath = Environment.getExternalStorageDirectory() + "/Download/" +
                Calendar.getInstance().getTime().toString() + ".jpg";
        File fileScreenshot = new File(filePath);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileScreenshot);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        onShareOnePhoto(fileScreenshot);
    }

    private void onShareOnePhoto(File file) {
        Uri path = FileProvider.getUriForFile(getActivity(),
                "id.ac.ui.cs.mobileprogramming.yama.burncalorie", file);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, "My Current Activity");
        shareIntent.putExtra(Intent.EXTRA_STREAM, path);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share..."));
    }

    private class insertDataAsyncTask extends AsyncTask<SummaryData, Void, Void> {
        @Override
        protected Void doInBackground(SummaryData... summaryData) {
            database.MainDao().insert(summaryData[0]);
            dataList.clear();
            dataList.addAll(database.MainDao().getAll());
            return null;
        }
    }

}
