package com.avantrio.assessment.ui.home.Fragments.User;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avantrio.assessment.R;

public class UserHolder extends RecyclerView.ViewHolder {
    public ImageView userImage;
    public TextView user_first_name,user_name;
    ImageButton  btn_more_info;

    public UserHolder(@NonNull View itemView) {
        super(itemView);
        userImage = (ImageView) itemView.findViewById(R.id.userImage);
        user_first_name = (TextView) itemView.findViewById(R.id.user_first_name);
        user_name = (TextView) itemView.findViewById(R.id.user_name);
        btn_more_info= (ImageButton) itemView.findViewById(R.id.button_more_info);


    }
}
