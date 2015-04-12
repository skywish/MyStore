package com.skywish.mystore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by skywish on 2015/4/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextView register;
    private Button login;
    private EditText edit_email;
    private EditText edit_password;

    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = (EditText) findViewById(R.id.login_edit_email);
        edit_password = (EditText) findViewById(R.id.login_edit_password);

        login = (Button) findViewById(R.id.login_button_login);
        login.setOnClickListener(this);
        register = (TextView) findViewById(R.id.login_text_register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button_login:
                login();
                break;
            case R.id.login_text_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void login() {
        email = edit_email.getText().toString();
        password = edit_password.getText().toString();

        final BmobUser user = new BmobUser();
        user.setUsername(email);
        user.setPassword(password);

        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
//                if (user.getEmailVerified()) {
//                    Toast.makeText(LoginActivity.this, "登陆成功",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LoginActivity.this, "请先激活邮箱",
//                            Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(LoginActivity.this, "登陆成功",
                        Toast.LENGTH_SHORT).show();
                MainTabActivity.activityStart(LoginActivity.this, email);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this, "登陆失败" + s,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
