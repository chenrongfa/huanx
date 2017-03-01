package chen.yy.com.huanxin.model.bean;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/21
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class GroupInfo {
      private String groupName;
      private String CreateName;
      private int   sum;//几个人
      private  String invatePerson;
      private String groupId;

      @Override
      public String toString() {
            return "GroupInfo{" +
                    "groupName='" + groupName + '\'' +
                    ", CreateName='" + CreateName + '\'' +
                    ", sum=" + sum +
                    ", invatePerson='" + invatePerson + '\'' +
                    ", groupId='" + groupId + '\'' +
                    '}';
      }

      public String getGroupName() {
            return groupName;
      }

      public void setGroupName(String groupName) {
            this.groupName = groupName;
      }

      public String getCreateName() {
            return CreateName;
      }

      public void setCreateName(String createName) {
            CreateName = createName;
      }

      public int getSum() {
            return sum;
      }

      public void setSum(int sum) {
            this.sum = sum;
      }

      public String getInvatePerson() {
            return invatePerson;
      }

      public void setInvatePerson(String invatePerson) {
            this.invatePerson = invatePerson;
      }

      public String getGroupId() {
            return groupId;
      }

      public void setGroupId(String groupId) {
            this.groupId = groupId;
      }

      public GroupInfo() {
      }

      public GroupInfo(String groupName, String createName, int sum, String invatePerson, String groupId) {
            this.groupName = groupName;
            CreateName = createName;
            this.sum = sum;
            this.invatePerson = invatePerson;
            this.groupId = groupId;
      }
}
