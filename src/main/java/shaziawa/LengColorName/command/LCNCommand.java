package shaziawa.LengColorName.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import shaziawa.LengColorName.gui.ColorGUI;

public class LCNCommand implements CommandExecutor {

    public LCNCommand(shaziawa.LengColorName.LengColorName plugin) {
        plugin.getCommand("lcn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c仅玩家可使用");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("lengcolorname.use")) {
            p.sendMessage("§c你没有权限");
            return true;
        }
        new ColorGUI(p);
        return true;
    }
}