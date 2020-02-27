package com.emptyirony.networkmanager.friend.data;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 0:42
 * 4
 */
@Data
public class FriendData {
    @Getter
    private static List<FriendData> cache = new ArrayList<>();

    private String name;
    private String target;
    private long time;

    public FriendData(String name, String target, long time) {
        this.name = name;
        this.target = target;
        this.time = time;
    }

    public static FriendData getDataByTargetName(String name) {
        Optional<FriendData> friendData = cache.stream().filter(data -> data.target.equalsIgnoreCase(name) && data.isUseful()).findAny();
        return friendData.orElse(null);
    }

    public boolean add() {
        if (cache.contains(this)) {
            return false;
        }
        cache.add(this);
        return true;
    }

    public boolean isUseful() {
        return System.currentTimeMillis() - time < 5 * 1000 * 60;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendData that = (FriendData) o;
        return name.equalsIgnoreCase(that.getName()) && target.equalsIgnoreCase(that.getTarget());
    }

}
