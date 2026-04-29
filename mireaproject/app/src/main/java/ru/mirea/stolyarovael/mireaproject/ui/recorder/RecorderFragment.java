package ru.mirea.stolyarovael.mireaproject.ui.recorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import ru.mirea.stolyarovael.mireaproject.R;

public class RecorderFragment extends Fragment {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private Button btnRecord, btnPlay, btnDelete;
    private TextView tvDuration, tvFileType;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean isRecording = false;
    private String recordFilePath = null;

    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recorder, container, false);

        btnRecord = root.findViewById(R.id.btnRecord);
        btnPlay = root.findViewById(R.id.btnPlay);
        btnDelete = root.findViewById(R.id.btnDelete);
        tvDuration = root.findViewById(R.id.tvDuration);
        tvFileType = root.findViewById(R.id.tvFileType);
        
        File musicDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (musicDir != null) {
            recordFilePath = new File(musicDir, "audiorecord.3gp").getAbsolutePath();
            Log.d("MireaProject", "Recording file path: " + recordFilePath);
        }

        btnRecord.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            } else {
                if (isRecording) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
        });

        btnPlay.setOnClickListener(v -> startPlaying());

        btnDelete.setOnClickListener(v -> deleteAudio());

        return root;
    }

    private void startRecording() {
        if (recordFilePath == null) return;

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;
            btnRecord.setText("Остановить запись");
            btnPlay.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            tvDuration.setVisibility(View.GONE);
            tvFileType.setVisibility(View.GONE);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошипк", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (RuntimeException e) {
                // Ignore if stop called too soon
            }
            recorder.release();
            recorder = null;
        }
        isRecording = false;
        btnRecord.setText("Начать запись");
        btnPlay.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        
        displayDuration();
        displayFileType();
    }

    private void deleteAudio() {
        if (recordFilePath != null) {
            File file = new File(recordFilePath);
            if (file.exists()) {
                if (file.delete()) {
                    Toast.makeText(getContext(), "Аудио удалено", Toast.LENGTH_SHORT).show();
                    btnPlay.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.GONE);
                    tvDuration.setVisibility(View.GONE);
                    tvFileType.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "Ошипк", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void displayDuration() {
        if (recordFilePath == null) return;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        retriever.setDataSource(recordFilePath);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillis = Long.parseLong(time);

        long hours = (timeInMillis / (1000 * 60 * 60)) % 24;
        long minutes = (timeInMillis / (1000 * 60)) % 60;
        long seconds = (timeInMillis / 1000) % 60;

        String duration = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        tvDuration.setText("Длительность: " + duration);
        tvDuration.setVisibility(View.VISIBLE);


    }

    private void displayFileType() {
        if (recordFilePath != null) {
            String extension = recordFilePath.substring(recordFilePath.lastIndexOf(".") + 1);
            tvFileType.setText("Тип файла: " + extension);
            tvFileType.setVisibility(View.VISIBLE);
        }
    }

    private void startPlaying() {
        if (recordFilePath == null) return;

        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Ошипк", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
