package com.example.carassistant.ui.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.R;
import com.example.carassistant.ui.Notifications;
import com.example.carassistant.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.util.Objects.isNull;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    ArrayList<Notifications> notificationsList;
    Context context;


    public NotificationAdapter(ArrayList<Notifications> notificationsList, Context context){
        this.context = context;
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
        SimpleDateFormat simpleDateFormat = null;
        Date date1 = null;
        SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(context);
        Notifications notification = notificationsList.get(position);
        holder.tvName.setText(notification.getName());
        Log.i("len:",String.valueOf(notification.getWay()));
        int range;

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.context, "notify")
                        .setSmallIcon(R.drawable.ic__car_notify)
                        .setContentTitle("CarAssistant")
                        .setContentText("Сработало напоминание "+holder.tvName.getText().toString())
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this.context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Notifs";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        if (notification.getDate().isEmpty()) {
            range = notification.getWay() - pr.getInt("Way", 0);
            if (range<=0) {
                holder.tvInfo.setText("Уже пора!");

                notificationManager.notify(100, builder.build());
            }
            else holder.tvInfo.setText("Осталось " + range + "км");
        }
        else if (notification.getWay()==0){
            simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

            try {
                date1 = simpleDateFormat.parse(notification.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i("sdf:", notification.getDate());
            Log.i("sdf:", String.valueOf(date1));
            Log.i("sdf:", String.valueOf(new Date()));
            Log.i("way", String.valueOf(pr.getInt("Way", 0)));
            if (printDifference(new Date(), date1)<=0) {

                notificationManager.notify(101, builder.build());
            }
            else holder.tvInfo.setText("Осталось " + printDifference(new Date(), date1)+1 + " дней");
            printDifference(date1, new Date());
        } else {
            simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

            try {
                date1 = simpleDateFormat.parse(notification.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i("sdf:", notification.getDate());
            Log.i("sdf:", String.valueOf(date1));
            Log.i("sdf:", String.valueOf(new Date()));
            range = notification.getWay()-pr.getInt("Way", 0);
            if (range<=0 || printDifference(new Date(), date1)<=0 ){

                notificationManager.notify(102, builder.build());
            }
            else holder.tvInfo.setText("Осталось " + printDifference(new Date(), date1) + " дней/"+ range + "км");
            printDifference(date1, new Date());
        }


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

    public int printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;




        return (int) elapsedDays;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvInfo, tvRange;
        Button btDelete, btEdit;
        SharedPreferences pr;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            pr = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            tvName = itemView.findViewById(R.id.spending_name);
            tvInfo = itemView.findViewById(R.id.tv_info_notif);
            tvRange = itemView.findViewById(R.id.spending_date);
            btDelete = itemView.findViewById(R.id.button_item_delete);
            btEdit = itemView.findViewById(R.id.button_item_edit);
        }
    }
}
