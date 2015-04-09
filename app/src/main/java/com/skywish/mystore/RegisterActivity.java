package com.skywish.mystore;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skywish.mystore.model.User;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by skywish on 2015/4/5.
 */
public class RegisterActivity extends BaseActivity {

    private Button register;
    private EditText edit_email;
    private EditText edit_password;
    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_email = (EditText) findViewById(R.id.register_edit_email);
        edit_password = (EditText) findViewById(R.id.register_edit_password);
        register = (Button) findViewById(R.id.register_button_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAndAdd();
            }
        });
    }

    private void verifyAndAdd() {
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "请输入邮箱",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "请输入密码",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        addUser();
    }

    private void addUser() {
        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setPassword(password);

        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(), "注册失败" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
