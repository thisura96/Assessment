package com.avantrio.assessment.ui.home.Fragments.UserLogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avantrio.assessment.R;
import com.avantrio.assessment.model.UserLogModel;

import java.util.ArrayList;
import java.util.List;

public class UserLogsAdapter  extends RecyclerView.Adapter<UserLogHolder>{
    private Context context;
    private List<UserLogModel> userLogModelList = new ArrayList<>();

    public UserLogsAdapter(Context context, List<UserLogModel> list) {
        this.context = context;
        this.userLogModelList = list;
    }

    @NonNull
    @Override
    public UserLogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_log_list, parent, false);
        return new UserLogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserLogHolder holder, int position) {
        boolean user_log_alert_view = userLogModelList.get(position).getUser_log_alert_view();
        holder.user_log_date.setText(userLogModelList.get(position).getUser_log_date());
        holder.user_log_alert_view.setText( String.valueOf(user_log_alert_view));
        holder.user_log_location.setText(userLogModelList.get(position).getUser_log_location());
        holder.user_log_time.setText(userLogModelList.get(position).getUser_log_time());

    }

    @Override
    public int getItemCount() {
        return userLogModelList.size();
    }
}
