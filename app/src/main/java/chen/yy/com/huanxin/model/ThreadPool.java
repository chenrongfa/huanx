package chen.yy.com.huanxin.model;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/17
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class ThreadPool {
      private static ThreadPool threadPool = new ThreadPool();
      private static ExecutorService executor= Executors.newCachedThreadPool();
      private Context mContext;
      private static boolean add;
      private ThreadPool(){}
      public static ThreadPool getInstance(){
            return threadPool;
      }

  public void intit(Context context){
        mContext=context;

  }
      public static ExecutorService getThreadPool(){

            return executor;
      }
//      public static boolean addAccount(final String user, final String pwd){
//
//            executor.execute(new Runnable() {
//                  @Override
//                  public void run() {
//                        try {
//                              EMClient.getInstance().createAccount(user,pwd);
//                              add=true;
//                        } catch (HyphenateException e) {
//                              e.printStackTrace();
//                              add=false;
//                        }
//                  }
//            });
//            //延迟一秒, 让他获取到线程里面的值
//            SystemClock.sleep(200);
//            return add;
//      }
}
