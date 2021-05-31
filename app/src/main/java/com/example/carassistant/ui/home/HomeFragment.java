package com.example.carassistant.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.carassistant.MainActivity;
import com.example.carassistant.R;
import com.example.carassistant.ui.StartActivity;
import com.example.carassistant.ui.StartActivityInfo;

import java.util.Objects;

public class HomeFragment extends Fragment {
    private TextView tvTitle, tvYear, tvWay, tvEngine;
    private ImageView ivCar;
    private Button btChange;
    private HomeViewModel homeViewModel;
    public SharedPreferences homePref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btChange = root.findViewById(R.id.button_change_car_info);
        tvEngine = root.findViewById(R.id.tv_home_engine);
        tvYear = root.findViewById(R.id.tv_home_year);
        tvWay = root.findViewById(R.id.tv_home_way);
        tvTitle = root.findViewById(R.id.tv_home_cartitle);
        ivCar = root.findViewById(R.id.imageView_home_car);
        EditText tvEditWay = new EditText(getContext());
        tvEditWay.setInputType(InputType.TYPE_CLASS_NUMBER);
        //tvEditWay.setTag();
        homePref = PreferenceManager.getDefaultSharedPreferences(getContext());
        tvTitle.setText(homePref.getString("Mark", "") +" "+ homePref.getString("Model", ""));
        tvEngine.setText("Объем двигателя: "+homePref.getString("Engine", "")+" л.");
        tvYear.setText(homePref.getString("Year", "")+" г.в.");
        tvWay.setText(homePref.getInt("Way", 0)+" км.");
        byte[] imageAsBytes = Base64.decode(homePref.getString("Img", "").getBytes(), Base64.DEFAULT);
        ivCar.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        btChange.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), StartActivity.class);
            StartActivityInfo st = new StartActivityInfo(homePref.getString("Mark", ""),
                    homePref.getString("Model", ""),
                    homePref.getString("Year", ""),
                    homePref.getString("Engine", ""),
                    homePref.getInt("Way", 0));
            intent.putExtra("Info", st);
            startActivity(intent);
        });
        tvWay.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setView(tvEditWay)
                    .setTitle("Изменить пробег");
            tvEditWay.setText(homePref.getInt("Way", 0)+"");

            builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences.Editor edit = homePref.edit().putInt("Way", Integer.parseInt(tvEditWay.getText().toString()));
                    edit.apply();
                    tvWay.setText(Integer.parseInt(tvEditWay.getText().toString())+ " км.");
                }
            });
            AlertDialog dialog = builder.create();
            if(tvEditWay.getParent() != null) {
                ((ViewGroup)tvEditWay.getParent()).removeView(tvEditWay); // <- fix
            }
            dialog.show();
        });


        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            //textView.setText(s);
        });

        return root;
    }


}