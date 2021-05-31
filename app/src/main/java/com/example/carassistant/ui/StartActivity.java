package com.example.carassistant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carassistant.MainActivity;
import com.example.carassistant.R;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private EditText model, year, engine;
    AutoCompleteTextView mark;
    private ImageView imgCar;
    private Button addCar;
    TextView warning;
    Uri imageUri;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        int def = 0;
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        imgCar = findViewById(R.id.image_car);
        mark = findViewById(R.id.edittext_mark);
        model = findViewById(R.id.edittext_model);
        year = findViewById(R.id.edittext_year);
        engine = findViewById(R.id.edittext_engine);
        addCar = findViewById(R.id.button_add_car);
        warning = findViewById(R.id.textview_warning);
        if(getIntent().hasExtra("Info")){
            Log.i("meow", String.valueOf(mark));
            StartActivityInfo info=(StartActivityInfo)getIntent().getSerializableExtra("Info");
            mark.setText(info.getMark());
            model.setText(info.getModel());
            year.setText(info.getYear());
            engine.setText(info.getEngine());
            def = info.getWay();
        }

        String[] spendTypes = getResources().getStringArray(R.array.marks_array);
        List<String> catList = Arrays.asList(spendTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, catList);
        mark.setAdapter(adapter);
        imgCar.setOnClickListener(view -> {
            openGallery();
        });

        int finalDef = def;
        addCar.setOnClickListener(view -> {
            if (areEditTextsEmpty()){
                warning.setText("Заполните все поля!");
            } else {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ((BitmapDrawable) imgCar.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                SharedPreferences.Editor edit = pref.edit().putString("Mark", this.mark.getText().toString());
                edit.putString("Model", this.model.getText().toString());
                edit.putString("Year", this.year.getText().toString());
                edit.putString("Engine", this.engine.getText().toString());
                edit.putString("Img", encoded);
                edit.putInt("Way", finalDef);
                edit.apply();
                Intent i = new Intent(StartActivity.this, MainActivity.class);
                finish();
                startActivity(i);
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
