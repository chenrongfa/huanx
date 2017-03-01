package chen.yy.com.huanxin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;

import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.model.ThreadPool;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/17
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class SplashActivity extends AppCompatActivity {
      private int sleeptime=2000;
      private ImageView iv_logo;
      EMClient instance;
      Intent intent;
      @Override
      protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splase);
            iv_logo = (ImageView) findViewById(R.id.iv_logo);
            initAnim();

             instance= EMClient.getInstance();


      }

      private void initAnim() {
            RotateAnimation rn=new RotateAnimation(0,360f,0.5f,0.5f);
            rn.setDuration(1000);
            rn.setFillAfter(true);
            iv_logo.startAnimation(rn);
      }

      @Override
      protected void onResume() {
            super.onResume();
            ThreadPool.getThreadPool().execute(new Runnable() {
                  @Override
                  public void run() {
                        if (instance.isLoggedInBefore()){
                              long start=System.currentTimeMillis();
                              instance.groupManager().loadAllGroups();
                              instance.chatManager().loadAllConversations();
                              long cost=System.currentTimeMillis()-start;

                              if(sleeptime-cost>0){
                                    SystemClock.sleep(sleeptime);
                              }else {
                                    SystemClock.sleep(cost);
                              }
                              intent=new Intent(SplashActivity.this,MainActivity.class);
                              startActivity(intent);
                        }else{
                              SystemClock.sleep(sleeptime);
                              intent=new Intent(SplashActivity.this,LoginActivity.class);
                              startActivity(intent);
                        }
                        finish();
                  }
            });

      }
}
