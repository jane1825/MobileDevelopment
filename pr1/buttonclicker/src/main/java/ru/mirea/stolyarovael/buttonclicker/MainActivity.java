package ru.mirea.stolyarovael.buttonclicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tvOut;
    private Button btnWhoAmI;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        checkBox = findViewById(R.id.checkBox);


        btnWhoAmI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOut.setText("Мой номер по списку № 23 (Столярова)");
                checkBox.setChecked(true);
            }
        });
    }

    public void onMyButtonClick(View view) {
        tvOut.setText("Это не я сделал!");
        checkBox.setChecked(false);
        Toast.makeText(this, "Ещё один способ!", Toast.LENGTH_SHORT).show();
    }
}