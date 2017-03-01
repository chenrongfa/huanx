package chen.yy.com.huanxin.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.model.db.DbHelp;
import chen.yy.com.huanxin.utils.ContactTable;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/23
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class ContactImp {

      private DbHelp contactDb;

      public ContactImp(DbHelp contactDb) {
            this.contactDb = contactDb;


      }

      /**
       * 从数据库查询 所有信息
       *
       * @return
       */
      public List<UserInfo> getContacts() {
            //打开数据库
            SQLiteDatabase readableDatabase = contactDb.getReadableDatabase();
            String sql = "select * from " + ContactTable.TABLE + " where " + ContactTable.ISFRIEND + "=1";
            Cursor cursor = readableDatabase.rawQuery(sql, null);
            //遍历
            List<UserInfo> userInfos = null;
            while (cursor.moveToNext()) {
                  //要用再创建
                  if (userInfos == null) {
                        userInfos = new ArrayList<>();
                  }
                  String user = cursor.getString(cursor.getColumnIndex(ContactTable.USER));
                  String photo = cursor.getString(cursor.getColumnIndex(ContactTable.PHOTO));
                  String pwd = cursor.getString(cursor.getColumnIndex(ContactTable.PWD));
                  String nickName = cursor.getString(cursor.getColumnIndex(ContactTable.NICKNAME));
                  UserInfo userInfo = new UserInfo(photo, nickName, user, pwd);
                  userInfos.add(userInfo);
            }
//关闭游标
            cursor.close();
            return userInfos;

      }


      /**
       * 通过name 查询user 的信息
       *
       * @param name
       * @return
       */
      public UserInfo getContactByName(String name) {
            SQLiteDatabase readableDatabase = contactDb.getReadableDatabase();
            String sql = "select * from " + ContactTable.TABLE + " where " + ContactTable.USER + " =?";
            Cursor cursor = readableDatabase.rawQuery(sql, new String[]{name});
            UserInfo userInfo = null;
            while (cursor.moveToNext()) {
                  String user = cursor.getString(cursor.getColumnIndex(ContactTable.USER));
                  String photo = cursor.getString(cursor.getColumnIndex(ContactTable.PHOTO));
                  String pwd = cursor.getString(cursor.getColumnIndex(ContactTable.PWD));
                  String nickName = cursor.getString(cursor.getColumnIndex(ContactTable.NICKNAME));
                  userInfo = new UserInfo(photo, nickName, user, pwd);
            }
//关闭游标
            cursor.close();

            return userInfo;
      }

      /**
       * 批量保存
       *
       * @param u
       * @param isFriend
       */
      public void saveContactsList(List<UserInfo> u, boolean isFriend) {
            if (u != null && u.size() > 0) {

                  for (UserInfo us : u) {
                        saveContact(us, isFriend);
                  }

            }
      }
      public void saveContactsLists(List<String> u, boolean isFriend) {
            if (u != null && u.size() > 0) {

                  for (String us : u) {
                        UserInfo userInfo = new UserInfo();
                        userInfo.setUser(us);
                        saveContact(userInfo, isFriend);
                  }

            }

      }

      /**
       * 是否存在name 好友
       *
       * @param name
       * @return
       */
      public boolean isHasUser(String name) {
            List<UserInfo> contacts = getContacts();
            if (contacts!=null&&contacts.size()>0) {
                  for (UserInfo u : contacts) {
                        if (u.getUser().equals(name)) return true;
                  }
            }
            return false;
      }

      /**
       * 更新数据  比例拉黑 nickname
       *
       * @param userInfo
       * @param isfriend
       */
      public void update(UserInfo userInfo, boolean isfriend) {
            if (userInfo != null) {
                  if (isHasUser(userInfo.getUser())) {
                        SQLiteDatabase readableDatabase = contactDb.getReadableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(ContactTable.NICKNAME, userInfo.getNickName());
                        values.put(ContactTable.PHOTO, userInfo.getPhoto());
                        values.put(ContactTable.ISFRIEND, isfriend ? 1 : 0);
                        readableDatabase.update(ContactTable.TABLE, values, ContactTable.USER + "=?", new String[]{userInfo.getUser()});
                  }
            }
      }

      public boolean saveContact(UserInfo userInfo, boolean isFriend) {
            //不存在时才执行
//            if (!isHasUser(userInfo.getUser())) {
                  SQLiteDatabase readableDatabase = contactDb.getReadableDatabase();
                  ContentValues values = new ContentValues();
                  values.put(ContactTable.NICKNAME, userInfo.getNickName());
                  values.put(ContactTable.USER, userInfo.getUser());
                  values.put(ContactTable.PWD, userInfo.getPwd());
                  values.put(ContactTable.PHOTO, userInfo.getPhoto());
                  values.put(ContactTable.ISFRIEND, isFriend ? 1 : 0);
                  long insert = readableDatabase.replace(ContactTable.TABLE, null, values);
                  return true;

//            } else {
//                  return false;


      }

      /**
       * 通过 name 删除数据
       *
       * @param name
       */
      public void deleteByName(final String name) {
         if(name!=null) {
               SQLiteDatabase readableDatabase = contactDb.getReadableDatabase();
               readableDatabase.delete(ContactTable.TABLE, ContactTable.USER + " =?", new String[]{name});
         }
      }

}
