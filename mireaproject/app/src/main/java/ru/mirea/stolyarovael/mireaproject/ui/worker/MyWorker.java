package ru.mirea.stolyarovael.mireaproject.ui.worker;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("MireaProjectWorker", "Выполнение задачи началато");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            return Result.failure();
        }
        Log.d("MireaProjectWorker", "Выполнение задачи завершено");
        return Result.success();
    }
}
