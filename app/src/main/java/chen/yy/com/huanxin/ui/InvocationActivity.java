package chen.yy.com.huanxin.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.adapter.InvocationAdapter;
import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.bean.InvocationInfo;
import chen.yy.com.huanxin.model.bean.InvocationStatus;
import chen.yy.com.huanxin.model.db.DbManager;
import chen.yy.com.huanxin.utils.Constans;
import chen.yy.com.huanxin.utils.ShowTipUtil;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/23
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */
public class InvocationActivity extends AppCompatActivity {
      @BindView(R.id.etb_invocation)
      EaseTitleBar etbInvocation;
      @BindView(R.id.lv_invocation)
      ListView lvInvocation;
      private InvocationAdapter invocationAdapter;
      private LocalBroadcastManager localBroadcastManager;
      private AcceptBroadcast acceptBroadcast = new AcceptBroadcast();
      private RelativeLayout rightLayout;

      @Override
      protected void onCreate(@Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.invocation);
            ButterKnife.bind(this);
            initData();


      }

      private void initData() {
            etbInvocation.getLeftLayout().setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        finish();
                  }
            });
            //right of event
            etbInvocation.setRightLayoutClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        if (invocationAdapter != null) {
                              List<InvocationInfo> invocations = invocationAdapter.getInvocations();
                              for (InvocationInfo info : invocations) {
                                    DbManager.getDbManager().getInvocationImp().removeByName(info.getUserInfo().getUser());
                              }
                              invocations.clear();
                              invocationAdapter.setInvocations(invocations);
                              invocationAdapter.notifyDataSetChanged();
                        }
                  }
            });
            //long event remove
            lvInvocation.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                  @Override
                  public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InvocationActivity.this);

                        alertDialog.setTitle("是否删除");
                        alertDialog.setNegativeButton("是", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                    if (invocationAdapter != null) {
                                          List<InvocationInfo> invocations = invocationAdapter.getInvocations();
                                                DbManager.getDbManager().getInvocationImp().removeByName(invocations.get(position).getUserInfo().getUser());
                                              invocations.remove(position);
                                          invocationAdapter.setInvocations(invocations);
                                          invocationAdapter.notifyDataSetChanged();
                                    }


//
                              }
                        });


                        alertDialog.setPositiveButton("否", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog alertDialog1 = alertDialog.create();
                                    alertDialog1.dismiss();
                              }
                        });
                        alertDialog.show();
                        return true;
                  }
            });

            List<InvocationInfo> invocations = DbManager.getDbManager().getInvocationImp().getInvocations();
            if (invocations != null && invocations.size() > 0) {
                  invocationAdapter = new InvocationAdapter(this, invocations);
                  lvInvocation.setAdapter(invocationAdapter);
                  //设置 accept 和refuse 的逻辑
                  invocationAdapter.setAcceptListener(new InvocationAdapter.AcceptListener() {
                        @Override
                        public void accept(final InvocationInfo invocationInfo) {
                              ThreadPool.getThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                          try {
                                                String user = invocationInfo.getUserInfo().getUser();
                                                if (user != null) {
                                                      EMClient.getInstance().contactManager().acceptInvitation(user);
                                                      DbManager.getDbManager().getInvocationImp().updateInvocationstatus(InvocationStatus
                                                              .reviecer, user);
                                                }else{
                                                      EMClient.getInstance().groupManager().acceptInvitation(invocationInfo.getGroupInfo().getGroupId(),invocationInfo
                                                      .getGroupInfo().getCreateName());
                                                      DbManager.getDbManager().getInvocationImp().updateInvocationstatus(InvocationStatus.reviever_group,
                                                              invocationInfo.getGroupInfo().getGroupName());
                                                }
                                                runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                            refresh();
                                                            ShowTipUtil.ShowTip(InvocationActivity.this, "成功接受");
                                                      }
                                                });
                                          } catch (final HyphenateException e) {
                                                e.printStackTrace();
                                                runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                            ShowTipUtil.ShowTip(InvocationActivity.this, "接受失败" + e.toString());
                                                      }
                                                });
                                          }
                                    }
                              });
                        }

                        @Override
                        public void refuse(final InvocationInfo invocationInfo) {
                              ThreadPool.getThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                          try {
                                                String user = invocationInfo.getUserInfo().getUser();
                                                if (user != null) {
                                                      EMClient.getInstance().contactManager().declineInvitation(user);
                                                      DbManager.getDbManager().getInvocationImp().updateInvocationstatus(InvocationStatus
                                                              .reject, user);

                                                      runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                  refresh();
                                                                  ShowTipUtil.ShowTip(InvocationActivity.this, "成功拒绝");
                                                            }
                                                      });
                                                }
                                          } catch (final HyphenateException e) {
                                                e.printStackTrace();
                                                runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                            ShowTipUtil.ShowTip(InvocationActivity.this, "拒绝失败" + e.toString());
                                                      }
                                                });
                                          }

                                    }
                              });

                        }
                  });

            }
            //注册广播
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.registerReceiver(acceptBroadcast, new IntentFilter(Constans.INVOCATION_CHANGE));
      }

      //更新数据
      public void refresh() {
            List<InvocationInfo> invocations = DbManager.getDbManager().getInvocationImp().getInvocations();
            invocationAdapter.refresh(invocations);

      }

      @Override
      protected void onDestroy() {
            super.onDestroy();
            //解除广播
            localBroadcastManager.unregisterReceiver(acceptBroadcast);
      }
      class AcceptBroadcast extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                  List<InvocationInfo> invocations = DbManager.getDbManager().getInvocationImp().getInvocations();
                  if (invocationAdapter == null) {
                        invocationAdapter = new InvocationAdapter(InvocationActivity.this, invocations);
                  } else {
                        invocationAdapter.refresh(invocations);
                  }
            }
      }
}
