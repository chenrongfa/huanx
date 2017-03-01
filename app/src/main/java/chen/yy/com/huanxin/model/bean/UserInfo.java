package chen.yy.com.huanxin.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/18
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class UserInfo implements Parcelable{
      private String user;
      private String pwd;
      private String  nickName;
      private  String photo;

      public UserInfo(String user) {
            this.user = user;
      }

      public UserInfo() {
      }

      public UserInfo(String photo, String nickName, String user, String pwd) {
            this.photo = photo;
            this.nickName = nickName;
            this.user = user;
            this.pwd = pwd;

      }

      protected UserInfo(Parcel in) {
            user = in.readString();
            pwd = in.readString();
            nickName = in.readString();
            photo = in.readString();
      }

      @Override
      public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(user);
            dest.writeString(pwd);
            dest.writeString(nickName);
            dest.writeString(photo);
      }

      @Override
      public int describeContents() {
            return 0;
      }

      public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
            @Override
            public UserInfo createFromParcel(Parcel in) {
                  return new UserInfo(in);
            }

            @Override
            public UserInfo[] newArray(int size) {
                  return new UserInfo[size];
            }
      };

      public String getUser() {
            return user;
      }

      public void setUser(String user) {
            this.user = user;
      }

      public String getPwd() {
            return pwd;
      }

      public void setPwd(String pwd) {
            this.pwd = pwd;
      }

      public String getNickName() {
            return nickName;
      }

      public void setNickName(String nickName) {
            this.nickName = nickName;
      }

      public String getPhoto() {
            return photo;
      }

      public void setPhoto(String photo) {
            this.photo = photo;
      }

      @Override
      public String toString() {
            return "UserInfo{" +
                    "user='" + user + '\'' +
                    ", pwd='" + pwd + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", photo='" + photo + '\'' +
                    '}';
      }
}
