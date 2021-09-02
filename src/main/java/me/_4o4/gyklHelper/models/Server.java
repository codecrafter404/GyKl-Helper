package me._4o4.gyklHelper.models;

import java.util.Arrays;

public class Server {
    String server_id = "";
    String server_name = "";
    ServerConfig config;
    ServerData data;

    public Server(String server_id, String server_name, ServerConfig config, ServerData data) {
        this.server_id = server_id;
        this.server_name = server_name;
        this.config = config;
        this.data = data;
    }

    public String getServer_id() {
        return server_id;
    }

    public String getServer_name() {
        return server_name;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public ServerData getData() {
        return data;
    }
    public static Server getDefault(String serverID, String serverName){
        return new Server(
                serverID,
                serverName,
                new ServerConfig(
                        Arrays.asList(),
                        Arrays.asList(),
                        "",
                        "",
                        Arrays.asList(),
                        "8b",
                        "english",
                        false,
                        "!"
                ),
                new ServerData(
                        Arrays.asList()
                )
        );
    }
}
