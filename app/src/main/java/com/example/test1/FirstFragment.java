package com.example.test1;

import android.Manifest;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.test1.databinding.FragmentFirstBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 200;
    private final String[] cameraPermissions = {Manifest.permission.CAMERA};
    private final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    ImageView historyBtn;
    TextView history;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == getActivity().RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Bundle extras = data.getExtras();
//                                Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hello);
                                Bitmap imageBitmap = (Bitmap) extras.get("data");

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
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Hebrew App");
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Find the ImageView by its ID within the inflated layout
        historyBtn = view.findViewById(R.id.history_btn);
        // Set up click listener for the ImageView
        history = view.findViewById(R.id.history_text);

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the history button click
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
                if (acct != null) {
                    String userEmail = acct.getEmail();
                    // Create an intent to start the HistoryDisplayActivity
                    Intent historyIntent = new Intent(requireContext(), com.example.test1.HistoryDisplayActivity.class);
                    // Pass user email as an extra to the HistoryDisplayActivity
                    historyIntent.putExtra("user_email", userEmail);
                    // Start the HistoryDisplayActivity
                    startActivity(historyIntent);
                } else {
                    // Handle the case when the user is not signed in
                    // You may want to show a message or take other actions
                }
            }
        });
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });

        // Check the user's sign-in status and show/hide buttons accordingly
        checkSignInStatus();

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the GoogleSignInFragment
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_GoogleSignInFragment);
            }
        });

        binding.buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    // Method to check the user's sign-in status and show/hide buttons accordingly
    private void checkSignInStatus() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            // User is signed in, hide the signInButton and show the signOutButton and historyButton
            binding.buttonSignIn.setVisibility(View.GONE);
            binding.buttonSignOut.setVisibility(View.VISIBLE);
            historyBtn.setVisibility(View.VISIBLE); // Show history button
            history.setVisibility(View.VISIBLE);
        } else {
            // User is signed out, show the signInButton and hide the signOutButton and historyButton
            binding.buttonSignIn.setVisibility(View.VISIBLE);
            binding.buttonSignOut.setVisibility(View.GONE);
            historyBtn.setVisibility(View.GONE); // Hide history button
            history.setVisibility(View.GONE);


        }
    }



    private void saveImageToFile(Bitmap i) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hello);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        // Pass the context to the ServerConnectionTask constructor
        new ServerConnectionTask(requireContext()).execute(base64Image);
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

        private Context context; // Add a Context field

        ServerConnectionTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String base64Image = params[0];
            try {
                // Pass the context to the sendBase64ImgToServer method
                ServerConnectBase64.sendBase64ImgToServer(context, base64Image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    void signOut(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(requireActivity(),gso);

        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                System.out.println("Sign out succseesfuly");
                startActivity(new Intent(requireActivity(),MainActivity.class));
            }
        });
    }

}
