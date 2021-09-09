package me._4o4.gyklHelper.models;


public class ServerData {
    private CachedEntity cache;

    public ServerData(CachedEntity cache) {
        this.cache = cache;
    }

    public CachedEntity getCache() {
        return cache;
    }

    public void setCache(CachedEntity cache) {
        this.cache = cache;
    }
}
