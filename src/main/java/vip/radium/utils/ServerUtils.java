package vip.radium.utils;

import java.util.HashMap;
import java.util.Map;

public final class ServerUtils {

    private static final Map<String, Long> serverIpPingCache = new HashMap<>();

    private static final String HYPIXEL = "hypixel.net";

    private ServerUtils() {
    }

    public static void update(String ip, long ping) {
        serverIpPingCache.put(ip, ping);
    }

    public static long getPingToServer(String ip) {
        return serverIpPingCache.getOrDefault(ip, 200L);
    }

    public static boolean isOnServer(String ip) {
        return !mc.getMinecraft().isSingleplayer() && getCurrentServerIP().endsWith(ip);
    }

    public static String getCurrentServerIP() {
        return mc.getMinecraft().isSingleplayer() ?
                "Singleplayer" :
                mc.getMinecraft().getCurrentServerData().serverIP;
    }

    public static boolean isOnHypixel() {
        return isOnServer(HYPIXEL);
    }

    public static long getPingToCurrentServer() {
        return mc.getMinecraft().isSingleplayer() ?
                0 :
                getPingToServer(getCurrentServerIP());
    }
}
