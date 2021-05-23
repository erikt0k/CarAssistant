package com.example.carassistant.ui.notifications;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NotificationsDB db = new NotificationsDB(v.getContext());
                db.delete(notificationsList.get(position).getId());

                notificationsList.remove(position);  // remove the item from list
                notifyItemRemoved(position); // notify the adapter about the removed item
                NotificationsFragment.updateList();
                Log.e("pos: ", String.valueOf(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public void setArrayMyData(ArrayList<Notifications> arrayMyData) {
        this.notificationsList = arrayMyData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvDate, tvRange;
        Button btDelete, btEdit;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.spending_name);
            tvDate = itemView.findViewById(R.id.spending_type);
            tvRange = itemView.findViewById(R.id.spending_date);
            btDelete = itemView.findViewById(R.id.button_item_delete);
            btEdit = itemView.findViewById(R.id.button_item_edit);
        }
    }
}
