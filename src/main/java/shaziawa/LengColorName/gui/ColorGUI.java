package shaziawa.LengColorName.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shaziawa.LengColorName.util.ColorUtil;

import java.util.*;

public class ColorGUI implements Listener {

    private static final Map<UUID, String> colorMap = new HashMap<>();
    private static final Set<ColorGUI> activeGuis = new HashSet<>();

    private final Player player;

    /* ---------- 静态工具 ---------- */
    public static String getNameColored(UUID uuid) {
        return colorMap.getOrDefault(uuid, Bukkit.getOfflinePlayer(uuid).getName());
    }

    public static void closeAll() {
        Iterator<ColorGUI> it = activeGuis.iterator();
        while (it.hasNext()) {
            ColorGUI gui = it.next();
            if (gui.player.isOnline()) {
                gui.player.closeInventory();
            }
            HandlerList.unregisterAll(gui);
            it.remove();
        }
    }

    /* ---------- 构造 ---------- */
    public ColorGUI(Player player) {
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, shaziawa.LengColorName.LengColorName.getInstance());
        activeGuis.add(this);
        open();
    }

    /* ---------- 打开 GUI ---------- */
    private void open() {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, "LengColorName");

        // 单一颜色（前两行）
        List<Material> dyes = Arrays.asList(
                Material.BLACK_DYE, Material.RED_DYE, Material.GREEN_DYE, Material.BROWN_DYE,
                Material.BLUE_DYE, Material.PURPLE_DYE, Material.CYAN_DYE, Material.LIGHT_GRAY_DYE,
                Material.GRAY_DYE, Material.PINK_DYE, Material.LIME_DYE, Material.YELLOW_DYE,
                Material.LIGHT_BLUE_DYE, Material.MAGENTA_DYE, Material.ORANGE_DYE, Material.WHITE_DYE
        );
        List<String> colorNames = Arrays.asList(
                "黑色", "红色", "绿色", "棕色", "蓝色", "紫色", "青色", "淡灰色",
                "灰色", "粉色", "酸橙色", "黄色", "淡蓝色", "品红色", "橙色", "白色"
        );
        for (int i = 0; i < dyes.size(); i++) {
            char colorChar = "0123456789abcdef".charAt(i);
            inv.setItem(i, createItem(dyes.get(i), colorNames.get(i), "§" + colorChar + player.getName()));
        }

        // 渐变色（第三行开始）
        String[][] gradients = {
                {"#FF5555", "#FFAA00"},  // 红橙
                {"#55FF55", "#FFFF55"},  // 绿黄
                {"#5555FF", "#FF55FF"},  // 蓝紫
                {"#00AAFF", "#55FF7F"},  // 青绿
                {"#FF00AA", "#AA00FF"},  // 粉紫
                {"#FFFFFF", "#AAAAAA"}   // 白灰
        };
        String[] gradientNames = {
                "§c红橙渐变", "§a绿黄渐变", "§9蓝紫渐变", "§b青绿渐变", "§d粉紫渐变", "§f白灰渐变"
        };
        int slot = 18;
        for (int g = 0; g < gradients.length; g++) {
            String gradient = ColorUtil.gradient(player.getName(), gradients[g]);
            inv.setItem(slot++, createItem(Material.PAPER, gradientNames[g], gradient));
        }

        player.openInventory(inv);
    }

    /* ---------- 工具方法 ---------- */
    private ItemStack createItem(Material mat, String display, String preview) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(display);
        meta.setLore(Arrays.asList("§7当前效果: " + preview));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    /* ---------- 事件 ---------- */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("LengColorName")) return;
        e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String display = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
        String colorized = "";

        // 单一颜色
        List<String> colorNames = Arrays.asList(
                "黑色", "红色", "绿色", "棕色", "蓝色", "紫色", "青色", "淡灰色",
                "灰色", "粉色", "酸橙色", "黄色", "淡蓝色", "品红色", "橙色", "白色"
        );
        int idx = colorNames.indexOf(display);
        if (idx >= 0) {
            char colorChar = "0123456789abcdef".charAt(idx);
            colorized = "§" + colorChar + p.getName();
        } else {
            // 渐变色
            String[][] gradients = {
                    {"#FF5555", "#FFAA00"}, {"#55FF55", "#FFFF55"}, {"#5555FF", "#FF55FF"},
                    {"#00AAFF", "#55FF7F"}, {"#FF00AA", "#AA00FF"}, {"#FFFFFF", "#AAAAAA"}
            };
            String[] gradientNames = {
                    "§c红橙渐变", "§a绿黄渐变", "§9蓝紫渐变", "§b青绿渐变", "§d粉紫渐变", "§f白灰渐变"
            };
            int gIdx = Arrays.asList(gradientNames).indexOf(display);
            if (gIdx >= 0) {
                colorized = ColorUtil.gradient(p.getName(), gradients[gIdx]);
            }
        }

        if (!colorized.isEmpty()) {
            colorMap.put(p.getUniqueId(), colorized);
            p.sendMessage("§a已设置颜色！");
            p.closeInventory();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if ("LengColorName".equals(e.getView().getTitle())) {
            HandlerList.unregisterAll(this);
            activeGuis.remove(this);
        }
    }
}