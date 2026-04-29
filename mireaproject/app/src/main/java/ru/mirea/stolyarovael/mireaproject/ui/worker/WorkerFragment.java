package ru.mirea.stolyarovael.mireaproject.ui.worker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import ru.mirea.stolyarovael.mireaproject.databinding.FragmentWorkerBinding;

public class WorkerFragment extends Fragment {
    private FragmentWorkerBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnStartWork.setOnClickListener(v -> {
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
            WorkManager.getInstance(requireContext()).enqueue(workRequest);

            WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(getViewLifecycleOwner(), workInfo -> {
                        if (workInfo != null) {
                            binding.textStatus.setText("Текущий статус: " + workInfo.getState().name());
                        }
                    });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
