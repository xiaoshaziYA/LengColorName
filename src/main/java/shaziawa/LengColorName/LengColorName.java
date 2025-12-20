package shaziawa.LengColorName;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import shaziawa.LengColorName.command.LCNCommand;
import shaziawa.LengColorName.gui.ColorGUI;
import shaziawa.LengColorName.placeholder.LCNExpansion;
import shaziawa.LengColorName.util.ColorUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class LengColorName extends JavaPlugin {

    private static LengColorName instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // 加载成功彩虹提示
        String rainbow = ColorUtil.gradient("LengColorName 加载成功！ author:shazi_awa",
                "#FF0000", "#FF7F00", "#FFFF00", "#00FF00", "#0000FF", "#4B0082", "#9400D3");
        Bukkit.getConsoleSender().sendMessage(rainbow);

        // 注册命令
        new LCNCommand(this);

        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new LCNExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        ColorGUI.closeAll();
        
        // 注销所有事件
        HandlerList.unregisterAll(this);

        Bukkit.getConsoleSender().sendMessage("§cLengColorName 已卸载");
    }

    public static LengColorName getInstance() {
        return instance;
    }
}