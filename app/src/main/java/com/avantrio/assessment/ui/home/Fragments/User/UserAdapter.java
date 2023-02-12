package com.avantrio.assessment.ui.home.Fragments.User;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.avantrio.assessment.R;
import com.avantrio.assessment.model.UserModel;
import com.avantrio.assessment.ui.home.Fragments.Setting.SettingFragment;
import com.avantrio.assessment.ui.home.Fragments.UserLogs.UserLogsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserAdapter extends RecyclerView.Adapter<UserHolder>{
    private Context context;
    private List<UserModel> list = new ArrayList<>();


    public UserAdapter(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_list, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        String name = list.get(position).getName();
        //get first letter of each String item
        String firstLetter = String.valueOf(name.charAt(0));

        int[] colors = new int[] {R.color.green,R.color.purple,R.color.blue,R.color.gold,R.color.orange,
                R.color.opal_green,R.color.dark_blue,R.color.red};


        int randomColor = colors[new Random().nextInt(colors.length)];
        holder.userImage.setImageResource(randomColor);
        holder.user_first_name.setText(firstLetter);
        holder.user_name.setText(name);



        holder.btn_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context wrapper = new ContextThemeWrapper(context, R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper,  holder.btn_more_info);
                popup.inflate(R.menu.popup_menu_item);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch(id) {
                            case R.id.history:
                                Bundle bundle = new Bundle();
                                bundle.putInt("userid",list.get(position).getId());
                                Fragment fragment = new UserLogsFragment();
                                fragment.setArguments(bundle);
                                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.container_main_body, fragment);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                return true;
                            case R.id.location:
                                return true;
                            default:
                                return false;
                        }

                    }
                });
                popup.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

