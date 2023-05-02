package com.ala.ala.services;

import com.ala.ala.Entities.Delivery;
import com.ala.ala.Entities.Etat;
import com.ala.ala.Entities.Sale;
import com.ala.ala.dto.DeliveryRequest;
import com.ala.ala.dto.DeliveryStatsResponse;
import com.ala.ala.dto.PageResponse;
import com.lowagie.text.DocumentException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IDeliveryServices {

    void deleteDeliveryById(Long deliveryId);



    Delivery ajouterDelivery(DeliveryRequest deliveryRequest);


    Delivery AddRecAndAssignToCustomer(Delivery r, Integer idCus);


    Delivery AddRecAndAssignToCustomer(Delivery r, Long idCus);

    Delivery retriveRec(Long idRec);


    List<Delivery> findByetat(Etat etat);

    /*int getbydate(Date startDate);*/

    List<Delivery> getbydate(Date date);

    void dd(Long idRec);

    long getnbRecByuser(Integer idCus);


    void fixReclamation(long idRec, String reply,String replays , long idMod) throws InterruptedException;

    Delivery getById(long id, boolean read);

    PageResponse<Delivery> findAll(int page, int size, String sortBy, String sort, boolean all);

    DeliveryStatsResponse getStats();

    byte[] generateInvoice(List<Sale> sales) throws IOException, DocumentException;


}
