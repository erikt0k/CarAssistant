package com.example.carassistant.ui.notifications;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.R;
import com.example.carassistant.ui.DatePickerFragment;
import com.example.carassistant.ui.Notifications;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class NotificationsFragment extends Fragment {
    static NotificationsDB db;
    static ArrayList<Notifications> notifList;
    static RecyclerView rvNotifications;
    RecyclerView.LayoutManager layoutManager;
    static NotificationAdapter notificationAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        db = new NotificationsDB(this.getContext());
        notifList = db.selectAll();
        final ViewGroup nullParent = null;
        View v = inflater.inflate(R.layout.fragment_notifications, nullParent);
        rvNotifications = v.findViewById(R.id.notif_rv);
        rvNotifications.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
        rvNotifications.setHasFixedSize(true);
        rvNotifications.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(v.getContext());
        rvNotifications.setLayoutManager(layoutManager);
        notificationAdapter = new NotificationAdapter(db.selectAll(), getContext());
        rvNotifications.setAdapter(notificationAdapter);



        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), s -> {

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
    static void updateList() {
        notificationAdapter.setArrayMyData(db.selectAll());
        notificationAdapter.notifyDataSetChanged();
    }





    public static class AddNotificationFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        EditText etDate, etName, etRange, etRepeatValue;
        TextView tvAlert;
        Spinner spinnerType;
        @NotNull
        @Override
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.add_notif_fragment, null);
            tvAlert = v.findViewById(R.id.tv_alert_notif);
            etDate = v.findViewById(R.id.edit_spending_date);
            etName=v.findViewById(R.id.edit_spending_name);
            etRange=v.findViewById(R.id.edit_notif_range);
            etRepeatValue = v.findViewById(R.id.edit_spending_money);
            spinnerType = v.findViewById(R.id.type_spinner);

            builder.setView(v)
                    // Add action buttons
                    .setPositiveButton("Сохранить", null)
                    .setNegativeButton("Отмена", (dialog1, id) -> dialog1.cancel());
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            if (etName.getText().toString().isEmpty()){
                                tvAlert.setText("Заполните поле 'Наименование'!");
                            }
                            else if (etDate.getText().toString().isEmpty()&&etRange.getText().toString().isEmpty()){
                                tvAlert.setText("Заполните дату/расстояние!");

                            } else {
                                byte rangeType;
                                int range, repeat;
                                if (spinnerType.getSelectedItem().toString().equals("километров")) {
                                    rangeType = 1;
                                } else if (spinnerType.getSelectedItem().toString().equals("дней"))
                                    rangeType = 2;
                                else return;

                                if (etRange.getText().toString().isEmpty())
                                    range = 0;
                                else {
                                    SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(getContext());
                                    range = pr.getInt("Way", 0)+Integer.parseInt(etRange.getText().toString());
                                }

                                if (etRepeatValue.getText().toString().isEmpty()) {
                                    repeat = 0;
                                    rangeType = 0;
                                } else repeat = (Integer.parseInt(etRange.getText().toString()));
                                Notifications nt = new Notifications(0, etName.getText().toString(), etDate.getText().toString(), range, repeat, rangeType);
                                dbInput(nt);
                                updateList();
                                dismiss();
                            }
                        }
                    });

                }
            });

            etDate.setOnClickListener(v1 -> {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(AddNotificationFragment.this, 0);
                datePicker.show(getParentFragmentManager(), "date picker");
            });

            return dialog;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
            Log.i("Date", currentDate);
            //dates = Integer.toString(dayOfMonth);
            //months = Integer.toString(month);
            //years = Integer.toString(year);

            etDate.setText(currentDate);
        }
    }
}