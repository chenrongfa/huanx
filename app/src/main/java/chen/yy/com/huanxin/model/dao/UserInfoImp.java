package chen.yy.com.huanxin.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.model.db.DbHelp;
import chen.yy.com.huanxin.utils.UserInfoTable;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/18
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class UserInfoImp {
      private static Context context;
      private DbHelp userInfoDb;
      public UserInfoImp(DbHelp dbHelp){
            userInfoDb=dbHelp;
      }

//      public void init(Context context){
//            this.context=context;
//            userInfoDb = new UserInfoDb(this.context, null, null, 1);
//
//      }
//并没有更改密码和删除用户能力?/????/
      /**
       *  注册用户是先判断数据库是否存在
       * @param userInfo
       */
      private static final String TAG = "UserInfoImp";
      public String addAccount(final UserInfo userInfo){
            String result=null;

             SQLiteDatabase readableDatabase = userInfoDb.getReadableDatabase();

            //存在提示用户用户名已经存在
            if(hasUser(userInfo.getUser())){
                  result= "用户已经存在";
            }else {
                  //新服务器和数据库向添加用户
//                  boolean b = ThreadPool.addAccount(userInfo.getUser(), userInfo.getPwd());
//                  Log.e(TAG, "addAccount: "+b );
//                  if(b){
                        ContentValues values=new ContentValues();
                        values.put(UserInfoTable.USER,userInfo.getUser());
                        values.put(UserInfoTable.PWD,userInfo.getPwd());
                        values.put(UserInfoTable.PHOTO,userInfo.getPhoto());
                        values.put(UserInfoTable.NICKNAME,userInfo.getNickName());
                        readableDatabase.insert(UserInfoTable.TABLE,null,values);
                        result= "success";

            }
         return result;
      }
      //是否有这样的用户名
    public boolean  hasUser(String userName){
          boolean isUser=false;
          SQLiteDatabase readableDatabase = userInfoDb.getReadableDatabase();
          String sql="select * from "+ UserInfoTable.TABLE +" where "+UserInfoTable.USER+"=?";
          Cursor cursor = readableDatabase.rawQuery(sql, new String[]{userName});

          if (cursor.moveToNext()){
                isUser=true;
                Log.e(TAG, "hasUser: "+cursor );
          }else {
                Log.e(TAG, "hasUser: meiyou" );
                isUser=false;
          }
     cursor.close();
   return isUser;
    }


}
