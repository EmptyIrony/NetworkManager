package com.emptyirony.networkmanager.data.sub;

import lombok.Data;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/25 11:10
 * 4
 */
@Data
public class StaffOption {
    private boolean notify;
    private int staffChat;

    public StaffOption(boolean notify, int staffChat) {
        this.notify = notify;
        this.staffChat = staffChat;
    }

    public StaffOption() {
    }
}
