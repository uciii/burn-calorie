package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {
    private List<SummaryData> summaryData;
    private Context context;
    private RoomDB db;

    public RecyclerViewAdapter2(Context context, List<SummaryData> summaryData){
        this.context = context;
        this.summaryData = summaryData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summary_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SummaryData data = summaryData.get(position);
        db = RoomDB.getInstance(context);

        holder.date_time.setText(data.getDate_time());
        holder.activity.setText(data.getActivity_name());
        holder.calorie.setText(String.valueOf(data.getCalorie()));

        holder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SummaryData d = summaryData.get(holder.getAdapterPosition());

                db.MainDao().delete(d);

                int pos= holder.getAdapterPosition();
                summaryData.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, summaryData.size());
            }
        });



    }

    @Override
    public int getItemCount() {
        return summaryData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date_time, activity, calorie;
        ImageView btDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_time = itemView.findViewById(R.id.date_time);
            activity = itemView.findViewById(R.id.activity_name);
            calorie = itemView.findViewById(R.id.calorie);
            btDel = itemView.findViewById(R.id.delete);
        }
    }
}
