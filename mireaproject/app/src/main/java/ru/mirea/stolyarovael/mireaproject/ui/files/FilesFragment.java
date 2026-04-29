package ru.mirea.stolyarovael.mireaproject.ui.files;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.stolyarovael.mireaproject.R;

public class FilesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FileAdapter adapter;
    private List<File> fileList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_files, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        loadFiles();
        
        adapter = new FileAdapter(fileList, this::convertFile);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void loadFiles() {
        fileList.clear();
        File dir = requireContext().getFilesDir();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file);
                }
            }
        }
    }

    public void showCreateFileDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_file, null);
        EditText etName = dialogView.findViewById(R.id.editTextFileName);
        EditText etContent = dialogView.findViewById(R.id.editTextFileContent);

        new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setPositiveButton("Создать", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String content = etContent.getText().toString();
                    if (!name.isEmpty()) {
                        createFile(name + ".txt", content);
                    } else {
                        Toast.makeText(getContext(), "Введите имя файла", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void createFile(String fileName, String content) {
        try (FileOutputStream fos = requireContext().openFileOutput(fileName, android.content.Context.MODE_PRIVATE)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            loadFiles();
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Файл создан", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
        }
    }

    private void convertFile(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            String newFileName;
            String newContent;

            if (file.getName().endsWith(".txt")) {
                newFileName = file.getName().replace(".txt", ".json");
                newContent = "{\"content\": \"" + content.replace("\"", "\\\"") + "\"}";
            } else if (file.getName().endsWith(".json")) {
                newFileName = file.getName().replace(".json", ".txt");
                // Simple attempt to extract content if it was our JSON
                newContent = content.replace("{\"content\": \"", "").replace("\"}", "").replace("\\\"", "\"");
            } else {
                Toast.makeText(getContext(), "Неизвестный формат", Toast.LENGTH_SHORT).show();
                return;
            }

            File newFile = new File(requireContext().getFilesDir(), newFileName);
            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                fos.write(newContent.getBytes(StandardCharsets.UTF_8));
            }
            
            loadFiles();
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Конвертировано в " + newFileName, Toast.LENGTH_SHORT).show();
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка конвертации", Toast.LENGTH_SHORT).show();
        }
    }
}
