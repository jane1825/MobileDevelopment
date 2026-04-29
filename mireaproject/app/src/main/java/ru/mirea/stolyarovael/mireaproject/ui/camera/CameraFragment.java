package ru.mirea.stolyarovael.mireaproject.ui.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.mirea.stolyarovael.mireaproject.R;

public class CameraFragment extends Fragment {

    private Button btnTakePicture;
    private ImageView[] imageViews = new ImageView[4];
    private List<String> photoPaths = new ArrayList<>();
    private Uri currentPhotoUri;
    private String currentPhotoPath;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(getContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    photoPaths.add(currentPhotoPath);
                    updateUI();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        btnTakePicture = root.findViewById(R.id.btnTakePicture);
        imageViews[0] = root.findViewById(R.id.ivPhoto1);
        imageViews[1] = root.findViewById(R.id.ivPhoto2);
        imageViews[2] = root.findViewById(R.id.ivPhoto3);
        imageViews[3] = root.findViewById(R.id.ivPhoto4);

        btnTakePicture.setOnClickListener(v -> {
            if (photoPaths.size() < 4) {
                checkPermissionAndTakePhoto();
            } else {
                Toast.makeText(getContext(), "Коллаж готов!", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void checkPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("CameraFragment", "Ошипк", ex);
            }
            if (photoFile != null) {
                currentPhotoUri = FileProvider.getUriForFile(requireContext(),
                        "ru.mirea.stolyarovael.mireaproject.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
                takePictureLauncher.launch(takePictureIntent);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d("FILEPATH", storageDir.toString());
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void updateUI() {
        btnTakePicture.setText(String.format(Locale.getDefault(), "Сделать фото (%d/4)", photoPaths.size()));
        for (int i = 0; i < photoPaths.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPaths.get(i));
            imageViews[i].setImageBitmap(bitmap);
        }
    }
}
