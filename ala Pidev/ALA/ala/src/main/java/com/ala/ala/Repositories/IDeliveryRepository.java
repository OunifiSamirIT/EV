package com.ala.ala.Repositories;

import com.ala.ala.Entities.Etat;
import com.ala.ala.Entities.Produit;
import com.ala.ala.Entities.Delivery;
import com.ala.ala.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IDeliveryRepository extends PagingAndSortingRepository<Delivery,Long>, JpaSpecificationExecutor<Delivery> {

    long countByAcheteurAndProduit(User acheteur, Produit produit);

    List<Delivery> findByEtat(@Param("etat") Etat etat);

    long countByEtat(Etat etat);

  @Query("SELECT w FROM  Delivery w  WHERE w.date = :date ")
     List<Delivery> getbydate (@Param("date") Date date);


    @Query("select count(r) from Delivery r where r.acheteur.idUser=:customerId")
    long countByCustomerId(long customerId);

    Page<Delivery> findAllByReadr(boolean read, Pageable pagingSort);

    long countByReadr(boolean read);

   /* @Query("SELECT u FROM User u WHERE (:search IS NULL OR u.nomUser LIKE %:search% "
            + " OR u.prenomUser LIKE %:search% OR u.username LIKE %:search% OR u.cinUser LIKE %:search% OR u.phoneNumber LIKE %:search% )")
    List<User> rechercheDynamique(@Param("search") String search);
*/



}
