package com.hades.imlibrary.rongyun;

/**
 * Created by Hades on 2017/4/5.
 */

public interface IMCallBack {
    void TokenError(String info);
    void ConnectError(String error);
    /**
     * 描述 群组、用户信息提供者
     * RongIM sdk在展示UI的时候会调用信息提供者，此处异步缓存昵称和头像
     **/
    void getGroupId(String groupid);
    void getUserId(String userid);
}
