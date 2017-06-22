package ua.vzaperti.matrix.net;


public enum MatrixEvent {
    NONE,
    RESET,
    WAKEUP_MESSAGE, // Neo
    NEO_LIGHT_ON, // Neo
    NEO_LIGHT_OFF, // Neo
    ENABLE_HACK,   // Neo
    DISABLE_HACK,  // Neo
    WIRES_CONNECTED, // Neo
    WIRES_DISCONNECTED, // Neo
    OPEN_CHAIR_ROOM_DOOR, // Neo
    CLOSE_CHAIR_ROOM_DOOR, // Neo
    PILL_DETECTED, // Morpheus
    PILL_REMOVED, // Morpheus
    OPEN_SHIP_DOOR, // Morpheus
    CLOSE_SHIP_DOOR, // Morpheus

    SHIP_DOOR_OPENED,   // status message
    SHIP_DOOR_CLOSED,   // status message

    NEO_CONNECTED, // Ship
    NEO_DISCONNECTED, // Ship
    DISK1_CONNECTED, // Ship
    DISK1_DISCONNECTED, // Ship
    DISK2_CONNECTED, // Ship
    DISK2_DISCONNECTED, // Ship
    DISK3_CONNECTED, // Ship
    DISK3_DISCONNECTED, // Ship
    SENTINAIL_ATTACK, // Ship
    EMP_ENABLED, // Ship
    EMP_DISABLED, // Ship
    EMP_FIRED, // Ship
    NUMBER_DIALED,// Hotel
    OPEN_HOTEL_DOOR, // Hotel
    CLOSE_HOTEL_DOOR, // Hotel
    SHOW_SMITH, // Ship
    OPEN_NEO_TABLE, // Neo
    CONNECTED_SMITH, // Neo
    DISCONNECTED_SMITH, // Neo
    SHOW_DISCS_HOLO, // Ship
    SHOW_SMITH_HOLO, // Ship
    OPEN_FINAL_DOOR, // Neo
    CLOSE_FINAL_DOOR, // Neo

    CORRECT_HOTEL_NUMBER, //Neo
    HOTEL_HELP_PRESSED, //Neo
    //	REACTOR1_CONNECTED, //Neo
    REACTOR_DISCONNECTED, //Neo
    //	REACTOR2_CONNECTED, //Neo
    //	REACTOR2_DISCONNECTED, //Neo
    CLOSE_PIRAMID, //Neo
    OPEN_PIRAMID, //Neo
    OUTER_SWITCH_ON, //Neo
    OUTER_SWITCH_OFF, //Neo

    OPEN_EVERYTHING, // All
    OPEN_METAL_CHAIR, // Ship
    INTERCOM_PRESSED, // Ship
    EMP_FAILED,
    HOTEL_PHONE_FLASH,
    HOTEL_PHONE_UNFLASH,
    STOP_MUSIC,

    SET_LANGUAGE_RU,
    SET_LANGUAGE_EN,
    MORPHEUS_PHONE_ENABLE,
    MORPHEUS_PHONE_DISABLE;
}
