package com.example.test1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.test1.databinding.FragmentFirstBinding;

import java.io.ByteArrayOutputStream;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 200;
    private final String[] cameraPermissions = {Manifest.permission.CAMERA};
    private final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == getActivity().RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Bundle extras = data.getExtras();
                                Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hello);
//                                Bitmap imageBitmap = (Bitmap) extras.get("data");

                                try {
                                    saveImageToFile(imageBitmap);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }

                                // Navigate to the second fragment after taking the photo
                                NavHostFragment.findNavController(this)
                                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                            }
                        } else {
                            // Photo capture failed or was canceled
                            // Handle accordingly
                        }
                    });

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });
    }

    private void saveImageToFile(Bitmap i) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hello);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        new ServerConnectionTask().execute(base64Image);
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Camera permission is already granted
            Log.d("CameraPermission", "Camera permission is granted");
            openCamera();
        } else {
            // Camera permission is not granted, request it
            Log.d("CameraPermission", "Camera permission is not granted");
            ActivityCompat.requestPermissions(requireActivity(), cameraPermissions,
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ServerConnectionTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String base64Image = params[0];
            try {
                return ServerConnectBase64.sendBase64ImgToServer(base64Image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
