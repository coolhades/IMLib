package com.hades.imlibrary.rongyun;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by Hades on 2017/3/1.
 */
@MessageTag(value = "app:custom", flag = MessageTag.ISCOUNTED| MessageTag.ISPERSISTED)
public class CustomMessage extends MessageContent {
    private String content;//消息属性，可随意定义
    private Drawable bitmap;
    private String url;


    public CustomMessage() {
    }

    public static CustomMessage obtain(String content, Drawable bitmap1, String url){

        CustomMessage customMessage = new CustomMessage();
        customMessage.content = content;
        customMessage.bitmap = bitmap1;
        customMessage.url = url;
        Log.d("TAG-CustomMessage", "obtain url="+url);
        return customMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Drawable getBitmap() {
        return bitmap;
    }

    public void setBitmap(Drawable bitmap) {
        this.bitmap = bitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //消息解析成json
    public CustomMessage(byte[] data) {
        Log.d("TAG-CustomMessage", "CustomMessage");
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content"))
                content = jsonObj.optString("content");
//                bitmap = jsonObj.getJSONObject("image");
            if (jsonObj.has("url"))
                url = jsonObj.optString("url");
        } catch (JSONException e) {
            RLog.e("JSONException", e.getMessage());
        }

    }


    public CustomMessage(Parcel source) {
        setContent(ParcelUtils.readFromParcel(source));
        setUrl(ParcelUtils.readFromParcel(source));
    }

    //消息编码成byte 发送消息调用
    @Override
    public byte[] encode() {
        Log.d("TAG-CustomMessage", "encode");
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", content);
//            jsonObj.put("image", bitmap);
            jsonObj.put("url", getUrl());//新字段需要持久化
            Log.d("TAG-CustomMessage", "json Put"+"  url="+getUrl());
        } catch (JSONException e) {
            Log.e("TAG-CustomMessage", e.getMessage());
        }
        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("TAG-CustomMessage", "UnsupportedEncodingException");
        }

        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //图片要转为base64
        ParcelUtils.writeToParcel(parcel, content);
        ParcelUtils.writeToParcel(parcel, url);
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<CustomMessage> CREATOR = new Creator<CustomMessage>() {

        @Override
        public CustomMessage createFromParcel(Parcel source) {
            return new CustomMessage(source);
        }

        @Override
        public CustomMessage[] newArray(int size) {
            return new CustomMessage[size];
        }
    };
}
