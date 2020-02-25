package com.emptyirony.networkmanager.data.sub;

import com.emptyirony.networkmanager.data.sub.object.MsgType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/25 15:52
 * 4
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerOption {
    private boolean stream;
    private MsgType canMsg;

}
