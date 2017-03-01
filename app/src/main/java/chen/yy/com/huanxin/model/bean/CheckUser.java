package chen.yy.com.huanxin.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/27
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class CheckUser implements Parcelable {
      private UserInfo userInfo;

      @Override
      public String toString() {
            return "CheckUser{" +
                    "userInfo=" + userInfo.toString() +
                    ", ischecked=" + ischecked +
                    '}';
      }

      private boolean ischecked=true; //默认选择

      protected CheckUser(Parcel in) {
            ischecked = in.readByte() != 0;
      }

      public static final Creator<CheckUser> CREATOR = new Creator<CheckUser>() {
            @Override
            public CheckUser createFromParcel(Parcel in) {
                  return new CheckUser(in);
            }

            @Override
            public CheckUser[] newArray(int size) {
                  return new CheckUser[size];
            }
      };

      public UserInfo getUserInfo() {
            return userInfo;
      }

      public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
      }

      public boolean ischecked() {
            return ischecked;
      }

      public void setIschecked(boolean ischecked) {
            this.ischecked = ischecked;
      }

      public CheckUser(UserInfo userInfo) {
            this.userInfo = userInfo;
      }

      @Override
      public int describeContents() {
            return 0;
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (ischecked ? 1 : 0));
      }
}
