package ru.mirea.stolyarovael.dialogapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Создаем объект ProgressDialog
        ProgressDialog pd = new ProgressDialog(getActivity());

        // Устанавливаем заголовок и текст сообщения
        pd.setTitle("Загрузка");
        pd.setMessage("Пожалуйста, подождите...");


        pd.setIndeterminate(true);


        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return pd;
    }
}