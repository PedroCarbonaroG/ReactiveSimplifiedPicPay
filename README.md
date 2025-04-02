# Reactive Simplified PicPay

## Overview

Reactive Simplified PicPay is a modern, reactive web application built using Spring WebFlux. This project leverages the power of reactive programming to provide a highly scalable and efficient system for managing transactions, users, and various entities. The application is designed to handle high loads with minimal latency, making it ideal for real-time applications.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Configuration](#configuration)
- [Exception Handling](#exception-handling)
- [Security](#security)
- [Entities and Repositories](#entities-and-repositories)
- [Services](#services)
- [Mappers](#mappers)
- [Running the Application](#running-the-application)

## Features

- Reactive programming with Spring WebFlux
- MongoDB for reactive data storage
- Comprehensive exception handling
- Secure authentication and authorization
- Detailed logging
- PDF and Excel report generation

## Technologies

- Java 17
- Spring Boot
- Spring WebFlux
- Spring Data MongoDB
- Spring Security
- MapStruct
- Reactor
- Thymeleaf
- Apache POI
- OpenHTMLToPDF

## Architecture

The application follows a layered architecture with clear separation of concerns:

- **Controllers**: Handle HTTP requests and responses.
- **Services**: Contain business logic.
- **Repositories**: Interact with the database.
- **Mappers**: Convert between entities and DTOs.
- **Configuration**: Set up application-wide settings.
- **Exception Handling**: Manage application errors gracefully.

## Configuration

The configuration classes are designed to set up the application context and security settings. Key configuration files include:

- `SecurityConfig.java`: Configures security settings, including JWT authentication.
- `ReactiveSimplifiedPicPayApplication.java`: Main application class to bootstrap the application.

## Exception Handling

The application has a robust exception handling mechanism to ensure that errors are managed gracefully. Key exception classes include:

- `BadRequestException.java`
- `EmptyException.java`
- `NotFoundException.java`
- `TransactionValidationException.java`
- `SecurityException.java`

These exceptions are handled globally to provide meaningful error messages to the client.

## Security

Security is a critical aspect of the application. The security configuration includes:

- **JWT Authentication**: Secure token-based authentication.
- **Role-Based Access Control**: Different roles (USER, ADMIN) with specific permissions.
- **Password Encoding**: Secure password storage using BCrypt.

Key classes:

- `SecurityConfig.java`
- `SystemUserService.java`
- `SystemUserRepository.java`

## Entities and Repositories

The application uses MongoDB for data storage, with reactive repositories for non-blocking data access. Key entities and repositories include:

- **Entities**: `NaturalPerson`, `LegalPerson`, `Transaction`, `SystemUser`
- **Repositories**: `INaturalPersonRepository`, `ILegalPersonRepository`, `ITransactionRepository`, `ISystemUserRepository`

## Services

Services contain the business logic of the application. Key services include:

- `NaturalPersonService.java`: Manages natural persons.
- `LegalPersonService.java`: Manages legal persons.
- `TransactionService.java`: Handles transactions.
- `WalletService.java`: Manages wallet operations.
- `ExportingService.java`: Generates PDF and Excel reports.

## Mappers

MapStruct is used for mapping between entities and DTOs. Key mappers include:

- `IPersonMapper.java`
- `ITransactionMapper.java`

These mappers ensure that data is correctly transformed between different layers of the application.

## Running the Application

To run the application, use the following command:

```bash
./mvnw spring-boot:run
