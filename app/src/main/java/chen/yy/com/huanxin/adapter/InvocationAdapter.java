package chen.yy.com.huanxin.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.model.bean.GroupInfo;
import chen.yy.com.huanxin.model.bean.InvocationInfo;
import chen.yy.com.huanxin.model.bean.InvocationStatus;
import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.ui.InvocationActivity;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/24
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */
public class InvocationAdapter extends BaseAdapter {
      InvocationActivity invocationActivity;
    private   AcceptListener acceptListener;
      private String reason;

      public void setAcceptListener(AcceptListener acceptListener){
            this.acceptListener=acceptListener;
      }

      public List<InvocationInfo> getInvocations() {
            return invocations;
      }

      public void setInvocations(List<InvocationInfo> invocations) {
            this.invocations = invocations;
      }

      public void refresh(List<InvocationInfo> invocationInfos) {
            if (invocationInfos != null && invocationInfos.size() > 0) {
                  setInvocations(invocationInfos);
                  notifyDataSetChanged();
            }

      }

      private List<InvocationInfo> invocations;

      public InvocationAdapter(InvocationActivity invocationActivity, List<InvocationInfo> invocations) {
            this.invocationActivity = invocationActivity;
            this.invocations = invocations;
      }

      @Override
      public int getCount() {
            return invocations.size();
      }

      @Override
      public Object getItem(int position) {
            return invocations.get(position);
      }

      @Override
      public long getItemId(int position) {
            return 0;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
            if (convertView == null) {
                  convertView = View.inflate(invocationActivity, R.layout.invocation_item, null);
                  viewHolder=new ViewHolder(convertView);
                  convertView.setTag(viewHolder);
            }else{
                  viewHolder= (ViewHolder) convertView.getTag();
            }
            bindata(position, viewHolder);
            initEvent(viewHolder,invocations.get(position));
            return convertView;
      }

      private void bindata(int position, ViewHolder viewHolder) {
            UserInfo userInfo = invocations.get(position).getUserInfo();
            InvocationStatus status = invocations.get(position).getStatus();
            viewHolder.btnAccept.setVisibility(View.GONE);
            viewHolder.btnRefuse.setVisibility(View.GONE);
            reason = invocations.get(position).getReason();
            if(userInfo!=null){
                  viewHolder.tvId.setText(userInfo.getUser());
            }else{
                  GroupInfo groupInfo = invocations.get(position).getGroupInfo();
                  viewHolder.tvId.setText(groupInfo.getCreateName()+"邀请你加入"+groupInfo.getGroupName());
                  viewHolder.btnAccept.setVisibility(View.VISIBLE);
                  viewHolder.btnRefuse.setVisibility(View.VISIBLE);
            }
            if(status==InvocationStatus.newInvocaton){
                  //显示

                  viewHolder.tvReason.setText(reason);
                  viewHolder.btnAccept.setVisibility(View.VISIBLE);
                  viewHolder.btnRefuse.setVisibility(View.VISIBLE);
           }else if (status==InvocationStatus.reject){
                  if (reason!=null){
                        viewHolder.tvReason.setText(reason);
                  }else
                  viewHolder.tvReason.setText("丑拒");

            }else if (status==InvocationStatus.reviecer){
                  if (reason!=null){
                        viewHolder.tvReason.setText(reason);
                  }else
                        viewHolder.tvReason.setText("帅气比人");
            }else if (status==InvocationStatus.new_group){
                  if (reason!=null){
                        viewHolder.tvReason.setText(reason);
                  }else
                        viewHolder.tvReason.setText("加入群");
            }else if (status==InvocationStatus.reject_group){
                  if (reason!=null){
                        viewHolder.tvReason.setText(reason);
                  }else
                        viewHolder.tvReason.setText("拒绝加入群");
            }else if (status==InvocationStatus.reject_group){
                  if (reason!=null){
                        viewHolder.tvReason.setText(reason);
                  }else
                        viewHolder.tvReason.setText("接受加入群");
            }
      }

      private void initEvent(ViewHolder viewHolder, final InvocationInfo invocationInfo) {
            viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        if (acceptListener!=null){
                              acceptListener.accept(invocationInfo);
                        }

                  }
            });
            viewHolder.btnRefuse.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        if (acceptListener!=null){
                              acceptListener.refuse(invocationInfo);
                        }

                  }
            });
      }

      static class ViewHolder {
            @BindView(R.id.tv_id)
            TextView tvId;
            @BindView(R.id.tv_reason)
            TextView tvReason;
            @BindView(R.id.btn_accept)
            Button btnAccept;
            @BindView(R.id.btn_refuse)
            Button btnRefuse;

            ViewHolder(View view) {
                  ButterKnife.bind(this, view);
            }
      }
      public interface AcceptListener{
            void accept(InvocationInfo invocationInfo);
            void refuse(InvocationInfo invocationInfo);

      }
}
