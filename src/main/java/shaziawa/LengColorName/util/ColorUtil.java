package shaziawa.LengColorName.util;

import net.md_5.bungee.api.ChatColor;

public class ColorUtil {

    public static String gradient(String text, String... hex) {
        StringBuilder builder = new StringBuilder();
        int length = text.length();
        for (int i = 0; i < length; i++) {
            float ratio = (float) i / (length - 1);
            int index = (int) (ratio * (hex.length - 1));
            String c1 = hex[index];
            String c2 = hex[Math.min(index + 1, hex.length - 1)];
            java.awt.Color color = interpolate(hexToColor(c1), hexToColor(c2), ratio * (hex.length - 1) - index);
            builder.append(ChatColor.of(color)).append(text.charAt(i));
        }
        return builder.toString();
    }

    private static java.awt.Color hexToColor(String hex) {
        return java.awt.Color.decode(hex.startsWith("#") ? hex : "#" + hex);
    }

    private static java.awt.Color interpolate(java.awt.Color c1, java.awt.Color c2, float ratio) {
        int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * ratio);
        int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio);
        int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * ratio);
        return new java.awt.Color(r, g, b);
    }
}