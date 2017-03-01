package chen.yy.com.huanxin.domain;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;

import java.util.ArrayList;
import java.util.List;

import chen.yy.com.huanxin.R;
import chen.yy.com.huanxin.ui.VideoCallActivity;
import chen.yy.com.huanxin.ui.VoiceCallActivity;
import chen.yy.com.huanxin.utils.ShowTipUtil;

/**
 * huanxin
 * Created by chenrongfa on 2017/2/28
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */
public class ChatFragment extends EaseChatFragment {
    @Override
    protected void initView() {
        super.initView();
        setChatFragmentListener(new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
                Log.e(TAG, "onSetMessageAttributes: ");
            }

            @Override
            public void onEnterToChatDetails() {
                Log.e(TAG, "onEnterToChatDetails: ");

            }

            @Override
            public void onAvatarClick(String username) {
                ShowTipUtil.ShowTip(getActivity(), "dianima");
            }

            @Override
            public void onAvatarLongClick(String username) {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                Log.e(TAG, "onMessageBubbleClick: ");
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {
                Log.e(TAG, "onMessageBubbleLongClick: ");

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                Log.e(TAG, "onExtendMenuItemClick: ");
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                Log.e(TAG, "onSetCustomChatRowProvider: ");
                return null;
            }
        });


    }

    @Override
    protected void setUpView() {
        super.setUpView();
        EaseChatExtendMenu extendMenu = inputMenu.getExtendMenu();
        voiceResovle(extendMenu);
        videoResovle(extendMenu);
        extandEmoj();

    }

    private void videoResovle(EaseChatExtendMenu extendMenu) {
        extendMenu.registerMenuItem("视频通话", R.drawable.em_chat_video_call_normal, itemIds[4], new EaseChatExtendMenu
                .EaseChatExtendMenuItemClickListener() {
            @Override
            public void onClick(int itemId, View view) {
                Log.e(TAG, "onClick: diannima");
                String name = getArguments().getString(EaseConstant.EXTRA_USER_ID);
                Log.e(TAG, "onClick: dianima" + name);
                if (name != null) {
                    Intent intent = new Intent(getActivity(), VideoCallActivity.class);
                    intent.putExtra("username", name);
                    intent.putExtra("isComingCall", false);
                    getActivity().startActivity(intent);

                }
            }
        });
    }

    private void voiceResovle(EaseChatExtendMenu extendMenu) {
        extendMenu.registerMenuItem("语音通话", R.drawable.em_chat_voice_call_normal, itemIds[3], new EaseChatExtendMenu
                .EaseChatExtendMenuItemClickListener() {
            @Override
            public void onClick(int itemId, View view) {
                Log.e(TAG, "onClick: diannima");
                String name = getArguments().getString(EaseConstant.EXTRA_USER_ID);
                Log.e(TAG, "onClick: dianima" + name);
                if (name != null) {
                    Intent intent = new Intent(getActivity(), VoiceCallActivity.class);
                    intent.putExtra("username", name);
                    intent.putExtra("isComingCall", false);
                    getActivity().startActivity(intent);

                }
            }
        });
    }

    private void extandEmoj() {
        List<EaseEmojiconGroupEntity> emojicon1 = new ArrayList<>();
        EaseEmojiconGroupEntity em = new EaseEmojiconGroupEntity();
        em.setIcon(R.drawable.custom1);
        List<EaseEmojicon> emoj = new ArrayList<>();
        EaseEmojicon easeEm = new EaseEmojicon();
        easeEm.setIcon(R.drawable.custom2);
//            ImageSpan  image=new ImageSpan(BitmapFactory.decodeResource(getResources(),R.drawable.custom1));
//            SpannableString span=new SpannableString("121");
//            span.setSpan(image,0,2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        easeEm.setEmojiText("1");
        easeEm.setType(EaseEmojicon.Type.NORMAL);
        emoj.add(easeEm);
        EaseEmojicon easeEm1 = new EaseEmojicon();
        easeEm1.setIcon(R.drawable.ee_9);
        easeEm1.setType(EaseEmojicon.Type.NORMAL);
        easeEm1.setEmojiText("2");
        emoj.add(easeEm1);
        EaseEmojicon easeEm2 = new EaseEmojicon();
        easeEm2.setIcon(R.drawable.t);
        easeEm2.setType(EaseEmojicon.Type.NORMAL);
//            ImageSpan image = new ImageSpan(BitmapFactory.decodeResource(getResources(), R.drawable.custom4));
//            SpannableString span = new SpannableString("12");
//            span.setSpan(image, 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        easeEm2.setEmojiText("3");
        emoj.add(easeEm2);
        em.setEmojiconList(emoj);
        em.setType(EaseEmojicon.Type.NORMAL);

        emojicon1.add(em);


        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(em);
    }


}

