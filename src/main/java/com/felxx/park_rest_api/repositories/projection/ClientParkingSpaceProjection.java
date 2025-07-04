package com.felxx.park_rest_api.repositories.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientParkingSpaceProjection {

    String getLicensePlate;
    String getMake;
    String getModel;
    String getColor;
    String getClientCpf;
    String getReceipt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getEntryDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getExitDate;
    String getParkingSpaceCode;
    BigDecimal getPrice;
    BigDecimal getDiscount;
}
