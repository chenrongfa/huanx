package chen.yy.com.huanxin;

import android.util.Log;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
      private static final String TAG = "ExampleUnitTest";
      @Test
      public void addition_isCorrect() throws Exception {
            assertEquals(4, 2 + 2);
      }
      @Test
      public void test()throws Exception{

//            boolean b = new UserInfoImp().addAccount(new UserInfo("1", "sd", "sds", "dsd", "sds"));
//            Log.e(TAG, "test: "+b );
            int d=5/0;
            for (int i = 0; i <10 ; i++) {
                  Log.e(TAG, "test: "+i );

            }


      }
}