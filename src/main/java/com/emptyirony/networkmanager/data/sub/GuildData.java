package com.emptyirony.networkmanager.data.sub;

import com.emptyirony.networkmanager.data.PlayerData;
import com.emptyirony.networkmanager.data.sub.guild.GuildMemData;
import com.emptyirony.networkmanager.data.sub.guild.PermissionData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/27 9:41
 * 4
 */
@NoArgsConstructor
@Data
public class GuildData {
    private UUID uuid;
    private String name;
    private PlayerData leader;
    private long createTime;
    private int coins;
    private long exp;
    private Set<GuildMemData> guildMembers = new HashSet<>();
    private List<PermissionData> permissionGroups = new ArrayList<>();
    private String tag;
    private String color;
}
