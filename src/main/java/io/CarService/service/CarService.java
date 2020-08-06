package io.CarService.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.CarService.exceptionhandler.DataNotAllowedException;
import io.CarService.exceptionhandler.DataNotFoundException;
import io.CarService.exceptionhandler.DuplicateDataNotAllowedException;
import io.CarService.exceptionhandler.NullFormatException;
import io.CarService.model.Car;
import io.CarService.model.CarDTO;
import io.CarService.repository.CarRepository;
import io.CarService.utils.CommonUtils;

@Service
public class CarService {
	
    @Autowired
    private CarRepository carRepository;
    
    @Value("${NO_CAR_DATA}")
    private String NO_CAR_DATA;
    
    @Value("${CAR_PAYLOAD_NULL}")
    private String CAR_PAYLOAD_NULL;
    
    @Value("${CAR_PROFILE_NULL}")
    private String CAR_PROFILE_NULL;
    
    @Value("${DUPLICATE_CAR_ID}")
    private String DUPLICATE_CAR_ID;
    
    @Value("${START_AFTER_END_DATE}")
    private String START_AFTER_END_DATE;
    
    @Value("${START_SAME_END_DATE}")
    private String START_SAME_END_DATE;
    
    @Value("${DATES_RATE_NOT_NULL}")
    private String DATES_RATE_NOT_NULL;
    
    @Value("${DATE_FORMAT_NOT_ALLOWED}")
    private String DATE_FORMAT_NOT_ALLOWED;
    
    @Value("${NO_CAR_WITHIN_DATE}")
    private String NO_CAR_WITHIN_DATE;
    
    Logger logger = LoggerFactory.getLogger(CarService.class);
	
	public CompletableFuture<CarDTO> getAllCarsInformation() throws DataNotFoundException {
        logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
        try {
            List<Car> allCars = carRepository.findAll();
            if (allCars.size() == 0) {
                logger.error(NO_CAR_DATA);
                throw new DataNotFoundException(NO_CAR_DATA);
            }
            return CompletableFuture.completedFuture(new CarDTO(allCars));
        } finally {
            logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
        }
    }
	
	public CompletableFuture<Car> addNewCarInformation(Car carObj) throws NullFormatException, DuplicateDataNotAllowedException, DataNotAllowedException {
        logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
        try {
            if (carObj == null) {
                logger.error(CAR_PAYLOAD_NULL);
                throw new NullFormatException(CAR_PAYLOAD_NULL);
            }
            if (carObj.getCarPlateNumber() == null || carObj.getCarModelName() == null || carObj.getCarAvailableStartDate() == null || carObj.getCarAvailableEndDate() == null || carObj.getCarRentalPricePerHour() == null) {
                logger.error(CAR_PROFILE_NULL);
                throw new NullFormatException(CAR_PROFILE_NULL);
            }
            Integer requestedCarPlateNumber = carObj.getCarPlateNumber();
            LocalDateTime requestedAvailabilityStartDate = carObj.getCarAvailableStartDate();
            LocalDateTime requestedAvailabilityEndDate = carObj.getCarAvailableEndDate();
            if (carRepository.findById(requestedCarPlateNumber).isPresent()) {
                logger.error(DUPLICATE_CAR_ID);
                throw new DuplicateDataNotAllowedException(DUPLICATE_CAR_ID);
            }
            if (CommonUtils.isDateAfter.apply(requestedAvailabilityStartDate, requestedAvailabilityEndDate)) {
                logger.error(START_AFTER_END_DATE);
                throw new DataNotAllowedException(START_AFTER_END_DATE);
            }
            if (CommonUtils.isDateEqual.apply(requestedAvailabilityStartDate, requestedAvailabilityEndDate)) {
                logger.error(START_SAME_END_DATE);
                throw new DataNotAllowedException(START_SAME_END_DATE);
            }
            return CompletableFuture.completedFuture(carRepository.save(carObj));
        } finally {
            logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
        }
    }

    public CompletableFuture<CarDTO> searchCarsByAvailabilityAndRentalPrice(String availableStartDate, String availableEndDate, Double requestedRentalPrice) throws NullFormatException, DataNotAllowedException, DataNotFoundException {
        logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
        try {
            LocalDateTime requestedAvailabilityStartDate = null;
            LocalDateTime requestedAvailabilityEndDate = null;
            if (availableStartDate == null || availableEndDate == null || requestedRentalPrice == null) {
                logger.error(DATES_RATE_NOT_NULL);
                throw new NullFormatException(DATES_RATE_NOT_NULL);
            }
            if (CommonUtils.getLocalDateTimeFromDateString.apply(availableStartDate) instanceof Exception) {
                logger.error(DATE_FORMAT_NOT_ALLOWED);
                throw new DataNotAllowedException(DATE_FORMAT_NOT_ALLOWED);
            } else
                requestedAvailabilityStartDate = (LocalDateTime) CommonUtils.getLocalDateTimeFromDateString.apply(availableStartDate);
            if (CommonUtils.getLocalDateTimeFromDateString.apply(availableEndDate) instanceof Exception) {
                logger.error(DATE_FORMAT_NOT_ALLOWED);
                throw new DataNotAllowedException(DATE_FORMAT_NOT_ALLOWED);
            } else
                requestedAvailabilityEndDate = (LocalDateTime) CommonUtils.getLocalDateTimeFromDateString.apply(availableEndDate);
            if (CommonUtils.isDateAfter.apply(requestedAvailabilityStartDate, requestedAvailabilityEndDate)) {
                logger.error(START_AFTER_END_DATE);
                throw new DataNotAllowedException(START_AFTER_END_DATE);
            }
            if (CommonUtils.isDateEqual.apply(requestedAvailabilityStartDate, requestedAvailabilityEndDate)) {
                logger.error(START_SAME_END_DATE);
                throw new DataNotAllowedException(START_SAME_END_DATE);
            }
            List<Car> responseCarObj = carRepository.findByAvailableDatesAndRentalPrice(requestedAvailabilityStartDate, requestedAvailabilityEndDate, requestedRentalPrice);
            if (responseCarObj.size() == 0) {
                logger.error(NO_CAR_WITHIN_DATE);
                throw new DataNotFoundException(NO_CAR_WITHIN_DATE);
            }
            return CompletableFuture.completedFuture(new CarDTO(responseCarObj));
        } finally {
            logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
        }
    }

    public Object exportCarReport(String reportFormat) throws DataNotFoundException {
        logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has started in Thread:" + Thread.currentThread().getName());
        try{
            List<Car> allCarInfo = carRepository.findAll();
            if (allCarInfo.size() == 0) {
                logger.error(NO_CAR_DATA);
                throw new DataNotFoundException(NO_CAR_DATA);
            }
            String reportPath=System.getProperty("user.dir")+File.separator+"Reports"+ File.separator+"Car"+File.separator+CommonUtils.getFormattedCurrentDateTimeString.apply("yyyy-MM-dd-HH-mm-ss");
            String jasperReportTemplateSource=System.getProperty("user.dir")+"/src/main/resources/cars.jrxml";
            return CommonUtils.generateReport(jasperReportTemplateSource,reportFormat,reportPath,allCarInfo,"Car Report");
        }finally {
            logger.info("Method:" + Thread.currentThread().getStackTrace()[2].getMethodName() + " has ended in Thread:" + Thread.currentThread().getName());
        }
    }

}
