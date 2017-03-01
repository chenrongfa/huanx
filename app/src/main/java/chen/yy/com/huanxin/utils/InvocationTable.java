package chen.yy.com.huanxin.utils;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/23
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class InvocationTable {
      public static final String USER="user";

      public static final String PHOTO="photo";
      public static final String NICKNAME="nickname";
      public static final String GROUPNAME="groupName";
      public static final String CREATENAME="CreateName";
      public static final String REASON="reason";
      public static final String   SUM="sum";//几个人
      public static final String GROUPID="groupId";
      public static final String table="invocation";
      public static  final  String STATUS="status";
      public static  final  String CREATE_SQL= "create table "+table+"  ("
              +USER+" text  ,"+
              PHOTO+ " text,"+
              NICKNAME+ " text,"+
              GROUPNAME+ " text,"+
              CREATENAME+" text,"+
              REASON+" text,"+
              SUM + " integer,"+
              GROUPID +" text ,"+
              STATUS +" integer  )";


}
