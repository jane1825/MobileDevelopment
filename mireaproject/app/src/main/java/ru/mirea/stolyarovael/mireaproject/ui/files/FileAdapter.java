package ru.mirea.stolyarovael.mireaproject.ui.files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import ru.mirea.stolyarovael.mireaproject.R;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private final List<File> files;
    private final OnConvertClickListener listener;

    public interface OnConvertClickListener {
        void onConvertClick(File file);
    }

    public FileAdapter(List<File> files, OnConvertClickListener listener) {
        this.files = files;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = files.get(position);
        holder.textViewName.setText(file.getName());
        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            holder.textViewContent.setText(content);
        } catch (IOException e) {
            holder.textViewContent.setText("Ошибка чтения");
        }

        holder.buttonConvert.setOnClickListener(v -> listener.onConvertClick(file));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewContent;
        Button buttonConvert;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewFileName);
            textViewContent = itemView.findViewById(R.id.textViewFileContent);
            buttonConvert = itemView.findViewById(R.id.buttonConvert);
        }
    }
}
