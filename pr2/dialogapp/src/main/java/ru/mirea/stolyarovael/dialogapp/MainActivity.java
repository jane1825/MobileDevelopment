package ru.mirea.stolyarovael.dialogapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickShowTimeDialog(View v) {
        new MyTimeDialogFragment().show(getSupportFragmentManager(), "time");
    }

    public void onClickShowDateDialog(View v) {
        new MyDateDialogFragment().show(getSupportFragmentManager(), "date");
    }

    public void onClickShowProgressDialog(View v) {
        new MyProgressDialogFragment().show(getSupportFragmentManager(), "progress");
    }

    public void onTimeSet(int hour, int minute) {
        Toast.makeText(this, "Выбрано время: " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    public void onDateSet(int year, int month, int day) {
        Toast.makeText(this, "Выбрана дата: " + day + "." + (month+1) + "." + year, Toast.LENGTH_SHORT).show();
    }


    public void onClickShowDialog(View view) {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");

        Snackbar.make(view, "Привет", Snackbar.LENGTH_LONG)
                .setAction("ОК", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    public void onOkClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!",
                Toast.LENGTH_LONG).show();
    }

    public void onCancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!",
                Toast.LENGTH_LONG).show();
    }

    public void onNeutralClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!",
                Toast.LENGTH_LONG).show();
    }
}