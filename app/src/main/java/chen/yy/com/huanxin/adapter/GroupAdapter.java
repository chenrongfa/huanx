package chen.yy.com.huanxin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.yy.com.huanxin.R;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/25
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class GroupAdapter extends BaseAdapter {
      Context context;

      public List<EMGroup> getAllGroups() {
            return allGroups;
      }

      public void setAllGroups(List<EMGroup> allGroups) {
            this.allGroups = allGroups;
      }

      public void refresh(List<EMGroup> groups) {
            if (groups != null) {
                  setAllGroups(groups);
            notifyDataSetChanged();

            }
      }

      List<EMGroup> allGroups;

      public GroupAdapter(Context context, List<EMGroup> allGroups) {
            this.context = context;
            this.allGroups = allGroups;
      }

      @Override
      public int getCount() {
            return allGroups.size();
      }

      @Override
      public Object getItem(int position) {
            return allGroups.get(position);
      }

      @Override
      public long getItemId(int position) {
            return 0;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                  convertView = View.inflate(context, R.layout.group_item, null);
                  viewHolder=new ViewHolder(convertView);
                  convertView.setTag(viewHolder);

            }else{
                  viewHolder= (ViewHolder) convertView.getTag();

            }
            EMGroup emGroup = allGroups.get(position);

            String description = emGroup.getDescription();
            if(description.length()==0){
                  viewHolder.tvDesc.setText("群介绍:无");
            }else{
                  viewHolder.tvDesc.setText(description);
            }
            viewHolder.tvGroupname.setText(emGroup.getGroupName());
            return convertView;
      }

      static class ViewHolder {
            @BindView(R.id.iv_group_logo)
            ImageView ivGroupLogo;
            @BindView(R.id.tv_groupname)
            TextView tvGroupname;
            @BindView(R.id.tv_desc)
            TextView tvDesc;

            ViewHolder(View view) {
                  ButterKnife.bind(this, view);
            }
      }
}
