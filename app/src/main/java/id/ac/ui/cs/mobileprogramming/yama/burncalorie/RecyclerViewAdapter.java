package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.yama.burncalorie.databinding.ActivityListItemBinding;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Activity> list;
    private OnItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ActivityListItemBinding binding;

        public ViewHolder(@NonNull ActivityListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int pos);
    }

    public RecyclerViewAdapter(List<Activity> list) {
        this.list = list;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public Activity getItemAt(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ActivityListItemBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.activity_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.title.setText(list.get(position).getTitle());
        holder.binding.info.setText(list.get(position).getInfo());
        holder.binding.type.setText(list.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

