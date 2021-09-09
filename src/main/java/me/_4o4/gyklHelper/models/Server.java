package me._4o4.gyklHelper.models;

import java.util.Arrays;
import java.util.List;

public class Server {
    String server_id;
    String server_name;
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

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public void setConfig(ServerConfig config) {
        this.config = config;
    }

    public void setData(ServerData data) {
        this.data = data;
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
                        "",
                        "!"
                ),
                new ServerData(
                        null
                )
        );
    }
}
