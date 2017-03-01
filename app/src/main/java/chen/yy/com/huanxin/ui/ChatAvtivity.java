package chen.yy.com.huanxin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hyphenate.easeui.EaseConstant;

import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.domain.ChatFragment;

public class ChatAvtivity extends AppCompatActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chat_avtivity);
            initChat();
      }

      private static final String TAG = "ChatAvtivity";
      private void initChat() {
            String stringExtra = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
            int intExtra = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
            ChatFragment easeChatFragment=new ChatFragment();
            Bundle bundle=new Bundle();
            if(stringExtra!=null)
            bundle.putString(EaseConstant.EXTRA_USER_ID,stringExtra);
            bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE,intExtra);
            easeChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_chat,easeChatFragment).commit();

      }
}
