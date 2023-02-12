package com.avantrio.assessment.ui.home.Fragments.UserLogs;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avantrio.assessment.R;

public class UserLogHolder extends RecyclerView.ViewHolder {
    public TextView user_log_date,user_log_time,user_log_alert_view,user_log_location;
    public UserLogHolder(@NonNull View itemView) {
        super(itemView);
        user_log_date = (TextView) itemView.findViewById(R.id.user_log_date);
        user_log_time = (TextView) itemView.findViewById(R.id.user_log_time);
        user_log_alert_view = (TextView) itemView.findViewById(R.id.user_log_alert_view);
        user_log_location = (TextView) itemView.findViewById(R.id.user_log_location);


    }
}
