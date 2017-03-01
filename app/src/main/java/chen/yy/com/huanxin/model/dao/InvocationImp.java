package chen.yy.com.huanxin.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.bean.GroupInfo;
import chen.yy.com.huanxin.model.bean.InvocationInfo;
import chen.yy.com.huanxin.model.bean.InvocationStatus;
import chen.yy.com.huanxin.model.bean.UserInfo;
import chen.yy.com.huanxin.model.db.DbHelp;
import chen.yy.com.huanxin.utils.InvocationTable;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/23
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class InvocationImp {
      private DbHelp dbHelp;

      public DbHelp getDbHelp() {
            return dbHelp;
      }

      public void setDbHelp(DbHelp dbHelp) {
            this.dbHelp = dbHelp;
      }

      public InvocationImp(DbHelp dbHelp) {
            this.dbHelp = dbHelp;
      }

      public void addInvocation(final InvocationInfo invocationInfo) {
            if (invocationInfo != null) {
                  final SQLiteDatabase readableDatabase = dbHelp.getReadableDatabase();
                  final ContentValues values = new ContentValues();
                  values.put(InvocationTable.STATUS, invocationInfo.getStatus().ordinal());
                  values.put(InvocationTable.REASON, invocationInfo.getReason());
                  if (invocationInfo.getUserInfo() != null) {
                        values.put(InvocationTable.USER, invocationInfo.getUserInfo().getUser());
                        values.put(InvocationTable.NICKNAME, invocationInfo.getUserInfo().getNickName());
                        values.put(InvocationTable.PHOTO, invocationInfo.getUserInfo().getPhoto());
                  }else{
                        ThreadPool.getThreadPool().execute(new Runnable() {
                              @Override
                              public void run() {
                                    String groupId = invocationInfo.getGroupInfo().getGroupId();

                                    values.put(InvocationTable.GROUPNAME, invocationInfo.getGroupInfo().getGroupName());
                                    values.put(InvocationTable.GROUPID, groupId);
                                    values.put(InvocationTable.CREATENAME, invocationInfo.getGroupInfo().getCreateName());
                                    readableDatabase.replace(InvocationTable.table, null, values);
                                    return;
                              }
                        });



                  }
                  readableDatabase.replace(InvocationTable.table, null, values);


            }


      }

      /**
       *   获取邀请表的信息
       * @return
       */
      public List<InvocationInfo> getInvocations() {

            SQLiteDatabase readableDatabase = dbHelp.getReadableDatabase();
            String sql = "select * from " + InvocationTable.table;
            Cursor cursor = readableDatabase.rawQuery(sql, null);
            List<InvocationInfo> invocations = new ArrayList<>();
            while (cursor.moveToNext()) {
                  InvocationInfo info = new InvocationInfo();
                  int status = cursor.getInt(cursor.getColumnIndex(InvocationTable.STATUS));
                  InvocationStatus invocationStatus = intToEum(status);
                  info.setStatus(invocationStatus);
                  String reason = cursor.getString(cursor.getColumnIndex(InvocationTable.REASON));
                  info.setReason(reason);
                  String user = cursor.getString(cursor.getColumnIndex(InvocationTable.USER));
                  if (user != null) {

                        String nickName = cursor.getString(cursor.getColumnIndex(InvocationTable.NICKNAME));
                        String photo = cursor.getString(cursor.getColumnIndex(InvocationTable.PHOTO));
                        UserInfo userInfo = new UserInfo(photo, nickName, user, null);
                        info.setUserInfo(userInfo);
                  } else {
                        String groupName = cursor.getString(cursor.getColumnIndex(InvocationTable.GROUPNAME));
                        String groupId = cursor.getString(cursor.getColumnIndex(InvocationTable.GROUPID));
                        GroupInfo groupInfo = new GroupInfo();
                        groupInfo.setGroupId(groupId);
                        groupInfo.setGroupName(groupName);
                        info.setGroupInfo(groupInfo);
                  }


                  invocations.add(info);


            }
            cursor.close();
            return invocations;
      }

      /**
       *  enum 转换int
       * @param status
       * @return
       */
      private InvocationStatus intToEum(int status) {
            if (status == InvocationStatus.newInvocaton.ordinal()) {
                  return InvocationStatus.newInvocaton;
            } else if (status == InvocationStatus.reject.ordinal()) {

                  return InvocationStatus.reject;
            } else if (status == InvocationStatus.reviecer.ordinal()) {

                  return InvocationStatus.reviecer;
            } else if (status == InvocationStatus.new_group.ordinal()) {

                  return InvocationStatus.new_group;
            } else if (status == InvocationStatus.reject_group.ordinal()) {

                  return InvocationStatus.reject_group;
            } else if (status == InvocationStatus.reviever_group.ordinal()) {

                  return InvocationStatus.reviever_group;
            }


            return null;
      }

      /**
       * 通过名字删除
       *
       * @param name
       */
      public void removeByName(String name) {
            if (name != null) {
                  SQLiteDatabase readableDatabase = dbHelp.getReadableDatabase();
                  readableDatabase.delete(InvocationTable.table, InvocationTable.USER + "=? or " + InvocationTable.GROUPNAME + " =?", new
                          String[]{name, name});
            }


      }

      /**
       * 更新邀请状态
       *
       * @param invocationStatus
       * @param name
       */
      public void updateInvocationstatus(InvocationStatus invocationStatus, String name) {
            if (name != null && invocationStatus != null) {
                  SQLiteDatabase readableDatabase = dbHelp.getReadableDatabase();
                  ContentValues values = new ContentValues();
                  values.put(InvocationTable.STATUS, invocationStatus.ordinal());
                  readableDatabase.update(InvocationTable.table, values, InvocationTable.USER + "=? or " + InvocationTable.GROUPNAME + " " +
                          "=?", new String[]{name, name});
            }
      }
      public boolean hasUser(String user){
            List<InvocationInfo> invocations = getInvocations();
            for (InvocationInfo in:invocations
                 ) {
                  String user1 = in.getUserInfo().getUser();
                  if(user.equals(user1)){
                        return true;
                  }

            }

            return false;
      }
}
