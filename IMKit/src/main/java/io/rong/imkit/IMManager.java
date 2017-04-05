package io.rong.imkit;

import android.content.Context;
import android.util.Log;

import java.util.List;

import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Hades on 2017/3/2.
 */
public class IMManager {


    public static IMManager getInstance() {
        return Instance.manager;
    }


    /**
     * 创建时间 2017/3/2
     * auther Hades
     * 描述 Application中注册
     **/
    public void IMInit(Context context) {
        //RongIM
        RongIM.init(context);
        initConnectionCallBack();
        //先注册自定义消息
        RongIM.registerMessageType(CustomMessage.class);
        //最后注册聊天扩展功能
        setCustomExtensionModule();
    }

    IMInstanceCallBack instanceCallBack;
    IMBadgeViewCallBack badgeViewCallBack;

    /**
    * 创建时间 2017/4/5
    * auther Hades
    * 描述
     * @param instanceCallBack 单点登录
    **/
    public void setIMInstanceCallBackListener(IMInstanceCallBack instanceCallBack){
        this.instanceCallBack = instanceCallBack;
    }

    public void setBadgeViewCallBack(IMBadgeViewCallBack badgeViewCallBack){
        this.badgeViewCallBack = badgeViewCallBack;
    }


    /**
     * 创建时间 2017/3/2
     * auther Hades
     * 描述 用户登录后调用 在登录回调中调用，避免公有云、私有云冲突
     **/
    public void initConnection(final Context context, String rcToken, final IMCallBack callBack) {
        //链接服务器
        RongIM.connect(rcToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                if (null != RongIM.getInstance()) {
                    RongIM.getInstance().logout();
                    callBack.TokenError("onTokenIncorrect");
                }
            }

            @Override
            public void onSuccess(final String ss) {// s --》Userid

                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    //sdk用到信息时会调用该接口 来获取信息
                    @Override
                    public UserInfo getUserInfo(String s) {
                        callBack.getUserId(s);
                        return null;
                    }
                }, true);
                //群组
                RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                    @Override
                    public Group getGroupInfo(String s) {
                        callBack.getGroupId(s);
                        return null;
                    }
                }, true);

                //群组成员 暂无接口
                RongIM.setGroupUserInfoProvider(new RongIM.GroupUserInfoProvider() {
                                                    @Override
                                                    public GroupUserInfo getGroupUserInfo(String s, String s1) {
                                                        return null;
                                                    }
                                                }, true
                );
                RongIM.getInstance().addUnReadMessageCountChangedObserver(iUnReadMessageObserver, Conversation.ConversationType.GROUP);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("RongIM:", "Connected Failed");
                callBack.ConnectError(errorCode.getMessage());
                //链接失败要求重新登录
            }

        });


    }


    /**
     * 创建时间 2017/3/2
     * auther Hades
     * 描述 未读消息监听 更新badgeview
     **/
    private IUnReadMessageObserver iUnReadMessageObserver = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            //更新数值 目标MainActivity
           if (null != badgeViewCallBack){
               badgeViewCallBack.onMessageCount(i);
           }
        }
    };

    /**
     * 创建时间 2017/3/2
     * auther Hades
     * 描述 群组、用户信息提供者
     * RongIM sdk在展示UI的时候会调用信息提供者，此处异步缓存昵称和头像
     **/
    private void getIMGroupInfo(String groupID) {

    }


    private void getImUserInfo(final String imID) {

    }


    /**
     * 创建时间 2017/3/2
     * auther Hades
     * 描述 监听取消注册
     **/
    public void unRegist() {
        if (null != RongIM.getInstance()) {
            RongIM.getInstance().removeUnReadMessageCountChangedObserver(iUnReadMessageObserver);
        }
    }


    /**
     * 创建时间 2017/3/2
     * auther Hades
     * 描述 融云链接状态监听
     **/
    private void initConnectionCallBack() {
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                switch (connectionStatus) {
                    case CONNECTED://连接成功。
                        if (null != instanceCallBack) {
                            instanceCallBack.connectSuccess();
                        }
                        break;
                    case DISCONNECTED://断开连接。
                        if (null != instanceCallBack) {
                            instanceCallBack.disConnected();
                        }
                        break;
                    case CONNECTING://连接中。

                        break;
                    case NETWORK_UNAVAILABLE://网络不可用。

                        break;
                    case KICKED_OFFLINE_BY_OTHER_CLIENT:
                        //用户账户在其他设备登录，本机会被踢掉线
                        Log.d("TAG-RongIMConnect", "被踢下线");
                        if (null != instanceCallBack) {
                            instanceCallBack.logOut();
                        }
                        break;
                }
            }
        });
    }


    /**
     * 创建时间 2017/3/2
     * auther Hades
     * 描述 覆盖默认的聊天扩展
     **/
    private void setCustomExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (null != moduleList) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }

            if (null != defaultModule) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new RongIMPulgin());
            }
        }

    }

    private IMManager() {
    }

    private static class Instance {
        private static IMManager manager = new IMManager();
    }


    /**
    * 创建时间 2017/4/5
    * auther Hades
    * 描述 注册sdk后调用
    **/
    public interface IMInstanceCallBack {
        void disConnected();
        void connectSuccess();
        void logOut();
    }

    public interface IMBadgeViewCallBack {
        void onMessageCount(int i);
    }
}
