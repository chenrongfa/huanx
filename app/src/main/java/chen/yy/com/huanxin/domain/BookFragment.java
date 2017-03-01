package chen.yy.com.huanxin.domain;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.db.DbManager;
import chen.yy.com.huanxin.ui.AddAccount;
import chen.yy.com.huanxin.ui.ChatAvtivity;
import chen.yy.com.huanxin.ui.GroupActivity;
import chen.yy.com.huanxin.ui.InvocationActivity;
import chen.yy.com.huanxin.utils.Constans;
import chen.yy.com.huanxin.utils.ShowTipUtil;
import chen.yy.com.huanxin.utils.SpUtils;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/18
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class BookFragment extends EaseContactListFragment implements View.OnClickListener {
      private static final String TAG = "BookFragment";
      private static final int DELETE = 0;
      private static final int ERROR = 1;
      private final LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getContext());
      private TextView tv_invocation;
      private TextView tv_group;
      private ImageView iv_redcircle;
      private RedHot redHotbroadcast = new RedHot();
      private ContactBroadcast contactBroadcast = new ContactBroadcast();
      private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                  super.handleMessage(msg);
                  switch (msg.what) {
                        case DELETE:
                              ShowTipUtil.ShowTip(getActivity(), "删除成功");
                              break;

                        case ERROR:
                              ShowTipUtil.ShowTip(getActivity(), "删除错误");
                              break;

                  }
            }
      };

      @Override
      protected void initView() {
            super.initView();
            titleBar.setRightImageResource(R.drawable.em_add);
            titleBar.setRightLayoutClickListener(this);
            View inflate = View.inflate(getActivity(), R.layout.contact_heard, null);
            tv_invocation = (TextView) inflate.findViewById(R.id.tv_invocation);
            tv_group = (TextView) inflate.findViewById(R.id.tv_group);
            tv_group.setOnClickListener(this);
            tv_invocation.setOnClickListener(this);
            iv_redcircle = (ImageView) inflate.findViewById(R.id.iv_redcircle);

            listView.addHeaderView(inflate);
      }

      @Override
      protected void setUpView() {
            //单机item 跳转到聊天页面 要在setupview 之前设置

            setContactListItemClickListener(new EaseContactListItemClickListener() {
                  @Override
                  public void onListItemClicked(EaseUser user) {
                        Intent intent=new Intent( getActivity(), ChatAvtivity.class);
                        intent.putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername());
                        startActivity(intent);
                  }
            });
            super.setUpView();
            resolveHot();
            instance.registerReceiver(redHotbroadcast, new IntentFilter(Constans.INVOCATION_CHANGE));
            instance.registerReceiver(contactBroadcast, new IntentFilter(Constans.CONTACT_CHANGE));
            getNetData();
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                  @Override
                  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        itemLongEvent(position);
                        return true;
                  }
            });











      }

      private void itemLongEvent(int position) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            final EaseUser easeUser = (EaseUser) listView.getItemAtPosition(position);
            alertDialog.setTitle("是否删除" + easeUser.getUsername());
            alertDialog.setNegativeButton("是", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                        ThreadPool.getThreadPool().execute(new Runnable() {
                              @Override
                              public void run() {
                                    try {
                                          EMClient.getInstance().contactManager().deleteContact(easeUser.getUsername());
                                          DbManager.getDbManager().getContactImp().deleteByName(easeUser.getUsername());
                                         getNetData();
                                          handler.sendEmptyMessage(DELETE);
                                    } catch (HyphenateException e) {
                                          e.printStackTrace();
                                          Message message = Message.obtain();
                                          message.obj = e.toString();
                                          message.what = ERROR;
                                          handler.sendMessage(message);
                                    }
                              }
                        });

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
      }

      private void getNetData() {
            ThreadPool.getThreadPool().execute(new Runnable() {
                  @Override
                  public void run() {
                        try {
                              final List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                              //保存
                              if (usernames != null && usernames.size() >=0) {
                                    DbManager.getDbManager().getContactImp().saveContactsLists(usernames, true);
                                    // 更新
                                    Map<String, EaseUser> contactsMap = new HashMap<String, EaseUser>();
                                    for (String s : usernames) {
                                          contactsMap.put(s, new EaseUser(s));
                                    }           setContactsMap(contactsMap);
                                    getActivity().runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                                refresh();
                                          }
                                    });
                              }
                        } catch (HyphenateException e) {
                              e.printStackTrace();
                              ShowTipUtil.ShowTip(getActivity(), "获取不到数据");


                        }
                  }
            });


      }

      private void resolveHot() {
            boolean new_invite = SpUtils.getSpUtils().getBoolean("new_invite", false);
            if (new_invite) {
                  iv_redcircle.setVisibility(View.VISIBLE);
            } else {
                  iv_redcircle.setVisibility(View.GONE);
            }
      }

      @Override
      public void onClick(View v) {
            if (v == titleBar.getRightLayout()) {
                  Intent intent = new Intent(getActivity(), AddAccount.class);
                  getActivity().startActivity(intent);
            } else if (v == tv_group) {
                  //跳转到群组页面
                  Intent intent =new Intent(getActivity(),GroupActivity.class);
                  startActivity(intent);

            } else if (v == tv_invocation) {
                  ShowTipUtil.ShowTip(getActivity(), "invocation");
                  //set red hot and record status;
                  Intent intent = new Intent(getActivity(), InvocationActivity.class);
                  getActivity().startActivity(intent);
                  iv_redcircle.setVisibility(View.GONE);
                  SpUtils.getSpUtils().save("new_invite", false);


            }
      }

      @Override
      public void onDestroy() {
            super.onDestroy();
            //解除guangb
            instance.unregisterReceiver(redHotbroadcast);
            instance.unregisterReceiver(contactBroadcast);

      }

      /**
       *  红点广播处理
       */
      private class RedHot extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                  resolveHot();
                  Log.e(TAG, "onReceive: ");
            }
      }

      /**
       * 联系人广播变化处理
       */
      private class ContactBroadcast extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {

                  getNetData();
            }
      }
}
