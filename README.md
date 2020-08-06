# infor-car-microservice
Microservice designed using Spring Boot-Embedded H2 DB to create and handle Car Related Services.

  - Registering New Car with details.
  - Search All Existing Car Records.
  - Search Selective Records based on Criteria.
  - Generate Reports.
  
Different endpoints being exposed are:

Register New Car: http://localhost:3002/rest/car/addNewCar

Search All Cars: http://localhost:3002/rest/car/all

Search Cars by Dates and Price: http://localhost:3002/rest/car/search
[Query Parameters: availableStartDate, availableEndDate and maximumRentalPrice]

Generate Report of All Cars: http://localhost:3002/rest/car/all/reports/{reportType}
