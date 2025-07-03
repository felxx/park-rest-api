package com.felxx.park_rest_api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ParkingLotUtils {

    private static final double FIRST_15_MINUTES = 5.00;
    private static final double FIRST_60_MINUTES = 9.25;
    private static final double ADDITIONAL_15_MINUTES = 1.75;
    private static final double PERCENTUAL_DISCOUNT = 0.30;
    
    public static String generateReceipt() {
        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0, 19).replace("-", "").replace(":", "").replace("T", "-");
        return receipt;
    }

    public static BigDecimal calculatePrice(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = FIRST_15_MINUTES;
        } else if (minutes <= 60) {
            total = FIRST_60_MINUTES;
        } else {
            long addicionalMinutes = minutes - 60;
            Double totalParts = ((double) addicionalMinutes / 15);
            if (totalParts > totalParts.intValue()) { // 4.66 > 4
                total += FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * (totalParts.intValue() + 1));
            } else { // 4.0
                total += FIRST_60_MINUTES + (ADDITIONAL_15_MINUTES * totalParts.intValue());
            }
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculateDiscount(BigDecimal custo, long numeroDeVezes) {
        BigDecimal desconto = ((numeroDeVezes > 0) && (numeroDeVezes % 10 == 0))
                ? custo.multiply(new BigDecimal(PERCENTUAL_DISCOUNT))
                : new BigDecimal(0);
        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}
