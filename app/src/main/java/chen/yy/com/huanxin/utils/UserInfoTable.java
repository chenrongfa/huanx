package chen.yy.com.huanxin.utils;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/18
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class UserInfoTable {
      public static final String USER="user";
      public static final String PWD="pwd";
      public static final String PHOTO="photo";
      public static final String NICKNAME="nickname";
      public static final String TABLE="userInfo";

      public static  final  String SQL="create table "+TABLE +"(" +
              USER+" text primary key,"+" text ,"+ PWD+" text," +
              PHOTO+" text,"+ NICKNAME+" text"+
              ")";
}
