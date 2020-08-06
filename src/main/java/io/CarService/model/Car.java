package io.CarService.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name="tblCar")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "Car Model")
public class Car {

    @Id
    @Column(name = "carPlateNumber",unique = true)
    @ApiModelProperty(value="Car Plate Number", notes = "This is Primary Key for Car Model", allowableValues = "Alphanumeric", allowEmptyValue = false, required = true)
    private Integer carPlateNumber;
    @Column(name = "carModelName", nullable = false, length = 100)
    @ApiModelProperty(value="Car Model Name", notes = "Model Name of the Car", allowableValues = "Alphanumeric", allowEmptyValue = false, required = true)
    private String carModelName;
    @Column(name = "carAvailableStartDate", nullable = false, length = 50)
    @ApiModelProperty(value="Availability Start Date", notes = "From when the Car is available(yyyy-MM-ddThh:mm:ss)", allowableValues = "Date format(yyyy-MM-ddThh:mm:ss)", allowEmptyValue = false, required = true)
    private LocalDateTime carAvailableStartDate;
    @Column(name = "carAvailableEndDate",nullable = false, length = 50)
    @ApiModelProperty(value="Availability End Date", notes = "Till when the Car is available(yyyy-MM-ddThh:mm:ss)", allowableValues = "Date format(yyyy-MM-ddThh:mm:ss)", allowEmptyValue = false, required = true)
    private LocalDateTime carAvailableEndDate;
    @Column(name = "carRentalPricePerHour",length = 20)
    @ApiModelProperty(value="Car Rental Price Per Hour", notes = "Car Rental per Hour")
    private Double carRentalPricePerHour;
	public Integer getCarPlateNumber() {
		return carPlateNumber;
	}
	public void setCarPlateNumber(Integer carPlateNumber) {
		this.carPlateNumber = carPlateNumber;
	}
	public String getCarModelName() {
		return carModelName;
	}
	public void setCarModelName(String carModelName) {
		this.carModelName = carModelName;
	}
	public LocalDateTime getCarAvailableStartDate() {
		return carAvailableStartDate;
	}
	public void setCarAvailableStartDate(LocalDateTime carAvailableStartDate) {
		this.carAvailableStartDate = carAvailableStartDate;
	}
	public LocalDateTime getCarAvailableEndDate() {
		return carAvailableEndDate;
	}
	public void setCarAvailableEndDate(LocalDateTime carAvailableEndDate) {
		this.carAvailableEndDate = carAvailableEndDate;
	}
	public Double getCarRentalPricePerHour() {
		return carRentalPricePerHour;
	}
	public void setCarRentalPricePerHour(Double carRentalPricePerHour) {
		this.carRentalPricePerHour = carRentalPricePerHour;
	}
	public Car(Integer carPlateNumber, String carModelName, LocalDateTime carAvailableStartDate,
			LocalDateTime carAvailableEndDate, Double carRentalPricePerHour) {
		this.carPlateNumber = carPlateNumber;
		this.carModelName = carModelName;
		this.carAvailableStartDate = carAvailableStartDate;
		this.carAvailableEndDate = carAvailableEndDate;
		this.carRentalPricePerHour = carRentalPricePerHour;
	}
	public Car() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "Car [carPlateNumber=" + carPlateNumber + ", carModelName=" + carModelName + ", carAvailableStartDate="
				+ carAvailableStartDate + ", carAvailableEndDate=" + carAvailableEndDate + ", carRentalPricePerHour="
				+ carRentalPricePerHour + "]";
	}
    
    
}
