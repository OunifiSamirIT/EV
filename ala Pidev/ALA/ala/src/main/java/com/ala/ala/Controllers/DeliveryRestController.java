package com.ala.ala.Controllers;


import com.ala.ala.Entities.Delivery;
import com.ala.ala.Entities.Etat;
import com.ala.ala.Entities.Sale;
import com.ala.ala.Repositories.IProduitRepository;
import com.ala.ala.Repositories.IDeliveryRepository;
import com.ala.ala.dto.DeliveryRequest;
import com.ala.ala.dto.DeliveryStatsResponse;
import com.ala.ala.services.IDeliveryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/Rec")
public class DeliveryRestController {

    @Autowired
    IDeliveryServices deliveryServices;
    @Autowired
    IDeliveryRepository deliveryRepository;


    @Autowired
    IProduitRepository produitRepository;



    @Autowired
    IDeliveryServices delvService;








@DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDeliveryById(@PathVariable("id") Long deliveryId) {
        deliveryServices.deleteDeliveryById(deliveryId);
        return ResponseEntity.ok("Delivery deleted successfully");
    }


    @PostMapping
    public ResponseEntity<?> addRec(@RequestBody DeliveryRequest reclamation) {

        Delivery saved = deliveryServices.ajouterDelivery(reclamation);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getIdRec()).toUri();
        return ResponseEntity.created(location).build();
    }


@GetMapping("/{id}")
    public ResponseEntity<Delivery> getReclamation(@PathVariable long id, @RequestParam(defaultValue = "true") boolean read) {
        return ResponseEntity.ok(deliveryServices.getById(id, read));
    }

    @GetMapping("/find")
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "idRec") String sortBy,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "false") boolean all) {

        return ResponseEntity.ok(deliveryServices.findAll(page, size, sortBy, sort, all));

    }

    /*
    @PostMapping("/affe/{idCus}")

    @ResponseBody
    public Delivery addMedecinToClinique(@RequestBody Delivery reclamation, @PathVariable("idCus") Integer idCus) {
        return reclamationServices.AddRecAndAssignToCustomer(reclamation, idCus);
    }

    @PostMapping("/affecterRec/{idCus}/{idProduit}")
    @ResponseBody
    public Delivery addMedecinToClinique(@RequestBody Delivery reclamation, @PathVariable("idCus") Integer idCus, @PathVariable("idProduit") Long idProduit) {
        return reclamationServices.ajouterf(reclamation, idCus.longValue(), idProduit);
    }

*/

    @PostMapping("/{idRec}/send")
    public void send(@RequestBody String reply, String replays, @PathVariable("idRec") long idRec, @RequestParam("idMod") long idMod) throws InterruptedException {
        deliveryServices.fixReclamation(idRec, reply, replays, idMod);
    }




    @PostMapping("/invoices")
    public ResponseEntity<byte[]> generateInvoice(@RequestBody List<Sale> sales) {
        try {
            byte[] invoiceBytes = deliveryServices.generateInvoice(sales);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice.pdf");
            headers.setContentLength(invoiceBytes.length);
            return new ResponseEntity<>(invoiceBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }












    @ResponseBody
    public long nbrrdvby(@PathVariable("idCus") Integer idCus) {

        return deliveryServices.getnbRecByuser(idCus);
    }


    @GetMapping("reclam/{etat}")
    public List<Delivery> getall(@PathVariable("etat") Etat etat) {

        return deliveryServices.findByetat(etat);
    }

    @GetMapping("/stats")
    public ResponseEntity<DeliveryStatsResponse> getStats() {

        return ResponseEntity.ok(deliveryServices.getStats());
    }

   /* @Autowired
    private BadWordService badWordService;

    @PostMapping("/Addaaa")
    public void  createComment(@RequestBody Delivery description) {
        String filteredContent = badWordService.filter(description.getDescription());
        description.setDescription(filteredContent);

        // Save the filtered comment to the database and return it

    }*/


    /*

        @GetMapping("rec/{startDate}")
        @ResponseBody
        public List<Delivery> getBydate(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
            return reclamationServices.getbydate(date);
        }




        @PostMapping("bad")
        public ResponseEntity<?> create(@RequestBody Delivery reclamation) {
            try {
                reclamationServices.create(reclamation);
                return ResponseEntity.ok().build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        /*@GetMapping("/rechercheDynamique")
        List<User> searchUsers(@RequestParam(required = false) String recherche) {
            return userServices.rechercheDynamique(recherche);
        }


        @GetMapping("/{id}/reclamations")
        public ResponseEntity<Integer> getNombreReclamations(@PathVariable Long id) {
            int nombreReclamations = reclamationServices.getNombreReclamations(id);
            return ResponseEntity.ok(nombreReclamations);
        }*/
    /*@GetMapping("/search")
    public Iterable<Delivery> search(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long idRec,

            @RequestParam(required = false) Date date,
            @RequestParam(required = false) Etat etat)
    {
        return reclamationServices.search(description,idRec,date,etat);
    }

    @PostMapping("/{id}/comments")
    public  String addCommentToClaim(@PathVariable("id") Long claimId, @ModelAttribute Comment comment) {
        Delivery claim = deliveryRepository.findById(claimId).orElseThrow(EntityNotFoundException::new);
        comment.setTimestamp(LocalDateTime.now());
        comment.setDelivery(claim);
        claim.getComments().add(comment);
         deliveryRepository.save(claim);
         return null ;


    }*/



}