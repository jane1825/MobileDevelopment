package ru.mirea.stolyarovael.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editTextToast);
        Button button = findViewById(R.id.buttonCalculate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = editText.getText().toString();

                int count = text.length();


                String message = String.format("СТУДЕНТКА № 23 ГРУППА БСБО Количество символов - %d", count);


                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}