package com.ala.ala.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryStatsResponse implements Serializable {

    private long totale;
    private long traiter;
    private long nonTraiter;

    private long lu;
    private long nonLu;
}
