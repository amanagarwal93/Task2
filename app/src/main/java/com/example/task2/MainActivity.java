package com.example.task2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.task2.adapter.DataAdapter;
import com.example.task2.databinding.ActivityMainBinding;
import com.example.task2.utility.DataStorage;
import com.example.task2.utility.Utils;
import com.example.task2.viewmodel.DataViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    private DataViewModel dataViewModel;
    private DataAdapter adapter;

    private static final int REQUEST_PERMISSION_SETTING = 101;

    private String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!DataStorage.getInstance(this).isPermissionGranted()) {
            checkPermissions();
        }

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        binding.reyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.reyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.getResponseBtn.setOnClickListener(v -> {
            dataViewModel.getResponseDataMutableLiveData().observe(this, responseData -> {
                adapter = new DataAdapter(responseData);
                binding.reyclerView.setAdapter(adapter);
            });
        });

        binding.openMapsBtn.setOnClickListener(v -> {
            if (DataStorage.getInstance(this).isPermissionGranted()) {
                startActivity(new Intent(this, MapsActivity.class));
            } else {
                checkPermissions();
            }
        });

        binding.gmailLoginBtn.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 101);
        });
    }


    private void checkPermissions() {
        if (Utils.getInstance().isNetworkAvailable(this)) {

            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED) {
                DataStorage.getInstance(this).setPermissionGranted(true);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DataStorage.getInstance(this).setPermissionGranted(false);
                requestPermissions(permissionsRequired, REQUEST_PERMISSION_SETTING);
            } else {
                Utils.showShortToastMessage(binding.rootLayout, "No Internet Connection");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == 101) {
                try {
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Utils.showShortToastMessage(binding.rootLayout, "Sign In Success");
                } catch (ApiException e) {
                    // The ApiException status code indicates the detailed failure reason.
                    Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_SETTING) {

            if (permissions.length == 0) {
                return;
            }
            boolean allPermissionsGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
            }
            if (!allPermissionsGranted) {
                boolean somePermissionsForeverDenied = false;
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        //denied
                        DataStorage.getInstance(this).setPermissionGranted(false);
                        Log.d("denied", permission);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(permissionsRequired, REQUEST_PERMISSION_SETTING);
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                            //allowed
                            Log.d("allowed", permission);
                            DataStorage.getInstance(this).setPermissionGranted(true);
                        } else {
                            //set to never ask again
                            Log.d("set to never ask again", permission);
                            somePermissionsForeverDenied = true;
                        }
                    }
                }
                if (somePermissionsForeverDenied) {
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Permissions Required")
                            .setMessage("You have forcefully denied some of the required permissions " +
                                    "for this action. Please open settings, go to permissions and allow them.")
                            .setPositiveButton("Go to Settings", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dialog.dismiss();
                            })
                            .setCancelable(false)
                            .create()
                            .show();
                }
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                DataStorage.getInstance(this).isPermissionGranted();
            }
        }
    }
}