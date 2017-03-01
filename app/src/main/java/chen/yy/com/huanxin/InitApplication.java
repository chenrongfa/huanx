package chen.yy.com.huanxin;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import chen.yy.com.huanxin.controller.ListenerFriendStatu;
import chen.yy.com.huanxin.model.ThreadPool;
import chen.yy.com.huanxin.model.db.DbHelp;
import chen.yy.com.huanxin.model.db.DbManager;
import chen.yy.com.huanxin.ui.CallReceiver;
import chen.yy.com.huanxin.utils.SpUtils;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/17
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class InitApplication extends Application {
      private CallReceiver callReceiver;

      @Override
      public void onCreate() {
            super.onCreate();
            EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
            options.setAcceptInvitationAlways(false);
            options.setAutoAcceptGroupInvitation(false);
            options.setAutoLogin(true);//自动登录
            EaseUI.getInstance().init(this,options);

//初始化
//            EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
            ThreadPool.getInstance().intit(this);
            //初始化用户数据库
//            UserInfoImp.getUserInfoImp().init(getApplicationContext());
// 初始 sp

            SpUtils.getSpUtils().init(this,"huanxin");
            DbManager.init(new DbHelp(this,"name"));
            //更新好友数据库
            ThreadPool.getInstance(). getThreadPool().execute(new Runnable() {
                  @Override
                  public void run() {
                        try {
                              List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                              DbManager.getDbManager().getContactImp().saveContactsLists(usernames,true);
                        } catch (HyphenateException e) {
                              e.printStackTrace();
                        }
                  }
            });



            //初始化监听
            ListenerFriendStatu.getListenerFriendStatu().init(this);

      }

}
