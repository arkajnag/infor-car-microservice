package io.CarService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.CarService.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
	
	 //Native Query in JPA. Not using JPQL here.
    @Query(value="Select * from tbl_Car c " +
            "where c.car_available_start_date>=:availableStartDate and c.car_available_end_date<=:availableEndDate and c.car_rental_price_per_hour>=:maximumRentalPrice"
            ,nativeQuery = true)
    List<Car> findByAvailableDatesAndRentalPrice(@Param("availableStartDate")LocalDateTime availableStartDate,
                                                 @Param("availableEndDate")LocalDateTime availableEndDate,
                                                 @Param("maximumRentalPrice")Double maximumRentalPrice);

}
