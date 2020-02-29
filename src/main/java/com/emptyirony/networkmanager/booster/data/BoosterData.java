package com.emptyirony.networkmanager.booster.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/27 21:30
 * 4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoosterData {
    private String player;
    private UUID uuid;


}
