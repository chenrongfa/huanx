package chen.yy.com.huanxin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.utils.ShowTipUtil;

public class AddAccount extends AppCompatActivity implements View.OnClickListener {


      @BindView(R.id.etb_add)
      EaseTitleBar etbAdd;
      @BindView(R.id.query)
      EditText query;
      @BindView(R.id.search_clear)
      ImageButton searchClear;
      @BindView(R.id.tv_add)
      TextView tvAdd;
      @BindView(R.id.btn_add)
      Button btnAdd;
      @BindView(R.id.ll_add)
      LinearLayout llAdd;
      @BindView(R.id.activity_add_account)
      LinearLayout activityAddAccount;
      private static final String TAG = "AddAccount";

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_account);
            ButterKnife.bind(this);
            initEvent();
            

      }

      private void initEvent() {

            tvAdd.setOnClickListener(this);
            searchClear.setOnClickListener(this);
            btnAdd.setOnClickListener(this);
            etbAdd.setRightLayoutClickListener(this);
            etbAdd.setLeftLayoutClickListener(this);
            query.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        Toast.makeText(AddAccount.this, ""+s.toString(), Toast.LENGTH_SHORT).show();
                        if(s.length()>0){
                              searchClear.setVisibility(View.VISIBLE);
                        }else {
                              searchClear.setVisibility(View.INVISIBLE);
                        }
                  }

                  @Override
                  public void afterTextChanged(Editable s) {

                  }
            });
      }

      @Override
      public void onClick(View v) {
            if (v == etbAdd.getRightLayout()) {
                  //查找friend
                  String trim = query.getText().toString().trim();
                  if (trim != null&&trim.length()>0) {
                        final UserInfo userInfo = new UserInfo();
                        userInfo.setUser(trim);
                        tvAdd.setText(userInfo.getUser());
                        if(!llAdd.isShown()){
                              llAdd.setVisibility(View.VISIBLE);
                        }
                  } else {
                        ShowTipUtil.ShowTip(this, "用户名不能为空");
                  }
            } else if (v == btnAdd) {
                  //添加friend
                  String trim = tvAdd.getText().toString().trim();
                  Log.e(TAG, "onClick: "+trim );

                  final UserInfo userInfo = new UserInfo();
                  userInfo.setUser(trim);
                  ThreadPool.getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                              try {
                                    EMClient.getInstance().contactManager().addContact(userInfo.getUser(), "test");
                                    runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                                ShowTipUtil.ShowTip(AddAccount.this, "添加成功");
                                          }
                                    });
                              } catch (final HyphenateException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                                ShowTipUtil.ShowTip(AddAccount.this, "添加失败" + e.toString());
                                          }
                                    });
                              }
                        }
                  });

            } else if (v == searchClear) {
                  //清除edit
                  query.setText("");
            }else if(v==etbAdd.getLeftLayout()){
                 finish();
            }

      }

      @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
            Log.e(TAG, "onKeyDown: "+keyCode );
            if(event.getKeyCode()==KeyEvent.KEYCODE_DEL){
                if (query.getText().toString().length()==0){
                      if(llAdd.isShown()){
                            llAdd.setVisibility(View.GONE);
                      }
                }
            }
            return super.onKeyDown(keyCode, event);
      }
}
