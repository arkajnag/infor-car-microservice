package io.CarService.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.CarService.exceptionhandler.DataNotAllowedException;
import io.CarService.exceptionhandler.DataNotFoundException;
import io.CarService.exceptionhandler.DuplicateDataNotAllowedException;
import io.CarService.exceptionhandler.NullFormatException;
import io.CarService.model.Car;
import io.CarService.model.CarDTO;
import io.CarService.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Car API Service", consumes = "application/json",produces = "application/json")
@RestController(value = "Car Controller")
@RequestMapping(value = "/rest/car")
public class CarController {
	
	@Autowired
	private CarService carService;
	
    @ApiOperation(value = "GET All Car Information", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "Successful"),
            @ApiResponse(code = 400,message = "BAD Request"),
            @ApiResponse(code = 401,message = "Unauthorized"),
            @ApiResponse(code = 404,message = "Page Not Found"),
            @ApiResponse(code = 500,message = "Server Error")
    })
    @GetMapping(value="/all",produces = "application/json")
    public CompletableFuture<ResponseEntity<CarDTO>> getAllCarsInformation() throws DataNotFoundException {
        return carService.getAllCarsInformation()
                .thenApply(carDTO-> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(carDTO));
    }

    @ApiOperation(value = "Create new Car Information", httpMethod = "POST",produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "Successful"),
            @ApiResponse(code = 400,message = "BAD Request"),
            @ApiResponse(code = 401,message = "Unauthorized"),
            @ApiResponse(code = 404,message = "Page Not Found"),
            @ApiResponse(code = 500,message = "Server Error")
    })
    @PostMapping(value="/addNewCar",consumes = "application/json",produces = "application/json")
    public CompletableFuture<ResponseEntity<Car>> addNewCarInformation(@RequestBody Car carObj) throws NullFormatException, DuplicateDataNotAllowedException, DataNotAllowedException {
        return carService.addNewCarInformation(carObj).thenApply(car ->ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(car));
    }

    @ApiOperation(value = "GET Car Information based on Search Criteria", httpMethod = "GET", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 200,message = "Successful"),
            @ApiResponse(code = 400,message = "BAD Request"),
            @ApiResponse(code = 401,message = "Unauthorized"),
            @ApiResponse(code = 404,message = "Page Not Found"),
            @ApiResponse(code = 500,message = "Server Error")
    })
    @GetMapping(value = "/search", produces = "application/json")
    public CompletableFuture<ResponseEntity<CarDTO>> searchAvailableCarsOnConditions(@RequestParam(required = true,value = "availableStartDate")String availableStartDate,
                                                                                     @RequestParam(required = true,value="availableEndDate")String availableEndDate,
                                                                                     @RequestParam(required=true,value="maximumRentalPrice")String maximumRentalPrice)
            throws NullFormatException, DataNotAllowedException, DataNotFoundException {
        Double requestedRentalPrice=Double.parseDouble(maximumRentalPrice);
        return carService.searchCarsByAvailabilityAndRentalPrice(availableStartDate,availableEndDate,requestedRentalPrice)
                .thenApply(carDTO-> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(carDTO));
    }
	
    @GetMapping(value="/all/reports/{type}", produces = "application/json")
    public String getCarReport(@PathVariable(value = "type", required = true)String type) throws DataNotFoundException{
        Object response=carService.exportCarReport(type);
        return (response==null)?"Car Report file is created!!":"Error in generating the Report";
    }

}
