package io.rong.imkit;

import java.util.List;

import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;

/**
 * Created by Hades on 2017/2/23.
 */

public class RongIMPulgin extends DefaultExtensionModule {
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        for (int i = 0; i<pluginModules.size(); i++){
            if (pluginModules.get(i) instanceof FilePlugin){
                pluginModules.remove(i);
            }

        }

//        pluginModules.add(new RongIMCustomPulgin());
        return pluginModules;
    }




}
