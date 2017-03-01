package chen.yy.com.huanxin.ui;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.domain.BookFragment;
import chen.yy.com.huanxin.domain.ConversionFragment;
import chen.yy.com.huanxin.domain.settingsFragment;
import chen.yy.com.huanxin.utils.ShowTipUtil;


public class MainActivity extends AppCompatActivity  {



      @BindView(R.id.fl_container)
      FrameLayout flContainer;
      @BindView(R.id.rb_comment)
      RadioButton rbComment;
      @BindView(R.id.rb_book)
      RadioButton rbBook;
      @BindView(R.id.rb_settings)
      RadioButton rbSettings;
      @BindView(R.id.rg_all)
      RadioGroup rgAll;
      @BindView(R.id.activity_main)
      LinearLayout activityMain;
      private ArrayList<Fragment> baseFragmentList;
      private  int currposition=0;//默认为零
      private Fragment currfragment;
      private CallReceiver callReceiver;


      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //添加网络链接监听
            EMClient.getInstance().addConnectionListener(new MyConnectionListener());
            ButterKnife.bind(this);
            initFragment();
            initEvent();
            rgAll.check(R.id.rb_comment);
            //注册呼入语音
            IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
            callReceiver = new CallReceiver();
            registerReceiver(callReceiver, callFilter);


      }

      private static final String TAG = "MainActivity";

      private void initEvent() {
            rgAll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i){
                              case R.id.rb_book:
                                    Log.e(TAG, "onCheckedChanged: rb_book"+i );
                                    currposition=1;
                                    break;
                              case R.id.rb_comment:
                                    Log.e(TAG, "onCheckedChanged:rb_comment "+i );
                                    currposition=0;
                                    break;    case R.id.rb_settings:
                                    Log.e(TAG, "onCheckedChanged: rb_settings"+i);
                                    currposition=2;
                                    break;



                        }
                      Fragment nextFragment=getFragment(currposition);
                        switchBasefragment(currfragment,nextFragment);

                  }


            });


      }

      private Fragment getFragment(int currposition) {
            if(baseFragmentList!=null&&baseFragmentList.size()>=currposition)
                 return baseFragmentList.get(currposition);
            return null;
      }

      public void switchBasefragment(Fragment pre,Fragment next){
            if(pre!=next){
                  Log.e(TAG, "switchBasefragment: "+"jinlai" );
                  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                  if(next!=null){
                        Log.e(TAG, "switchBasefragment: "+"jinlai1" );
                        if(next.isAdded()&&next.isHidden()){
                              fragmentTransaction.show(next);
                              Log.e(TAG, "switchBasefragment: "+"jinlai2" );
                        }else{
                              Log.e(TAG, "switchBasefragment: "+"jinlai3" );
                              fragmentTransaction.add(R.id.fl_container,next);
                        }
                        if(pre!=null){
                              Log.e(TAG, "switchBasefragment: "+"jinlai4" );
                              fragmentTransaction.hide(pre);
                        }

                  }
                  Log.e(TAG, "switchBasefragment: ");
                  //提交
                  currfragment=next;
                  fragmentTransaction.commit();

            }

      }

      private void initFragment() {
            baseFragmentList=new ArrayList<>();
            baseFragmentList.add(new ConversionFragment());
            BookFragment bookFragment = new BookFragment();
//            bookFragment.setContactsMap();
//            bookFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
//                  @Override
//                  public void onListItemClicked(EaseUser user) {
////                        startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
//                  }
//            });
            baseFragmentList.add(bookFragment);
            baseFragmentList.add(new settingsFragment());
      }

      public void click(View view) {
            EMClient.getInstance().logout(true);
      }

      private class MyConnectionListener implements EMConnectionListener {
            @Override
            public void onConnected() {
            }
            @Override
            public void onDisconnected(final int error) {
                  runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                              if(error == EMError.USER_REMOVED){
                                    // 显示帐号已经被移除
                                    ShowTipUtil.ShowTip(MainActivity.this,"账号被删除");
                                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                              }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                                    // 显示帐号在其他设备登录
                                    ShowTipUtil.ShowTip(MainActivity.this,"另一台设备在登录");
                                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                              } else {
                                    if (NetUtils.hasNetwork(MainActivity.this)) {
                                          //连接不到聊天服务器
                                          ShowTipUtil.ShowTip(MainActivity.this,"连接不到聊天服务器");
                                    }
                                    else {
                                          //当前网络不可用，请检查网络设置
                                          ShowTipUtil.ShowTip(MainActivity.this,"当前网络不可用，请检查网络设置");
                                    }
                              }
                        }
                  });
            }
      }

      @Override
      protected void onDestroy() {
            super.onDestroy();
            //解除广播
          unregisterReceiver(callReceiver);
      }
}
