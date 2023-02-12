package com.avantrio.assessment.ui.home.Fragments.User;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avantrio.assessment.R;
import com.avantrio.assessment.api.ApiService;
import com.avantrio.assessment.api.RetrofitClient;
import com.avantrio.assessment.model.UserModel;
import com.avantrio.assessment.model.UserResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment implements TextWatcher {
    private static final int LOCATION_PERMISSION_ID = 1001;
    Context context;
    UserAdapter SettingAdapter;
    ArrayList<UserModel> userList;
    RecyclerView userRecyclerView;
    ToggleButton btn_sort;
    ImageButton btn_locate_track;
    Activity activity;
    EditText et_search;
    FusedLocationProviderClient mFusedLocationClient;
    LinearLayoutManager linearLayoutManager;
    boolean isAscending = true;
    private LocationRequest mLocationRequest;
    private String currentSearchText = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        context = getContext();
        activity = getActivity();
        userRecyclerView = view.findViewById(R.id.recycle_userView);
        btn_sort = view.findViewById(R.id.btn_sort);
        btn_locate_track = view.findViewById(R.id.btn_locate_track);
        et_search = view.findViewById(R.id.et_search);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString("token", "");
        callGetUser(token);

        userList = new ArrayList<>();
        SettingAdapter = new UserAdapter(getContext(), userList);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        userRecyclerView.setLayoutManager(linearLayoutManager);
        userRecyclerView.setAdapter(SettingAdapter);

        et_search.addTextChangedListener(this);

        btn_locate_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocation();
            }
        });
        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortData(isAscending);
                isAscending = !isAscending;
            }
        });


        return view;

    }

    public void sortData(boolean asc) {
        if (asc) {
            Collections.sort(userList, new Comparator<UserModel>() {
                @Override
                public int compare(UserModel lhs, UserModel rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        } else {
            Collections.reverse(userList);
        }
        SettingAdapter = new UserAdapter(getContext(), userList);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        userRecyclerView.setLayoutManager(linearLayoutManager);
        userRecyclerView.setAdapter(SettingAdapter);

    }

    public void callGetUser(String token) {
        ApiService apiService = RetrofitClient.createService(ApiService.class);
        Call<ArrayList<UserResponse>> userResponseCall = apiService.getUser("Bearer " + token);
        userResponseCall.enqueue(new Callback<ArrayList<UserResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<UserResponse>> call, Response<ArrayList<UserResponse>> response) {
                if (response.code() == 200) {
                    ArrayList<UserResponse> userResponses = response.body();
                    if (userResponses.size() > 0) {
                        for (int i = 0; i < userResponses.size(); i++) {
                            Integer id = userResponses.get(i).getId();
                            String name = userResponses.get(i).getName();
                            userList.add(new UserModel(id, name));

                        }
                        SettingAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (response.code() == 429) {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();

                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Log.d("onFailure", "callUser: " + jObjError.getString("message"));
                            Toast.makeText(context, "There seems to be an error processing you request. Please try again later.", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Log.d("Exception", "callUser: " + e.getMessage());
                            Toast.makeText(context, "There seems to be an error processing you request. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserResponse>> call, Throwable t) {
                Log.d("onResponseFailure", "callUser: " + t.getMessage());
                Toast.makeText(context, "There seems to be an error processing you request. Please try again later.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void changeLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                requestNewLocationData();
            } else {
                Toast.makeText(activity, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,request for permissions
            requestPermissions();
        }
    }

    private void requestPermissions() {
        requestPermissions(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // method to check if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest - object with appropriate methods
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        // mLocationRequest.setNumUpdates(1);

        // setting LocationRequest on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    void stopLocationUpdates() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    changeLocation();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            || ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showMessageOKCancel("You need to allow location permissions to change your location.",
                                (dialog, which) -> requestPermissions());
                    } else {
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        Log.d("Permission", "Never ask again selected, or device policy prohibits the app from having that permission.");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("You need to allow location permission to change your location. Please go to settings and allow permissions.")
                                .setCancelable(false)
                                .setPositiveButton("Settings", (dialog, id) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Geocoder gcd = new Geocoder(activity,
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(), 1);
                if (addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_address", address);
                    editor.apply();

                    Toast.makeText(context, "Well done! Location successfully updated", Toast.LENGTH_SHORT).show();

                }
                stopLocationUpdates();
            } catch (Exception e) {
                e.printStackTrace();
                stopLocationUpdates();

            }
        }
    };

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        currentSearchText = et_search.getText().toString();

        ArrayList<UserModel> filteredUserList = new ArrayList<UserModel>();
        for (UserModel shape : userList) {
            if (shape.getName().toLowerCase().contains(currentSearchText.toLowerCase())) {
                filteredUserList.add(shape);
            }


            SettingAdapter = new UserAdapter(getContext(), filteredUserList);
            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            userRecyclerView.setLayoutManager(linearLayoutManager);
            userRecyclerView.setAdapter(SettingAdapter);
        }


    }
}