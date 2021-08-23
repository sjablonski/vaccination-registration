# Vaccination Registration App

## Technology Stack:
- React with TypeScript
- Java with Spring Boot
- Apache Kafka
- Express.js

Frontend App:
- Patients can register for vaccination
- Verification of the entered data
- Display information about the status of the submission

Rest API:
- Handling HTTP requests
- Verification of submitted data
- Communication with Apache Kafka
- Sending emails to patients

Reservation App:
- Database handling
- Making a registration for vaccination
- Communication with Apache Kafka

MongoDB model:

```
{
    _id: String,
    date: String,
    time: String,
    patients: [{
        firstName: String,
        lastName: String,
        pesel: String,
        email: String,
        phoneNumber: String
    }],
    address: {
        institutionName: String,
        street: String,
        city: String,
        state: String,
        zip: String
    }
}

```