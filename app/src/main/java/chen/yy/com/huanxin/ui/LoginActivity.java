package chen.yy.com.huanxin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chen.yy.com.huanxin.R;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/17
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */
public class LoginActivity extends AppCompatActivity {
      @BindView(R.id.iv_login)
      ImageView ivLogin;
      @BindView(R.id.et_user)
      EditText etUser;
      @BindView(R.id.rl_user)
      RelativeLayout rlUser;
      @BindView(R.id.et_password)
      EditText etPassword;
      @BindView(R.id.rl_pwd)
      RelativeLayout rlPwd;
      @BindView(R.id.btn_register)
      Button btnRegister;
      @BindView(R.id.btn_login)
      Button btnLogin;
      private EMClient instance=EMClient.getInstance();
      private static final String TAG = "LoginActivity";

      @Override
      protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);
            ButterKnife.bind(this);
//            etPassword.setKeyListener(this);
//            etPassword.clearFocus();
//            etPassword.setFocusable(false);
//            etUser.clearFocus();
//            etUser.setFocusable(false);
            etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                  @Override
                  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (KeyEvent.KEYCODE_ENTER==event.getKeyCode()||actionId== EditorInfo.IME_ACTION_SEND){
                              Log.e(TAG, "onEditorAction: ");
                              if (event.getAction()==KeyEvent.ACTION_DOWN) {
                                    Log.e(TAG, "onEditorAction: 1" );
                                    login();

                              }
                        }
                        return true;
                  }
            });

      }

      @OnClick({R.id.iv_login, R.id.btn_register, R.id.btn_login})
      public void onClick(View view) {
            switch (view.getId()) {
                  case R.id.iv_login:

                        break;
//                  case R.id.et_user:
//                        showTip("输入");
//                        etUser.clearFocus();
//                        etUser.setFocusable(true);
//                        login();
//                        break;
//                  case R.id.et_password:
//                        login();
//                        etPassword.setFocusable(true);
//                        showTip("密码");
//                        break;
                  case R.id.btn_register:
                        showTip("注册");
                        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                        startActivity(intent);
                        break;
                  case R.id.btn_login:

                        login();
                        break;
                  default:
                        Log.e(TAG, "onClick: 1" );


                        break;
            }
      }
//监听enter按键
      @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
            Log.e(TAG, "onKeyDown: "+keyCode+"event"+event.getKeyCode() );
            if (KeyEvent.KEYCODE_ENTER==keyCode){
                  Log.e(TAG, "onKeyDown: jl" );
                  login();

            }
            return super.onKeyDown(keyCode, event);
      }


      private void login() {
            Log.e(TAG, "login: pu" );
            String passowrd = etPassword.getText().toString().trim();
            String user = etUser.getText().toString().trim();
            if((passowrd !=null&&passowrd.length()>0)&&(user !=null&&user.length()>0)){
                  //提前加载
              instance.login(user, passowrd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                          instance.groupManager().loadAllGroups();
                          instance.chatManager().loadAllConversations();
                          Intent intent1=new Intent(LoginActivity.this,MainActivity.class);
                          startActivity(intent1);
                          finish();
                    }
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {
                          Log.e(TAG, "onProgress: "+i );
                    }
              });
            }else {
                  showTip("密码或者用户为空");
            }
      }

      public void showTip(String message){
            if (message!=null)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            else
                  Toast.makeText(this, "输入为空", Toast.LENGTH_SHORT).show();

      }


}
