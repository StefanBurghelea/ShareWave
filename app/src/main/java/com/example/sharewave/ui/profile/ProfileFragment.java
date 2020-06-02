package com.example.sharewave.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.sharewave.R;
import com.example.sharewave.auth.AuthActivity;
import com.example.sharewave.auth.DatabaseHelper;
import com.example.sharewave.classes.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {


    private ProfileViewModel notificationsViewModel;

    ImageView imgLogout;
    TextView username;
    private DatabaseHelper db;
    List<User> user = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        declarar(root);

        db = new DatabaseHelper(getContext());

        user.addAll(db.getUser());

        String name= user.get(0).getUsername();

        username.setText(name);


        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.deleteUsers();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);

            }
        });

        return root;
    }

    private void declarar(View root) {

        imgLogout = root.findViewById(R.id.imglogout);
        username= root.findViewById(R.id.name);

    }
}