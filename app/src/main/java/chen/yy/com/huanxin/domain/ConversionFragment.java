package chen.yy.com.huanxin.domain;

import android.content.Intent;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

import chen.yy.com.huanxin.ui.ChatAvtivity;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/18
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class ConversionFragment extends EaseConversationListFragment {

      @Override
      protected void initView() {
            super.initView();
            EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
                  @Override
                  public void onMessageReceived(List<EMMessage> list) {
                        EaseUI.getInstance().getNotifier().onNewMesg(list);
                        refresh();

                  }

                  @Override
                  public void onCmdMessageReceived(List<EMMessage> list) {

                  }

                  @Override
                  public void onMessageRead(List<EMMessage> list) {

                  }

                  @Override
                  public void onMessageDelivered(List<EMMessage> list) {

                  }

                  @Override
                  public void onMessageChanged(EMMessage emMessage, Object o) {

                  }
            });
            setConversationListItemClickListener(new EaseConversationListItemClickListener() {
                  @Override
                  public void onListItemClicked(EMConversation conversation) {
                        String s = conversation.conversationId();


                        Intent intent=new Intent(getActivity(), ChatAvtivity.class);

                        intent.putExtra(EaseConstant.EXTRA_USER_ID,s);
                        if (conversation.getType().ordinal()==EaseConstant.CHATTYPE_GROUP){
                              intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                        }
                        startActivity(intent);
                  }
            });
      }
}
