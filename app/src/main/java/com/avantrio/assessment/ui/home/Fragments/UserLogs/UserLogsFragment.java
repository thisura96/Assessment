package com.avantrio.assessment.ui.home.Fragments.UserLogs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avantrio.assessment.R;
import com.avantrio.assessment.api.ApiService;
import com.avantrio.assessment.api.RetrofitClient;
import com.avantrio.assessment.model.UserLogModel;
import com.avantrio.assessment.model.UserLogsMain;
import com.avantrio.assessment.model.UserModel;
import com.avantrio.assessment.model.UserResponse;
import com.avantrio.assessment.ui.home.Fragments.User.UserAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserLogsFragment extends Fragment {
    Context context;
    Activity activity;
    ArrayList<UserLogModel> userLogsList;
    UserLogsAdapter userLogsAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView userLogRecyclerView;
    TextView  txt_userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_logs, container, false);
        context = getContext();
        activity = getActivity();

        txt_userName = view.findViewById(R.id.txt_userName);
        userLogRecyclerView= view.findViewById(R.id.recycle_userLogView);
        userLogsList = new ArrayList<>();
        userLogsAdapter = new UserLogsAdapter(getContext(), userLogsList);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        userLogRecyclerView.setLayoutManager(linearLayoutManager);
        userLogRecyclerView.setAdapter(userLogsAdapter);

        Bundle bundle = this.getArguments();

        if(bundle != null){

            String userid = String.valueOf(bundle.getInt("userid"));
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String token = sharedPreferences.getString("token", "");
            callGetUserLogs( token , userid.trim());
        }

        return view;
    }

    public void callGetUserLogs(String token ,String userid) {
        ApiService apiService = RetrofitClient.createService(ApiService.class);
        Call<UserLogsMain> userResponseCall = apiService.getUserLogs("Bearer " + token,userid);
        userResponseCall.enqueue(new Callback<UserLogsMain>() {
            @Override
            public void onResponse(Call<UserLogsMain> call, Response<UserLogsMain> response) {
                if (response.code() == 200) {
                    UserLogsMain userLogsMain = response.body();

                    txt_userName.setText( userLogsMain.getUser());

                    List<com.avantrio.assessment.model.Log> logs = userLogsMain.getLogs();
                    if (logs.size() > 0) {
                        for (int i = 0; i < logs.size(); i++) {
                            String date = logs.get(i).getDate();
                            String time = logs.get(i).getTime();
                            Boolean alertView = logs.get(i).getAlertView();
                            Double latitude = logs.get(i).getLatitude();
                            Double longitude =logs.get(i).getLongitude();
                            Geocoder geocoder;
                            String address = null;
                            List<Address> addresses = null;
                            try {

                                geocoder = new Geocoder(context, Locale.getDefault());
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                address = addresses.get(0).getAddressLine(0);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            userLogsList.add(new UserLogModel(date,time,alertView,address,""));
                        }
                        userLogsAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<UserLogsMain> call, Throwable t) {
                Log.d("onResponseFailure", "callUser: " + t.getMessage());
                Toast.makeText(context, "There seems to be an error processing you request. Please try again later.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}