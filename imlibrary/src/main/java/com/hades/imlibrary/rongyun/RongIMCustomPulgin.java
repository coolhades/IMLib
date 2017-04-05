package com.hades.imlibrary.rongyun;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.hades.imlibrary.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Hades on 2017/3/1.
 */

public class RongIMCustomPulgin implements IPluginModule {
    Conversation.ConversationType conversationType;
    String targetId;
    Context mContext;

    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.de_contacts);
    }

    @Override
    public String obtainTitle(Context context) {
        mContext = context;
        return "自定义消息";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        conversationType = rongExtension.getConversationType();
        targetId = rongExtension.getTargetId();
        //此处处理自定义消息 可弹出view填写消息
        CustomMessage customMessage = CustomMessage.obtain("自定义消息+++！！！",
                mContext.getResources().getDrawable(R.mipmap.de_contacts),
                "哈哈");

        RongIM.getInstance().sendMessage(Conversation.ConversationType.GROUP, targetId,
                customMessage, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        Log.e("CustomMessage", "-----onError--"+errorCode);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.e("CustomMessage", "-----onSuccess--"+integer);
                    }
                });
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
