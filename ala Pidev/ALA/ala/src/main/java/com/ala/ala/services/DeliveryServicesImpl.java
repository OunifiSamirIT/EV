package com.ala.ala.services;

import com.ala.ala.Entities.*;
import com.ala.ala.Repositories.IDeliveryRepository;
import com.ala.ala.Repositories.IProduitRepository;
import com.ala.ala.Repositories.UserRepository;
import com.ala.ala.dto.DeliveryRequest;
import com.ala.ala.dto.DeliveryStatsResponse;
import com.ala.ala.dto.PageResponse;

import com.ala.ala.exception.InputValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Service
public class DeliveryServicesImpl implements IDeliveryServices {



    @Autowired
    IDeliveryRepository deliveryRepository;

    @Autowired
    IProduitRepository produitRepository;


    @Autowired
    NotificationService notificationService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BadWordService badWordService;



    @Override
    public void deleteDeliveryById(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        deliveryRepository.delete(delivery);
    }

    //add dilvery
    @Override
    public Delivery ajouterDelivery(DeliveryRequest deliveryRequest) {

        List<String> errorList = validateReclamation(deliveryRequest);
        if (!errorList.isEmpty()) {
            throw new InputValidationException(errorList);
        }

        Produit produit = produitRepository.findById(deliveryRequest.getProduit()).orElseThrow(() -> new RuntimeException("Product not found"));

        User acheteur = userRepository.findById(deliveryRequest.getAcheteur()).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user has already made 3 complaints for this product
        if (deliveryRepository.countByAcheteurAndProduit(acheteur, produit) >= 3) {
            // Apply 20% discount on the product's price
            produit.setPrix((float) (produit.getPrix() * 0.8));
            produitRepository.save(produit);
        }

        Delivery delivery = new Delivery();
        delivery.setLivreur(userRepository.findById(deliveryRequest.getLivreur()).get());
        delivery.setAcheteur(acheteur);
        delivery.setProduit(produit);
        delivery.setDescription(deliveryRequest.getText());
        delivery.setDate(new Date());

        return deliveryRepository.save(delivery);
    }

    // pour les mots interdits
    private List<String> validateReclamation(DeliveryRequest deliveryRequest) {
        List<String> errors = new ArrayList<>();

        if (badWordService.isWordForbidden(deliveryRequest.getText())) {
            errors.add("La description contient un mot interdit.");
        }

        Optional<User> optionalUser = userRepository.findById(deliveryRequest.getLivreur());
        if (!optionalUser.isPresent()) {
            errors.add("id Livreur invalide");
        } else {
            User user = optionalUser.get();
            if (!Role.livreur.equals(user.getRole())) {
                errors.add("Role invalid (livreur)");
            }
        }

        Optional<Produit> optionalProduit = produitRepository.findById(deliveryRequest.getProduit());
        if (!optionalProduit.isPresent()) {
            errors.add("id Produit invalide");
        }

        Optional<User> opionalCustomer = userRepository.findById(deliveryRequest.getAcheteur());
        if (!opionalCustomer.isPresent()) {
            errors.add("id Customer invalide");
        } else {
            User user = opionalCustomer.get();
            if (!Role.acheteur.equals(user.getRole())) {
                errors.add("Role invalid (acheteur)");
            }
        }
        return errors;
    }

    @Override
    public Delivery AddRecAndAssignToCustomer(Delivery r, Integer idCus) {
        return null;
    }

//  ffectaion de delivery pour l'achteur
    @Override
    public Delivery AddRecAndAssignToCustomer(Delivery r, Long idCus) {
        User customer = userRepository.findById(idCus).get();
        r.setAcheteur(customer);

        return deliveryRepository.save(r);
    }

    @Override
    public Delivery retriveRec(Long idRec) {

        return deliveryRepository.findById(idRec).orElse(null);
    }

    @Override
    public List<Delivery> findByetat(Etat etat) {
        return deliveryRepository.findByEtat(etat);
    }

    @Override
    public List<Delivery> getbydate(Date date) {
        return deliveryRepository.getbydate(date);
    }

    @Override
    public void dd(Long idRec) {
        Delivery rt = deliveryRepository.findById(idRec).orElse(null);
        rt.setEtat(Etat.TRAITER);
        deliveryRepository.save(rt);
    }

