package chen.yy.com.huanxin.model.bean;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/21
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class InvocationInfo {
      private UserInfo userInfo;
      private GroupInfo groupInfo;
      private InvocationStatus status;

      public String getReason() {
            return reason;
      }

      public void setReason(String reason) {
            this.reason = reason;
      }

      private  String reason;

      public UserInfo getUserInfo() {
            return userInfo;
      }

      public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
      }

      public GroupInfo getGroupInfo() {
            return groupInfo;
      }

      public void setGroupInfo(GroupInfo groupInfo) {
            this.groupInfo = groupInfo;
      }

      public InvocationStatus getStatus() {
            return status;
      }

      public void setStatus(InvocationStatus status) {
            this.status = status;
      }

      public InvocationInfo() {
      }

      public InvocationInfo(InvocationStatus status, UserInfo userInfo, GroupInfo groupInfo) {
            this.status = status;
            this.userInfo = userInfo;
            this.groupInfo = groupInfo;
      }

      @Override
      public String toString() {
            return "InvocationInfo{" +
                    "userInfo=" + userInfo +
                    ", groupInfo=" + groupInfo +
                    ", status=" + status +
                    '}';
      }
}
