package com.emptyirony.networkmanager.data.sub.guild;

import com.emptyirony.networkmanager.data.sub.GuildData;
import lombok.Data;

import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/27 9:42
 * 4
 */
@Data
public class GuildMemData {
    private UUID uuid;
    private String name;
    private String lowerName;
    private GuildData guild;
    private PermissionData permission;
    private long joinedTime;
}
