package chen.yy.com.huanxin.controller;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.bean.GroupInfo;
import chen.yy.com.huanxin.model.bean.InvocationInfo;
import chen.yy.com.huanxin.model.bean.InvocationStatus;
import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.model.db.DbManager;
import chen.yy.com.huanxin.ui.CallReceiver;
import chen.yy.com.huanxin.utils.Constans;
import chen.yy.com.huanxin.utils.SpUtils;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/23
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class ListenerFriendStatu {
      private static final String TAG = "ListenerFriendStatu";
      private static ListenerFriendStatu listenerFriendStatu = new ListenerFriendStatu();
      private static long lastTime = 0;
      LocalBroadcastManager localBroadcastManager;
      private Application context;
      //      Looper looper=Looper.myLooper();
//      Looper.loop();
      private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                  super.handleMessage(msg);
            }
      };
      private CallReceiver callReceiver;
//      private CallReceiver callReceiver;

      private ListenerFriendStatu() {

      }

      public static ListenerFriendStatu getListenerFriendStatu() {
            return listenerFriendStatu;
      }

      public void init(final Application context) {
            this.context = context;
            localBroadcastManager = LocalBroadcastManager.getInstance(context);

            EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
                  @Override
                  public void onContactAdded(final String s) {
                        ThreadPool.getThreadPool().execute(new Runnable() {
                              @Override
                              public void run() {
                                    //增加好友
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setUser(s);
                                    DbManager.getDbManager().getContactImp().saveContact(userInfo, true);
                                    //发送广播 去更新列表
                                    localBroadcastManager.sendBroadcast(new Intent(Constans.CONTACT_CHANGE));
                              }
                        });

                  }

                  @Override
                  public void onContactDeleted(final String s) {
                        ThreadPool.getThreadPool().execute(new Runnable() {
                              @Override
                              public void run() {
                                    //删除好友
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setUser(s);
                                    DbManager.getDbManager().getContactImp().deleteByName(s);
                                    //发送广播 去更新列表
                                    localBroadcastManager.sendBroadcast(new Intent(Constans.CONTACT_CHANGE));

                              }
                        });
                  }

                  @Override
                  public void onContactInvited(final String s, final String reason) {
                        //发送邀请时 改变邀请状态
                        // 红点处理
                        ThreadPool.getThreadPool().execute(new Runnable() {
                              @Override
                              public void run() {
                                    localBroadcastManager.sendBroadcast(new Intent(Constans.INVOCATION_CHANGE));
                                    InvocationInfo invocationInfo = new InvocationInfo();
                                    invocationInfo.setStatus(InvocationStatus.newInvocaton);
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setUser(s);
                                    invocationInfo.setUserInfo(userInfo);
                                    invocationInfo.setReason(reason);
                                    SpUtils.getSpUtils().save("new_invite", true);
                                    Log.e(TAG, "onContactInvited: " + Thread.currentThread());
                                    long startTime = System.currentTimeMillis();
                                    DbManager.getDbManager().getInvocationImp().addInvocation(invocationInfo);


                              }
                        });
                        //
                  }

                  @Override
                  public void onFriendRequestAccepted(final String s) {
                        ThreadPool.getThreadPool().execute(new Runnable() {
                              @Override
                              public void run() {
                                    SpUtils.getSpUtils().save("new_invite", true);
                                    //
                                    InvocationInfo invocationInfo = new InvocationInfo();
                                    invocationInfo.setReason("邀请被" + s + "接受");
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setUser(s);
                                    invocationInfo.setStatus(InvocationStatus.reviecer);
                                    invocationInfo.setUserInfo(userInfo);

//                        DbManager.getDbManager().getInvocationImp().updateInvocationstatus(InvocationStatus.reviecer, s);
                                    DbManager.getDbManager().getInvocationImp().addInvocation(invocationInfo);
                                    localBroadcastManager.sendBroadcast(new Intent(Constans.INVOCATION_CHANGE));
                              }
                        });


                  }

                  @Override
                  public void onFriendRequestDeclined(final String s) {
                        ThreadPool.getThreadPool().execute(new Runnable() {
                              @Override
                              public void run() {
                                    SpUtils.getSpUtils().save("new_invite", true);
                                    //
                                    InvocationInfo invocationInfo = new InvocationInfo();
                                    invocationInfo.setReason("邀请被" + s + "拒绝");
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setUser(s);
                                    invocationInfo.setUserInfo(userInfo);
                                    invocationInfo.setStatus(InvocationStatus.reject);

//                        DbManager.getDbManager().getInvocationImp().updateInvocationstatus(InvocationStatus.reviecer, s);
                                    DbManager.getDbManager().getInvocationImp().addInvocation(invocationInfo);
//                        DbManager.getDbManager().getInvocationImp().updateInvocationstatus(InvocationStatus.reject, s);
                                    localBroadcastManager.sendBroadcast(new Intent(Constans.INVOCATION_CHANGE));

                              }
                        });

                  }
            });

            EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
                  @Override
                  public void onInvitationReceived(final String groupId, final String groupName, final String inviter, final String
                          reason) {
                        ThreadPool.getThreadPool().execute(new Runnable() {
                              @Override
                              public void run() {
                                    InvocationInfo invocations = new InvocationInfo();
                                    GroupInfo group = new GroupInfo();
                                    try {
                                          EMGroup name = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
//                                       group.setGroupName(groupName);
                                          Log.e(TAG, "run: " + groupName);
                                          Log.e(TAG, "run: " + name.getGroupName());
                                          group.setGroupName(name.getGroupName());
                                          group.setGroupId(groupId);
                                          group.setCreateName(inviter);
                                          invocations.setReason(reason);
                                          invocations.setGroupInfo(group);
                                          invocations.setStatus(InvocationStatus.new_group);
                                          SpUtils.getSpUtils().save("new_invite", true);
                                          DbManager.getDbManager().getInvocationImp().addInvocation(invocations);
                                          localBroadcastManager.sendBroadcast(new Intent(Constans.INVOCATION_CHANGE));
                                    } catch (HyphenateException e) {
                                          e.printStackTrace();
                                    }

                              }
                        });

                        //红点


                  }

                  @Override
                  public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {

                  }

                  @Override
                  public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

                  }

                  @Override
                  public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {

                  }

                  @Override
                  public void onInvitationAccepted(String groupId, String inviter, String reason) {

                  }

                  @Override
                  public void onInvitationDeclined(String groupId, String invitee, String reason) {

                  }

                  @Override
                  public void onUserRemoved(String groupId, String groupName) {

                  }

                  @Override
                  public void onGroupDestroyed(String groupId, String groupName) {

                  }

                  @Override
                  public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {

                  }
            });

      }



}
