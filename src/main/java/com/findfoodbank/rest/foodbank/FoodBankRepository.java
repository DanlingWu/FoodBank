package com.findfoodbank.rest.foodbank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FoodBankRepository extends JpaRepository<FoodBank, Long>{

    /**
     * Find the nearest food bank to a given latitude and longitude within a distance
     * @param lat
     * @param lng
     * @param distance
     * @return
     */
    @Query(value ="SELECT *, ( 6371 * acos( cos( radians(?1) ) * cos( radians(  latitude ) ) * cos( radians( longitude ) - radians(?2) ) + sin(     radians(?1) ) * sin( radians( latitude ) ) ) ) AS distance FROM food_bank HAVING distance < ?3 ORDER BY distance LIMIT 0 , 20",nativeQuery = true)
    List<FoodBank> findNearByFoodBanks(BigDecimal lat, BigDecimal lng, int distance);

}
