package dev.lrusian.reclaim.util;

import net.md_5.bungee.api.ChatColor;
import java.util.List;
import java.util.stream.Collectors;

public class CC {

    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(List<String> in) {
        return in.stream().map(CC::translate).collect(Collectors.toList());
    }



}
