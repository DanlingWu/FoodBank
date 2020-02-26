# FoodBank

This is a basic micro service REST API to add and find FoodBanks.
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

1. IntelliJ with Maven
2. MySQL
3. POSTMAN (optional)

The initial data was populated from src/main/resources/data.sql
### To Run
Edit the run configuration to point to this class 
```
com/findfoodbank/rest/FindFoodBankRestServiceApplication.java 
```
### To Use
Get the list of all FoodBanks
```
 curl http://localhost:8080/food-bank
```
Add new a FoodBank to the database.
```
curl -X POST -H "Content-Type: application/json" -d '{ "name": "Test 2", "address": "Clifton Terrace, Finsbury Park, London N4 3JP"}' http://localhost:8080/food-bank
```
Find the nearby FoodBanks by supplying params as below (distance is in miles).
```
/food-bank/nearest/{lat}/{lng}/{distance}
curl http://localhost:8080/food-bank/nearest/51.564089/-0.1126616/2
```
### To Do
- Stop duplicate entries
- Cache the Google Map API call to reduce usage
- Cache the results of the search to be more efficient
- Login and auth system for admin user 

