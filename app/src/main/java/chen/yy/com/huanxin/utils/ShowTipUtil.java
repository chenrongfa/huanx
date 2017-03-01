package chen.yy.com.huanxin.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/20
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class ShowTipUtil {
      public static void ShowTip(Activity context,String message){
            if (message!=null)
            Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
            else Toast.makeText(context, "无信息", Toast.LENGTH_SHORT).show();
      }
}
