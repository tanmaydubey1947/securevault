# SecureVault Application

## Overview

SecureVault is a robust application designed for secure financial transactions and user management. The application consists of two main services: PaymentService and UserService. Both services are part of the same project but are maintained in different branches.

## Services

### PaymentService

The PaymentService handles all payment-related operations. It provides functionality for:
- **Add Money**: Allows users to add funds to their accounts.
- **Send Money**: Facilitates transferring funds between users.

### UserService

The UserService manages user-related operations, including:
- **Add User**: Enables the addition of new users to the system.
- **Authenticate User**: Authenticates users using JWT tokens for secure access.

## Communication Between Services

The PaymentService and UserService communicate through Kafka. Kafka serves as a bridge to handle events such as:
- **Add Money**: Notifies the PaymentService when money is added to a user's account.
- **Send Money**: Sends transactions between users.

## Branch Structure

- **main**: The default branch where the stable version of the application resides.
- **payment-service**: Contains the code and features related to the PaymentService.
- **user-service**: Contains the code and features related to the UserService.