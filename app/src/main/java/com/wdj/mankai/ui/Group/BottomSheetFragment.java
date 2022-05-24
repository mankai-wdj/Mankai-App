package com.wdj.mankai.ui.Group;

import static android.content.Context.MODE_PRIVATE;
import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wdj.mankai.R;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    Context context;
    Switch bottomSwitch;
    LinearLayout PasswordArea;
    EditText GroupPassword;
    TextView GroupPassawordSubmit;
    String password;


    public BottomSheetFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_groupmake_bottomsheet, container, false);

        PasswordArea = view.findViewById(R.id.Group_sheef_password_area);
        GroupPassword = view.findViewById(R.id.Group_password);
        GroupPassawordSubmit = view.findViewById(R.id.bottomSheet_submit);

        bottomSwitch = view.findViewById(R.id.Group_sheef_sw);
        PasswordArea.setVisibility(View.GONE);
        bottomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    PasswordArea.setVisibility(View.VISIBLE);
                } else {
                    PasswordArea.setVisibility(View.GONE);
                }
            }
        });


        //그룹 비번 설정후 확인 클릭시 원래 페이지로 넘어감
        GroupPassawordSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("password",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("PasswordValue", GroupPassword.getText().toString());
                editor.commit();
                dismiss();
            }
        });

        return view;

    }
}




