package me._4o4.gyklHelper.models;

import java.util.ArrayList;
import java.util.List;

public class ServerData {
    List<MutedUser> muted_users = new ArrayList<>();

    public ServerData(List<MutedUser> muted_users) {
        this.muted_users = muted_users;
    }

    public List<MutedUser> getMuted_users() {
        return muted_users;
    }
}
