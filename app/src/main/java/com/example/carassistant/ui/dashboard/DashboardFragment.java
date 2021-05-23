package com.example.carassistant.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.carassistant.R;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carassistant.R;
import com.example.carassistant.ui.DatePickerFragment;
import com.example.carassistant.ui.Spendings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DashboardFragment extends Fragment {
    static DashboardDB dbDash;
    static ArrayList<Spendings> spendingsList;
    static RecyclerView rvSpendings;
    RecyclerView.LayoutManager layoutManager;
    static DashboardAdapter dashboardAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dbDash = new DashboardDB(this.getContext());
        spendingsList = dbDash.selectAll();
        final ViewGroup nullParent = null;
        View v = inflater.inflate(R.layout.fragment_dashboard, nullParent);
        rvSpendings = v.findViewById(R.id.spendings_rv);
        rvSpendings.addItemDecoration(new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL));
        rvSpendings.setHasFixedSize(true);
        rvSpendings.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(v.getContext());
        rvSpendings.setLayoutManager(layoutManager);
        dashboardAdapter = new DashboardAdapter(dbDash.selectAll());
        rvSpendings.setAdapter(dashboardAdapter);



        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {

        });

        FloatingActionButton addSpendingButton = v.findViewById(R.id.add_spending_but);
        addSpendingButton.setOnClickListener(view -> {
            DashboardFragment.AddDashboardFragment dialog = new DashboardFragment.AddDashboardFragment();
            dialog.show(getChildFragmentManager(), "custom");
        });
        return v;
    }

    public static void dbDashInput(Spendings sp){
        dbDash.insert(sp.getSpendingName(), sp.getSpendingDate(), sp.getSpendingType(), sp.getSpendingSum());
    }
    static void updateList() {
        dashboardAdapter.setArrayMyData(dbDash.selectAll());
        dashboardAdapter.notifyDataSetChanged();
    }





    public static class AddDashboardFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        EditText etDate, etName, etSum;
        AutoCompleteTextView acType;
        Spinner spinnerType;
        @NotNull
        @Override
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.add_spending_fragment, null);
            etDate = v.findViewById(R.id.edit_spending_date);
            etName=v.findViewById(R.id.edit_spending_name);
            acType=v.findViewById(R.id.edit_spending_type);
            etSum = v.findViewById(R.id.edit_spending_money);
            String[] spendTypes = getResources().getStringArray(R.array.spending_type_array);
            List<String> catList = Arrays.asList(spendTypes);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this.getActivity(), android.R.layout.simple_dropdown_item_1line, catList);
            acType.setAdapter(adapter);
            builder.setView(v)
                    // Add action buttons
                    .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int money = Integer.parseInt(etSum.getText().toString());
                            Spendings nt = new Spendings(0, etName.getText().toString(), etDate.getText().toString(), acType.getText().toString(), money);
                            dbDashInput(nt);
                            updateList();
                        }
                    })
                    .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());

            etDate.setOnClickListener(v1 -> {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(DashboardFragment.AddDashboardFragment.this, 0);
                datePicker.show(getParentFragmentManager(), "date picker");
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