package com.smartsheet.api.models.enums;

public class SeatType {

    public enum DowngradeSeatType {
        GUEST, VIEWER
    }

    public enum UpgradeSeatType {
        GUEST, MEMBER
    }

    public enum ListUsers {
        MEMBER, PROVISIONAL_MEMBER, GUEST, VIEWER
    }
}
