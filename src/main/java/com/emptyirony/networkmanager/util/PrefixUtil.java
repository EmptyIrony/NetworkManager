package com.emptyirony.networkmanager.util;

import me.allen.chen.user.User;

import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/26 9:19
 * 4
 */


public class PrefixUtil {

    @Deprecated
    public static String getPrefix(UUID uuid) {
        return User.getByUUID(uuid).getPrefix();
    }
}
