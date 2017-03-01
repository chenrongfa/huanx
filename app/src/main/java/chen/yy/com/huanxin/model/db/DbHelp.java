package chen.yy.com.huanxin.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import chen.yy.com.huanxin.utils.ContactTable;
import chen.yy.com.huanxin.utils.InvocationTable;
import chen.yy.com.huanxin.utils.UserInfoTable;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/23
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class DbHelp extends SQLiteOpenHelper {
      public DbHelp(Context context, String name) {
            super(context, name, null, 1);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
//联系人
            String sql = ContactTable.SQL;
            db.execSQL(sql);
            //邀请表
            db.execSQL(InvocationTable.CREATE_SQL);
            //用户表
            db.execSQL(UserInfoTable.SQL);
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

      }
}
