package com.example.carassistant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carassistant.R;

public class StartActivity extends AppCompatActivity {
    private EditText mark, model, year, engine;
    private ImageView imgCar;
    private Button addCar;
    TextView warning;
    Uri imageUri;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        pref = getSharedPreferences("CarInfo", MODE_PRIVATE);
        imgCar = findViewById(R.id.image_car);
        mark = findViewById(R.id.edittext_mark);
        model = findViewById(R.id.edittext_model);
        year = findViewById(R.id.edittext_year);
        engine = findViewById(R.id.edittext_engine);
        addCar = findViewById(R.id.button_add_car);
        warning = findViewById(R.id.textview_warning);
        imgCar.setOnClickListener(view -> {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            startActivityForResult(intent, 1);
            openGallery();
        });
        addCar.setOnClickListener(view -> {
            if (areEditTextsEmpty()){
                warning.setText("Заполните все поля!");
            } else {
                SharedPreferences.Editor mark = pref.edit().putString("Mark", this.mark.getText().toString());
                mark.putString("Model", this.model.getText().toString());
                mark.putInt("Year", Integer.parseInt(this.year.getText().toString()));
                mark.putString("Engine", this.engine.getText().toString());
                mark.apply();
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.println(Log.ASSERT, requestCode+"", resultCode+"");
        if (requestCode == 1 && resultCode == RESULT_OK )
        {
            try {
                imageUri = data.getData();
                imgCar.setImageURI(imageUri);
            } catch (Exception ignored) {
            }
        }}

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 1);
    }
    public boolean areEditTextsEmpty(){
        return mark.getText().toString().isEmpty() || model.getText().toString().isEmpty() || year.getText().toString().isEmpty() || engine.getText().toString().isEmpty();
    }
}
