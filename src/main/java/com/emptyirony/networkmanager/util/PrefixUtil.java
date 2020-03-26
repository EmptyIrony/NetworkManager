package com.emptyirony.networkmanager.util;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/26 9:19
 * 4
 */


public class PrefixUtil {

    public static String getPrefix(UUID uuid) {
        UserManager userManager = LuckPermsProvider.get().getUserManager();
        User user = userManager.getUser(uuid);
        if (user == null) {
            return "";
        }
        CachedMetaData data = user.getCachedData().getMetaData(QueryOptions.builder(QueryMode.NON_CONTEXTUAL).build());
        Optional<Integer> first = data.getPrefixes().keySet().stream().max(Comparator.naturalOrder());

        if (!first.isPresent()) {
            return "";
        }
        return data.getPrefixes().get(first.get());
    }
}
