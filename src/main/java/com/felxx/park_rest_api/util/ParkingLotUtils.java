package com.felxx.park_rest_api.util;

import java.time.LocalDateTime;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ParkingLotUtils {
    
    public static String generateReceipt() {
        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0, 19).replace("-", "").replace(":", "").replace("T", "-");
        return receipt;
    }
}
