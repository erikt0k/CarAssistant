package com.example.carassistant.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.R;
import com.example.carassistant.ui.Notifications;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    ArrayList<Notifications> notificationsList;


    public NotificationAdapter(ArrayList<Notifications> notificationsList){

        this.notificationsList = notificationsList;
    }
    @NonNull
    @NotNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }
    @Override
    public int getItemViewType(final int position) {
        return R.layout.notification_item;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationAdapter.ViewHolder holder, int position) {
        Notifications notification = notificationsList.get(position);
        holder.tvName.setText(notification.getName());
        holder.tvDate.setText("Осталось " + notification.getDate() + "дней");
        holder.tvRange.setText("Осталось " + notification.getWay() + "км");
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDate, tvRange;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.notif_name);
            tvDate = itemView.findViewById(R.id.notif_date);
            tvRange = itemView.findViewById(R.id.notif_range);
        }
    }
}
