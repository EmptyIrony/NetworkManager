package com.emptyirony.networkmanager.friend.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 0:42
 * 4
 */
@AllArgsConstructor
@Data
public class FriendData {
    private String name;
    private String target;
    private long time;

    public boolean isUseful() {
        return System.currentTimeMillis() - time >= 5 * 1000 * 60;
    }
}
