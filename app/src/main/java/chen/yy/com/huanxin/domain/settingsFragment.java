package chen.yy.com.huanxin.domain;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.controller.ListenerFriendStatu;
import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.db.DbManager;
import chen.yy.com.huanxin.ui.LoginActivity;
import chen.yy.com.huanxin.utils.ShowTipUtil;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/18
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class settingsFragment extends BaseFragment {
      @BindView(R.id.tv_loginout)
      TextView tvLoginout;
      private static final String TAG = "settingsFragment";

      @Override
      public View initView() {
            View inflate = inflater.inflate(R.layout.settings_fragment, null);
            ButterKnife.bind(this, inflate);
            return inflate;
      }

      @Override
      public void bindata() {
            super.bindata();
            ThreadPool.getThreadPool().execute(new Runnable() {
                  @Override
                  public void run() {
                        final String currentUser = EMClient.getInstance().getCurrentUser();
                        getActivity().runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                    tvLoginout.setText("当前用户:"+currentUser);
                              }
                        });
                  }
            });



      }

      @OnClick(R.id.tv_loginout)
      public void onClick() {
            ThreadPool.getThreadPool().execute(new Runnable() {
                  @Override
                  public void run() {
                        EMClient.getInstance().logout(false, new EMCallBack() {
                              @Override
                              public void onSuccess() {
                                    Log.e(TAG, "onSuccess: "+Thread.currentThread().getName());
                                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                                    getActivity().startActivity(intent);
                                    getActivity().runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                                ShowTipUtil.ShowTip(getActivity(),"退出成功");
                                                getActivity().finish();
                                          }
                                    });
// close database
                                    DbManager.getDbManager().closeAll();
                                    //close listener
                                    ListenerFriendStatu listenerFriendStatu = ListenerFriendStatu.getListenerFriendStatu();
                                    listenerFriendStatu=null;

                              }

                              @Override
                              public void onError(int i, String s) {
                                    ShowTipUtil.ShowTip(getActivity(),s+i);
                              }

                              @Override
                              public void onProgress(int i, String s) {

                                    Log.e(TAG, "onSuccess: "+Thread.currentThread().getName());
                              }
                        });
                  }
            });
      }
}
