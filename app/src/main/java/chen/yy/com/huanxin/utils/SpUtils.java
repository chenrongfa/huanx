package chen.yy.com.huanxin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/23
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class SpUtils {
      private static  Context context;
      private static  SharedPreferences sp;
      private static SpUtils spUtils=new SpUtils();
      private SpUtils(){


      }
      public void init(Context context,String fileName){
            this.context=context;
            sp= context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
      }
      public static  SpUtils getSpUtils(){
            return  spUtils;
      }
      public void save(String key ,Object  values){
            SharedPreferences.Editor edit = sp.edit();
            if (values instanceof  String){
                  edit.putString(key,values.toString());


            }else if(values instanceof  Boolean ){
                  edit.putBoolean(key,(Boolean)values);
            }
            else if(values instanceof  Integer ){
                  edit.putInt(key,(Integer)values);
            }

    edit.commit();

      }
      public  String getString(String key , String defValues){

            String string = sp.getString(key, defValues);
            return  string;

      }  public  boolean getBoolean(String key , boolean defValues){

            boolean string = sp.getBoolean(key, defValues);
            return  string;

      }
}
