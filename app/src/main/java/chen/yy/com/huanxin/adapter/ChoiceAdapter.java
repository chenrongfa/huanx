package chen.yy.com.huanxin.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.model.bean.CheckUser;
import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.ui.ChoiceMemberActivity;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/25
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class ChoiceAdapter extends BaseAdapter implements View.OnClickListener {
      ChoiceMemberActivity choiceMemberActivity;
      List<CheckUser> contacts;
      private CheckBox cbAll;

      public ChoiceAdapter(ChoiceMemberActivity choiceMemberActivity, List<CheckUser> contacts, CheckBox cbAll) {
            this.choiceMemberActivity = choiceMemberActivity;
            this.contacts = contacts;
            this.cbAll = cbAll;
            cbAll.setOnClickListener(this);
            CheckAll();

      }

      /**
       * 设置 cbaall
       */
      private void CheckAll() {
            if (isCheckAll()) {
                  cbAll.setChecked(true);
            } else {
                  cbAll.setChecked(false);
            }
      }

      /**
       * 判断是否为全选
       *
       * @return
       */
      private boolean isCheckAll() {
            for (CheckUser check : contacts) {
                  if (!check.ischecked()) {
                        return false;
                  }

            }

            return true;
      }

      @Override
      public int getCount() {
            return contacts.size();
      }

      @Override
      public Object getItem(int position) {
            return contacts.get(position);
      }

      @Override
      public long getItemId(int position) {
            return 0;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                  convertView = View.inflate(choiceMemberActivity, R.layout.choice_item, null);
                  viewHolder=new ViewHolder(convertView);
                  convertView.setTag(viewHolder);

            }else {
                  viewHolder= (ViewHolder) convertView.getTag();
            }
            final CheckUser checkUser = contacts.get(position);
            bindData(viewHolder, checkUser);
            bindEvent(viewHolder, checkUser);
            return convertView;
      }

      /**
       *  事件的处理
       * @param viewHolder
       * @param checkUser
       */
      private void bindEvent(final ViewHolder viewHolder, final CheckUser checkUser) {
            viewHolder.cbChoice.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                       if(checkUser.ischecked()) {
                             checkUser.setIschecked(false);
                             viewHolder.cbChoice.setChecked(false);

                       }else {
                             checkUser.setIschecked(true);
                             viewHolder.cbChoice.setChecked(true);
                       }
                        CheckAll();
                        notifyDataSetChanged();
                  }
            });
      }

      /**
       *  数据的绑定
       * @param viewHolder
       * @param checkUser
       */
      private void bindData(ViewHolder viewHolder, CheckUser checkUser) {
            viewHolder.tvName.setText(checkUser.getUserInfo().getUser());
            if(checkUser.ischecked()){
                  viewHolder.cbChoice.setChecked(true);
            }else{
                  viewHolder.cbChoice.setChecked(false);
            }
      }

      private static final String TAG = "ChoiceAdapter";
      @Override
      public void onClick(View v) {
            Log.e(TAG, "onClick: " );
            if(isCheckAll()){
                  cbAll.setChecked(false);
                  for (CheckUser user:contacts){
                        if (user.ischecked()){
                              user.setIschecked(false);
                        }
                  }
            }else{
                  cbAll.setChecked(true);
                  for (CheckUser user:contacts){
                        if (!user.ischecked()){
                              user.setIschecked(true);
                        }
                  }
            }
            notifyDataSetChanged();

      }

      static class ViewHolder {
            @BindView(R.id.iv_logo)
            ImageView ivLogo;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.cb_choice)
            CheckBox cbChoice;

            ViewHolder(View view) {
                  ButterKnife.bind(this, view);
            }
      }
      public  List<UserInfo> getChoiceUser(){
                  List<UserInfo> userInfos=new ArrayList<>();
                  for(CheckUser user:contacts){
                        if(user.ischecked()){
                              userInfos.add(user.getUserInfo());
                        }
                  }
            return  userInfos;
      }
}
