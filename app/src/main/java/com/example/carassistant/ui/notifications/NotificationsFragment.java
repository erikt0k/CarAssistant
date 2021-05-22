package com.example.carassistant.ui.notifications;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.R;
import com.example.carassistant.ui.DatePickerFragment;
import com.example.carassistant.ui.Notifications;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;

public class NotificationsFragment extends Fragment {
    static NotificationsDB db;
    ArrayList<Notifications> notifList, bobik;
    RecyclerView rvNotifications;
    RecyclerView.LayoutManager layoutManager;
    NotificationAdapter notificationAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db = new NotificationsDB(this.getContext());
        notifList = db.selectAll();
        bobik = notifList;
        View v = inflater.inflate(R.layout.fragment_notifications, null);
        rvNotifications = v.findViewById(R.id.notif_rv);
        rvNotifications.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
        rvNotifications.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(v.getContext());
        rvNotifications.setLayoutManager(layoutManager);
        System.out.println(bobik);
        notificationAdapter = new NotificationAdapter(bobik);
        rvNotifications.setAdapter(notificationAdapter);


        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        FloatingActionButton addNotifButton = v.findViewById(R.id.add_notif_but);
        addNotifButton.setOnClickListener(view -> {
            AddNotificationFragment dialog = new AddNotificationFragment();
            dialog.show(getChildFragmentManager(), "custom");
        });
        return v;
    }

    public static void dbInput(Notifications nt){
        db.insert(nt.getName(), nt.getDate(), nt.getWay(), nt.getRepeatCounter(), nt.getRepeatType());

    }





    public static class AddNotificationFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        EditText etDate, etName, etRange;
        long notifId;
        @NotNull
        @Override
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.add_notif_fragment, null);
            etDate = v.findViewById(R.id.edit_notif_date);
            etName=v.findViewById(R.id.edit_notif_name);
            etRange=v.findViewById(R.id.notif_range);
            builder.setView(v)
                    // Add action buttons
                    .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Notifications nt = new Notifications(0, etName.getText().toString(), etDate.getText().toString(), 3, 1, 1);
                            dbInput(nt);
                        }
                    })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            etDate.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.setTargetFragment(AddNotificationFragment.this, 0);
                    datePicker.show(getFragmentManager(), "date picker");
                }
            });

            return builder.create();
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDate = DateFormat.getDateInstance().format(c.getTime());

            //dates = Integer.toString(dayOfMonth);
            //months = Integer.toString(month);
            //years = Integer.toString(year);

            etDate.setText(currentDate);
        }
    }
}