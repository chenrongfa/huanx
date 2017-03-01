package chen.yy.com.huanxin.model.db;

import android.content.Context;

import chen.yy.com.huanxin.model.dao.ContactImp;
import chen.yy.com.huanxin.model.dao.InvocationImp;
import chen.yy.com.huanxin.model.dao.UserInfoImp;

/**
 *
 * 数据库管理类
 * huanxin
 * Created by chenrongfa on 2017/2/23
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class DbManager {
      private static DbManager dbManager;
      private  Context context;
      private  static DbHelp  dbHelp;

      public ContactImp getContactImp() {
            return contactImp;
      }

      public InvocationImp getInvocationImp() {
            return invocationImp;
      }

      public UserInfoImp getUserInfoImp() {
            return userInfoImp;
      }

      private ContactImp contactImp;
      private InvocationImp invocationImp;
      private UserInfoImp userInfoImp;
      private  DbManager(){
            contactImp=new ContactImp(dbHelp);
      invocationImp=new InvocationImp(dbHelp);
      userInfoImp=new UserInfoImp(dbHelp);
}
public static void init(DbHelp db){
      dbHelp = db;
}
      public static  DbManager getDbManager(){
            if (dbManager==null){
                  dbManager=new DbManager();
                  return dbManager;
            }else {
                  return dbManager;
            }

      }
      public void closeAll(){
            if (dbHelp!=null) {
                  dbHelp.close();
            dbHelp=null;
            }
      }
}
