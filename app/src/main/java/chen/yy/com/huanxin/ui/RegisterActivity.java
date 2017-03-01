package chen.yy.com.huanxin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.model.db.DbManager;
import chen.yy.com.huanxin.utils.ShowTipUtil;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/17
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */
public class RegisterActivity extends AppCompatActivity {
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
      @BindView(R.id.et_pwd_confirm)
      EditText etPwdConfirm;
      @BindView(R.id.rl_confirm)
      RelativeLayout rlConfirm;
      @BindView(R.id.btn_register)
      Button btnRegister;

      @Override
      protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.register);
            ButterKnife.bind(this);
      }

      @OnClick({R.id.btn_register})
      public void onClick(View view) {
            switch (view.getId()){
                  case R.id.btn_register:
                        final String password = etPassword.getText().toString().trim();
                        final String user = etUser.getText().toString().trim();
                        final String cpassword = etPwdConfirm.getText().toString().trim();
                        if((password!=null&&password.length()>0)&&
                                (user!=null&&user.length()>0)){
                              if(password.equals(cpassword)) {
//                                    ThreadPool.getThreadPool().execute(new Runnable() {
//                                          @Override
//                                          public void run() {
//                                                try {
//                                                      //一致就注册
////                                                      EMClient.getInstance().createAccount(user, cpassword);
////                                                      runOnUiThread(new Runnable() {
////                                                            @Override
////                                                            public void run() {
////                                                                  Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
////                                                            }
////                                                      });
//                                                      finish();//并退出
//                                                } catch (final HyphenateException e) {
//                                                      runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                  Toast.makeText(RegisterActivity.this, ""+e.toString() , Toast.LENGTH_SHORT).show();
//
//                                                            }
//                                                      });

//                                                }
//                                          }
//                                    });
                                    final UserInfo userInfo=new UserInfo();
                                    userInfo.setUser(user);
                                    userInfo.setNickName("moren");
                                    userInfo.setPwd(password);
                                    userInfo.setPhoto(String.valueOf(R.drawable.atguigu_logo));
                                    ThreadPool.getThreadPool().execute(new Runnable() {
                                          @Override
                                          public void run() {
                                                try {
                                                      EMClient.getInstance().createAccount(user,password);
                                                      final String s = DbManager.getDbManager().getUserInfoImp().addAccount(userInfo);
                                                      runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                  if("success".equals(s)){
                                                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                                                    finish();
                                                                  }else {
                                                                        Toast.makeText(RegisterActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                                                                  }
                                                            }
                                                      });

                                                } catch (final HyphenateException e) {
                                                      e.printStackTrace();
                                                      runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                  ShowTipUtil.ShowTip(RegisterActivity.this,e.toString());
                                                            }
                                                      });
                                                }
                                          }
                                    });



                              }else {
                                    Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                              }
                        }else{
                              Toast.makeText(RegisterActivity.this, "用户密码不能为空", Toast.LENGTH_SHORT).show();
                        }

                        break;
            }

      }
}