    @Override
    public long getnbRecByuser(Integer idCus) {
        return deliveryRepository.countByCustomerId(idCus);
    }



    // fixation de message et mail a partir admin    liveur =message et achteur = mail
    // aussi traitait delivery de non traiter atraiter
    @Override
    public void fixReclamation(long idRec, String reply, String replays ,long idMod) throws InterruptedException {

        Delivery delivery = retriveRec(idRec);

        if (delivery == null) {
            throw new IllegalArgumentException("Invalid id");

        }

        Optional<User> optionalUser = userRepository.findById(idMod);

        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Invalid id");

        }

        User moderateur = optionalUser.get();

        if (!Role.moderateur.equals(moderateur.getRole())) {
            throw new InputValidationException(new ArrayList<>(Collections.singleton("Invalid User Role")));
        }

        User customer = delivery.getAcheteur();
        User livreur = delivery.getLivreur();

        String emailCustomer = customer.getEmailUser();
        String numLivreur = "+216"+ livreur.getNumTel();
        delivery.setEtat(Etat.TRAITER);
        delivery.setReponse(reply);

        delivery.setModerateur(moderateur);

        deliveryRepository.save(delivery);

        notificationService.sendMailNotification(emailCustomer,"Update Delivery", reply);
        notificationService.sendMessageNotification(numLivreur,"+15856321901", replays);
    }

    @Override
    public Delivery getById(long id, boolean read) {

        Optional<Delivery> optionalReclamation = deliveryRepository.findById(id);

        if (optionalReclamation.isPresent()) {
            Delivery delivery = optionalReclamation.get();
            if (read) {
                delivery.setReadr(true);
                deliveryRepository.save(delivery);
            }
            return delivery;
        }
        throw new EntityNotFoundException("Delivery NOT FOUND");
    }

    @Override
    public PageResponse<Delivery> findAll(int page, int size, String sortBy, String sort, boolean all) {
        Pageable pagingSort;
        if (sort.equalsIgnoreCase("desc")) {
            pagingSort = PageRequest.of(page, size, Sort.by(sortBy).descending());
        } else {
            pagingSort = PageRequest.of(page, size, Sort.by(sortBy));
        }
        Page<Delivery> reclamationPage;
        if (all) {

            reclamationPage = deliveryRepository.findAll(pagingSort);
        } else {

            reclamationPage = deliveryRepository.findAllByReadr(false ,pagingSort);
        }
        return new PageResponse<>(reclamationPage.getContent(), reclamationPage.getNumber(),
                reclamationPage.getTotalElements(),
                reclamationPage.getTotalPages());
    }

    @Override
    public DeliveryStatsResponse getStats() {
        DeliveryStatsResponse resp = new DeliveryStatsResponse();
        resp.setTotale(deliveryRepository.count());
        resp.setTraiter(deliveryRepository.countByEtat(Etat.TRAITER));
        resp.setNonTraiter(deliveryRepository.countByEtat(Etat.NONTRAITER));
        resp.setLu(deliveryRepository.countByReadr(true));
        resp.setNonLu(deliveryRepository.countByReadr(false));
        return resp;
    }




    @Override
    public byte[] generateInvoice(List<Sale> sales) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        // Add header
        Paragraph header = new Paragraph("Invoice");
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        // Add table of sales
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new int[]{1, 2, 2, 2, 2});
        table.addCell(new PdfPCell(new Phrase("Sale Id")));
        table.addCell(new PdfPCell(new Phrase("Sale Date")));
        table.addCell(new PdfPCell(new Phrase("Customer Name")));
        table.addCell(new PdfPCell(new Phrase("Product Name")));
        table.addCell(new PdfPCell(new Phrase("Total")));
        double total = 0;
        for (Sale sale : sales) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(sale.getSaleId()))));
            table.addCell(new PdfPCell(new Phrase(sale.getSaleDate().toString())));
            table.addCell(new PdfPCell(new Phrase(sale.getCustomerName())));
            table.addCell(new PdfPCell(new Phrase(sale.getProductName())));
            double saleTotal = sale.getUnitPrice() * sale.getQuantity();
            total += saleTotal;
            table.addCell(new PdfPCell(new Phrase(String.valueOf(saleTotal))));
        }
        document.add(table);
        // Add total
        Paragraph totalParagraph = new Paragraph("Total: " + total);
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);
        document.close();
        return outputStream.toByteArray();
    }











}









