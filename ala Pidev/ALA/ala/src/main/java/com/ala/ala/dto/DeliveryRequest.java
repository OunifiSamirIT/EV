package com.ala.ala.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest implements Serializable {


    private String text;


    private long produit;

    private long livreur;

    private long acheteur;

    public long getAcheteur() {
        return acheteur;
    }


}
