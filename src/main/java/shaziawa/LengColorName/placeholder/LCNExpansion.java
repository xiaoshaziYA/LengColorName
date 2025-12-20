package shaziawa.LengColorName.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.entity.Player;
import shaziawa.LengColorName.gui.ColorGUI;

public class LCNExpansion extends PlaceholderHook {

    private final shaziawa.LengColorName.LengColorName plugin;

    public LCNExpansion(shaziawa.LengColorName.LengColorName plugin) {
        this.plugin = plugin;
    }

    public void register() {
        PlaceholderAPI.registerPlaceholderHook("lcn", this);
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if ("name".equals(identifier) && player != null) {
            return ColorGUI.getNameColored(player.getUniqueId());
        }
        return null;
    }
}