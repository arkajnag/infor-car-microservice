package io.CarService;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import io.CarService.model.Car;
import io.CarService.repository.CarRepository;
import io.CarService.utils.CommonUtils;

@SpringBootApplication
@EnableEurekaClient
public class CarServiceApplication implements CommandLineRunner{
	
	private Logger logger= LoggerFactory.getLogger(CarServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CarServiceApplication.class, args);
	}
	
	@Autowired
	private CarRepository carRepository;

	@Override
	public void run(String... args) throws Exception {
		ExecutorService applicationStartNewCar= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		try{
			IntStream.rangeClosed(0, 30).forEach(i->CompletableFuture.supplyAsync(this::createNewCar,applicationStartNewCar).join());
		}catch(Exception e){
			logger.error("Application Start has failed. Exception Details:"+e.getMessage());
		}
	}
	
	private Car createNewCar(){
		logger.info("Method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" has started in Thread:"+Thread.currentThread().getName());
		try {
			Car requestCarObject = new Car();
			Integer carPlateNumber = CommonUtils.getRandomData.get();
			requestCarObject.setCarPlateNumber(carPlateNumber);
			requestCarObject.setCarModelName("Car-Model-" + CommonUtils.getRandomData.get());
			LocalDateTime carAvailabilityStartDate = CommonUtils.getRandomLocalDateTime.apply(LocalDateTime.now());
			LocalDateTime carAvailabilityEndDate = carAvailabilityStartDate.plusDays(60);
			requestCarObject.setCarAvailableStartDate(carAvailabilityStartDate);
			requestCarObject.setCarAvailableEndDate(carAvailabilityEndDate);
			Double randomDoubleRentalPrice = CommonUtils.getRandomDoubleWithinRange.apply(10.0, 20.0);
			requestCarObject.setCarRentalPricePerHour(randomDoubleRentalPrice);
			return carRepository.save(requestCarObject);
		}catch (Exception e){
			logger.warn("Method failed to create new Car Information during Application Start. Exception Details:"+e.getMessage());
			//createNewCar();
		}finally {
			logger.info("Method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" has ended");
		}
		return null;
	}

}
