package chen.yy.com.huanxin.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.adapter.GroupAdapter;
import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.utils.ShowTipUtil;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

      @BindView(R.id.tv_create)
      TextView tvCreate;
      @BindView(R.id.lv_group)
      ListView lvGroup;
      @BindView(R.id.activity_group)
      LinearLayout activityGroup;
      private GroupAdapter groupAdapter;
      private EditText et_groupName;
      private EditText et_groupDesc;
      private EditText et_reason;
      private EditText et_member;
      private Spinner sn_type;
      private Button btn_create;
      private Button btn_nevigate;
      private Dialog dialog;
      private EMGroupManager.EMGroupOptions emGroupOptions;
      private boolean isFirst=false;
      private Button btn_member;
      List<String> userInfos=new ArrayList<>();
      private String groupName;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_group);
            ButterKnife.bind(this);
            initdata();
            initAdapter();
      }

      private void initdata() {


            lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<EMGroup> allGroups = groupAdapter.getAllGroups();
                        Intent intent=new Intent(GroupActivity.this,ChatAvtivity.class);
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                        intent.putExtra(EaseConstant.EXTRA_USER_ID,allGroups.get(position).getGroupId());
                        startActivity(intent);
                  }
            });

      }

      private void initAdapter() {
            ThreadPool.getThreadPool().execute(new Runnable() {
                  @Override
                  public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        final List<EMGroup> allGroups = EMClient.getInstance().groupManager().getAllGroups();
                        if (allGroups != null) {
                              if(groupAdapter==null)
                              groupAdapter = new GroupAdapter(GroupActivity.this, allGroups);

                           runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                       if(!isFirst){
                                             isFirst=true;
                                             lvGroup.setAdapter(groupAdapter);
                                       }else {
                                             groupAdapter.refresh(allGroups);
                                       }
                                 }
                           });
                        }
                  }
            });
      }

      @OnClick({R.id.tv_create})
      public void onClick(View view) {
            if (tvCreate == view) {
                  initDialog();
                  initSpinnerEvent();
                  dialog.show();
            } else if (btn_create == view) {
                  handleCreate();
            } else if (btn_nevigate == view) {
                  ShowTipUtil.ShowTip(this, "nevigate");
                  dialog.dismiss();
            }else if (btn_member == view) {
                  ShowTipUtil.ShowTip(this, "btn_member");
                 Intent intent =new Intent(GroupActivity.this,ChoiceMemberActivity.class);
                  startActivityForResult(intent,1);
            }

      }

      private static final String TAG = "GroupActivity";
      private void handleCreate() {
            Log.e(TAG, "handleCreate: " );
            groupName = et_groupName.getText().toString().trim();
            if (groupName.length()==0) {
                  Log.e(TAG, "handleCreate: " );
                  ShowTipUtil.ShowTip(this, "群名为空");
                  return;
            }
            String groupDesc = et_groupDesc.getText().toString().trim();
            int num = 0;
            try {
                  num = Integer.parseInt(et_member.getText().toString().trim());
            } catch (Exception e) {
                  e.printStackTrace();
                  return;
            }
            if (num <= 0) {
                  ShowTipUtil.ShowTip(this, "数字太小");
            } else {
                  emGroupOptions.maxUsers = num;

            }
            String reason = et_reason.getText().toString().trim();

            final String finalGroupDesc = groupDesc;
            final String finalReason = reason;
            ThreadPool.getThreadPool().execute(new Runnable() {
                  @Override
                  public void run() {
                        try {
                              String[] s=new String[userInfos.size()];//成员名字 集合
                              EMClient.getInstance().groupManager().createGroup(groupName, finalGroupDesc, userInfos.toArray(s), finalReason,
                                      emGroupOptions);
                              runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                          ShowTipUtil.ShowTip(GroupActivity.this, "创建成功");
                                          //更新数据
                                          dialog.dismiss();
                                          dialog.cancel();
                                          initAdapter();
                                                   }
                              });
                        } catch (HyphenateException e) {
                              e.printStackTrace();
                              runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                          ShowTipUtil.ShowTip(GroupActivity.this, "失败");
                                    }
                              });
                        }
                  }
            });


      }

      private void initSpinnerEvent() {
            sn_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                  @Override
                  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ShowTipUtil.ShowTip(GroupActivity.this, sn_type.getItemAtPosition(position).toString());
                        switch (position) {
                              case 0:
                                    emGroupOptions.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                                    break;
                              case 1:
                                    emGroupOptions.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                                    break;
                              case 2:
                                    emGroupOptions.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                                    break;
                              case 3:
                                    emGroupOptions.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                                    break;

                        }
                  }

                  @Override
                  public void onNothingSelected(AdapterView<?> parent) {

                  }
            });
      }

      private void initDialog() {
            ShowTipUtil.ShowTip(this, "build group");
            dialog = new Dialog(this);
            dialog.setTitle("创建群");
            View inflate = View.inflate(this, R.layout.create_group, null);
            dialog.setContentView(inflate);
            et_groupName = (EditText) inflate.findViewById(R.id.et_groupName);
            et_groupDesc = (EditText) inflate.findViewById(R.id.et_groupDesc);
            btn_member = (Button) inflate.findViewById(R.id.btn_member);
            et_reason = (EditText) inflate.findViewById(R.id.et_reason);
            et_member = (EditText) inflate.findViewById(R.id.et_member);
            sn_type = (Spinner) inflate.findViewById(R.id.sn_type);
            btn_create = (Button) inflate.findViewById(R.id.btn_create);
            btn_nevigate = (Button) inflate.findViewById(R.id.btn_nevigate);
            btn_create.setOnClickListener(GroupActivity.this);
            btn_nevigate.setOnClickListener(GroupActivity.this);
            btn_member.setOnClickListener(GroupActivity.this);

            emGroupOptions = new EMGroupManager.EMGroupOptions();
            String[] values = new String[]{"私有群，只有群主可以邀请人", "私有群，群成员也能邀请人进群", "公开群 只能群主邀请加入", "公开群，任何人都能加入此群"};
            sn_type.setAdapter(new ArrayAdapter<String>(GroupActivity.this, android.R.layout.simple_list_item_1, values));
      }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            //接受选择的好友
            List<UserInfo> userInfos1=new ArrayList<>();
            if(requestCode==1&&resultCode==RESULT_OK){

                  userInfos1= data.getParcelableArrayListExtra("choice");
                  for (UserInfo user: userInfos1
                       ) {
                        userInfos.add(user.getUser());
                  }
            }


      }
}
