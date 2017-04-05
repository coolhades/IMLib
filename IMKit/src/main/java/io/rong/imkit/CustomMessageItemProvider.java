package io.rong.imkit;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by Hades on 2017/3/1.
 */
@ProviderTag(messageContent = CustomMessage.class)
public class CustomMessageItemProvider extends IContainerItemProvider.MessageProvider<CustomMessage>{

    class ViewHolder{
        TextView message;
        ImageView imageView;
        TextView textView;
        LinearLayout layout;
    }

    @Override
    public void bindView(View view, int i,
                         CustomMessage customMessage,
                         UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (Message.MessageDirection.SEND == uiMessage.getMessageDirection()){//消息方向，自己发送的
            holder.layout.setBackgroundResource(R.drawable.rc_ic_bubble_right);
        }else {
            holder.layout.setBackgroundResource(R.drawable.rc_ic_bubble_left);
        }
//        Log.d("TAG-CustomMessage", "image"+customMessage.getBitmap()+"url="+customMessage.getUrl());
        holder.message.setText(customMessage.getContent());
//        holder.imageView.setBackground(customMessage.getBitmap());
        holder.textView.setText(customMessage.getUrl());
//        AndroidEmoji.ensure((Spannable) holder.message.getText());//显示消息中的 Emoji 表情。
    }

    @Override
    public Spannable getContentSummary(CustomMessage customMessage) {
        return new SpannableString("自定义消息");
    }

    @Override
    public void onItemClick(View view, int i, CustomMessage customMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, CustomMessage customMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.im_custom_message_item, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.message);
        holder.imageView = (ImageView) view.findViewById(R.id.image);
        holder.textView = (TextView) view.findViewById(R.id.url);
        holder.layout = (LinearLayout) view.findViewById(R.id.line);
        view.setTag(holder);
        return view;
    }
}
