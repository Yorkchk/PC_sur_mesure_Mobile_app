# Project Name: Custom PC

## Why this project exists

*PC Sur Mesure* (Custom PC) was a team project for **SEG2505: Introduction to Software Engineering** at the **University of Ottawa** (Fall 2024). It is an Android application, written in Java with Firebase Firestore, that lets an organization order custom-built PCs from the components it has in stock. Four roles (Administrator, StoreKeeper, Assembler, Requester) each have their own interface. Our team of five built it over several graded deliverables, going through requirements analysis, UML modeling, design, implementation and testing, and working with Git branches and pull requests.

> **Note on the commit history:** the original repository belongs to the course's private GitHub organization and can't be made public. This repo is a copy of the code.


## Project Members

| First Name | LAST NAME    | GitHub Username |
|------------|--------------|-----------------|
| Youssef    | Cherkaoui    | Yorkchk         |
| Yasser     | El Mouatadir | yaselmo         |
| Sofia      | El Ouazzani  | Sofiaelouazzani |
| Ismail     | khayati      | Ismish87        |
| Amine      | Baba         | AmineBaba10     |

## Introduction

The Custom PC project aims to develop a personalized PC ordering application that meets the various needs of users regarding component selection. This system allows users with different roles (Administrator, StoreKeeper, Assembler, and Requester) to interact with the application's features according to their profile, enabling collaborative and structured management.

This deliverable focuses specifically on implementing the functionalities of the Assembler role, allowing them to assemble orders by analyzing order details. It also includes the Requester role, which has the ability to create orders using components available in stock. Then, the Assembler can choose to approve or reject the order.

The functionalities were designed to ensure a smooth and intuitive process while meeting functional and technical requirements. The project also includes the structure and tools necessary for scalable maintenance, such as database initialization and user management.

Our team is committed to delivering a robust and reliable application that follows the specifications and provides an efficient and secure user experience for all stakeholders.

## Requirement Clarifications

### Reformulated Explicit Requirements

- The system must allow the Requester to create an order.
- The system must allow the Requester to view their orders.
- The system must allow the Requester to change the quantity of a component in their order.
- The system must allow the Requester to add components to their order.
- The system must allow the Requester to remove components from their order.
- The system must allow the Requester to delete their order.

- The system must allow the Assembler to view orders to assemble.
- The system must allow the Assembler to reject orders for any reason.
- The system must allow the Assembler to approve orders.
- The system must not approve orders requesting components that are out of stock.


## Modeling

### Use Case Diagrams (optional)

![use-case](docs/diagrams/use-case.svg)

<sub>Source: [`docs/diagrams/use-case.puml`](docs/diagrams/use-case.puml)</sub>

### State Diagrams

![state-command](docs/diagrams/state-command.svg)

<sub>Source: [`docs/diagrams/state-command.puml`](docs/diagrams/state-command.puml)</sub>

### Requester Sequence Diagrams

![seq-requester-command](docs/diagrams/seq-requester-command.svg)

<sub>Source: [`docs/diagrams/seq-requester-command.puml`](docs/diagrams/seq-requester-command.puml)</sub>

### Assembler Sequence Diagrams

![seq-assembler-command](docs/diagrams/seq-assembler-command.svg)

<sub>Source: [`docs/diagrams/seq-assembler-command.puml`](docs/diagrams/seq-assembler-command.puml)</sub>

### Orders

![state-order](docs/diagrams/state-order.svg)

<sub>Source: [`docs/diagrams/state-order.puml`](docs/diagrams/state-order.puml)</sub>

## Activity Diagrams

### Home and Authentication

![activity-authentication](docs/diagrams/activity-authentication.svg)

<sub>Source: [`docs/diagrams/activity-authentication.puml`](docs/diagrams/activity-authentication.puml)</sub>

### User Management

![seq-user-management](docs/diagrams/seq-user-management.svg)

<sub>Source: [`docs/diagrams/seq-user-management.puml`](docs/diagrams/seq-user-management.puml)</sub>

## Error Management

User management includes validations and error messages:

- **Authentication error**: Message displayed when username or password is incorrect.
- **Unauthorized role**: Message displayed if a user tries to access a feature not allowed for their role.
- **Username already exists**: Error displayed when trying to create a user with an existing username.
- **Order quantity exceeds stock quantity**: Error displayed when creating an order containing a component exceeding stock availability.

These measures ensure the application remains secure and user-friendly for all user types.

### Stock Management

![seq-stock-management](docs/diagrams/seq-stock-management.svg)

<sub>Source: [`docs/diagrams/seq-stock-management.puml`](docs/diagrams/seq-stock-management.puml)</sub>

### Placing an Order

![seq-place-order](docs/diagrams/seq-place-order.svg)

<sub>Source: [`docs/diagrams/seq-place-order.puml`](docs/diagrams/seq-place-order.puml)</sub>

### Processing an Order

![seq-process-order](docs/diagrams/seq-process-order.svg)

<sub>Source: [`docs/diagrams/seq-process-order.puml`](docs/diagrams/seq-process-order.puml)</sub>

