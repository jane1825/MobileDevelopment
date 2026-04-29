package ru.mirea.stolyarovael.mireaproject.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.stolyarovael.mireaproject.R;

public class ProfileFragment extends Fragment {

    private EditText etName, etAge, etNickname, etZodiac, etAdditionalInfo;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "user_profile";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        etName = root.findViewById(R.id.editTextName);
        etAge = root.findViewById(R.id.editTextAge);
        etNickname = root.findViewById(R.id.editTextNickname);
        etZodiac = root.findViewById(R.id.editTextZodiac);
        etAdditionalInfo = root.findViewById(R.id.editTextAdditionalInfo);
        Button btnSave = root.findViewById(R.id.buttonSaveProfile);

        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        loadProfile();

        btnSave.setOnClickListener(v -> saveProfile());

        return root;
    }

    private void loadProfile() {
        etName.setText(sharedPreferences.getString("name", ""));
        etAge.setText(sharedPreferences.getString("age", ""));
        etNickname.setText(sharedPreferences.getString("nickname", ""));
        etZodiac.setText(sharedPreferences.getString("zodiac", ""));
        etAdditionalInfo.setText(sharedPreferences.getString("additional", ""));
    }

    private void saveProfile() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", etName.getText().toString());
        editor.putString("age", etAge.getText().toString());
        editor.putString("nickname", etNickname.getText().toString());
        editor.putString("zodiac", etZodiac.getText().toString());
        editor.putString("additional", etAdditionalInfo.getText().toString());
        editor.apply();

        Toast.makeText(getContext(), "Профиль сохранен", Toast.LENGTH_SHORT).show();
    }
}