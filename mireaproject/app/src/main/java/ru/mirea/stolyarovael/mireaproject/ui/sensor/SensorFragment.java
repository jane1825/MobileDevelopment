package ru.mirea.stolyarovael.mireaproject.ui.sensor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import ru.mirea.stolyarovael.mireaproject.R;

public class SensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private TextView tvPressureValue;
    private TextView tvPressureStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sensor, container, false);

        tvPressureValue = root.findViewById(R.id.tvPressureValue);
        tvPressureStatus = root.findViewById(R.id.tvPressureStatus);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);


        return root;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float pressureHPa = event.values[0];
            // Convert hPa to mmHg: 1 hPa = 0.750062 mmHg
            float pressureMmHg = pressureHPa * 0.750062f;

            tvPressureValue.setText(String.format(Locale.getDefault(), "Давление: %.2f ммрс", pressureMmHg));

            String status;
            int color;
            if (pressureMmHg >= 739 && pressureMmHg <= 755) {
                status = "НОРМАЛЬНОЕ";
                color = Color.GREEN;
            } else if (pressureMmHg < 730) {
                status = "НИЗВКОЕ";
                color = Color.RED;
            }else {
                status = "ВЫСОКОЕ";
                color = Color.RED;
            }
            
            tvPressureStatus.setText(status);
            tvPressureStatus.setTextColor(color);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this task
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pressureSensor != null) {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}