## Sequence Diagrams

### Home and Authentication

![seq-authentication](docs/diagrams/seq-authentication.svg)

<sub>Source: [`docs/diagrams/seq-authentication.puml`](docs/diagrams/seq-authentication.puml)</sub>

### Administrator Role

![seq-administrator](docs/diagrams/seq-administrator.svg)

<sub>Source: [`docs/diagrams/seq-administrator.puml`](docs/diagrams/seq-administrator.puml)</sub>

### StoreKeeper Role

![seq-storekeeper](docs/diagrams/seq-storekeeper.svg)

<sub>Source: [`docs/diagrams/seq-storekeeper.puml`](docs/diagrams/seq-storekeeper.puml)</sub>

### Assembler Role

![seq-assembler](docs/diagrams/seq-assembler.svg)

<sub>Source: [`docs/diagrams/seq-assembler.puml`](docs/diagrams/seq-assembler.puml)</sub>

### Requester Role

![seq-requester](docs/diagrams/seq-requester.svg)

<sub>Source: [`docs/diagrams/seq-requester.puml`](docs/diagrams/seq-requester.puml)</sub>

## Design Elements

### System Architecture

The application follows an architecture similar to the MVC (Model-View-Controller) model, where user interfaces communicate with controller classes that manage user interactions. These classes are grouped in the `ui` folder. Then, these classes communicate with other classes containing the business logic specific to the application. These classes also represent the entities participating in the application. Finally, these classes manipulate data by communicating with a Firebase NoSQL database.

### Technological Choices

- Programming Language: Java
- Platform: Android Studio
- Database: Firebase Firestore for real-time data management.

### Database Design

#### Users Table

- username (String)
- password (String)
- role (String, can be "Requester", "StoreKeeper", "Administrator", "Assembler")
- email (String, unique)
- firstName (String)
- lastName (String)

#### Components Table

- type (String)
- subtype (String)
- description (String, unique)
- comment (String, optional)
- creationDate (Timestamp)
- modificationDate (Timestamp)
- quantity (int)

### User Interfaces

The application provides separate interfaces for each role with role-specific functionalities. For example, the Administrator interface includes options to add or remove users, while the StoreKeeper interface allows stock management.

### Error Handling

Appropriate error messages are displayed to the user in case of authentication failure or data update errors. For example, if a user tries to log in with invalid credentials, an error message indicates that the entered information is incorrect.


### Test Values

#### Users

| Role           | Login Identifier             | Password  |
|----------------|------------------------------|-----------|
| Administrator  | yasser@elmouatadir.com       | yasser123 |
| StoreKeeper    | Ismail_Khayati@gmail.com     | Ismail    |
| Assembler      | Sofia_Elouazzani@gmail.com   | sofia123  |
| Requester      | youssef_cherkaoui@gmail.com  | youssef   |

#### Example Data File

You can find XML files containing information about users and components for testing purposes in the `data` folder located at `groupe-8/PCsurmesure/data`.

You can also find the current state of the database in a JSON file in the same folder.

## My Contribution

*Youssef Cherkaoui ([@Yorkchk](https://github.com/Yorkchk))*

This was a team effort. These are the parts I worked on:

**Architecture & foundations**
- Organized the project structure (`models` / `ui` packages) and started the authentication flow.
- Designed the `User` inheritance hierarchy (`Admin`, `StoreKeeper`, `Assembler`, `Requester`), including role changes.
- Connected the app to the team's Firestore database and fixed Gradle and library version issues.

**Authentication & accounts**
- Role-based redirection after login, email-based login, and fixes to sign-up and account settings.
- Navigation drawer menu, logout, and home pages for the roles.

**Administrator & StoreKeeper**
- Admin features: add, remove and edit users, plus the admin stock view.
- StoreKeeper back end and front end: add and edit components and manage stock.
- An XML importer that loads users and components from several files ([`ReadXmlDomParser`](PCsurmesure/app/src/main/java/com/example/pcsurmesure/models/ReadXmlDomParser.java)), and the XML/JSON data in `PCsurmesure/data`.

**Requester ordering flow**
- Created the [`Command`](PCsurmesure/app/src/main/java/com/example/pcsurmesure/models/Command.java) (order) model and the [`Requester`](PCsurmesure/app/src/main/java/com/example/pcsurmesure/models/Requester.java) logic: create, edit and delete an order, add or delete a component, change quantities.
- Connected the order screens to the back end ([`createOrderActivity`](PCsurmesure/app/src/main/java/com/example/pcsurmesure/ui/createOrderActivity.java), `ListOrder`, `EditOrderList` and their adapters).

**Assembler approval flow**
- Implemented order approval with stock validation and update ([`Assembler`](PCsurmesure/app/src/main/java/com/example/pcsurmesure/models/Assembler.java), [`Stock`](PCsurmesure/app/src/main/java/com/example/pcsurmesure/models/Stock.java)), and showed the order status in the UI.
- Finished integrating the front end for the final deliverable.

**Integration & quality**
- Merged branches through pull requests, fixed bugs across the project, and helped with the `TestAssembler` tests and the README.

