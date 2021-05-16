package com.example.carassistant.ui.notifications;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.carassistant.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NotificationsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        FloatingActionButton addNotifButton = (FloatingActionButton)root.findViewById(R.id.add_notif_but);
        addNotifButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                AddNotificationFragment dialog = new AddNotificationFragment();
                dialog.show(getChildFragmentManager(), "custom");
            }
        });
        return root;



    }


    public static class AddNotificationFragment extends DialogFragment {
        @NotNull
        @Override
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();

            builder.setView(inflater.inflate(R.layout.add_notif_fragment, null))
                    // Add action buttons
                    .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                        }
                    })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            return builder.create();

        }

    }
}