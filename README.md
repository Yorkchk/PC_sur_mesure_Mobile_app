# Project Name: Custom PC

## Project Members

| First Name | LAST NAME    | GitHub Username |
|------------|--------------|-----------------|
| Youssef    | Cherkaoui    | Yorkchk         |
| Yasser     | El Mouatadir | yaselmo         |
| Sofia      | El Ouazzani  | Sofiaelouazzani |
| Ismail     | Khayati      | Ismish87        |
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

### Proposed Implicit Requirements

<to be completed (optional)>

### Assumptions

<to be completed (optional)>

## Modeling

### Use Case Diagrams (optional)

```plantuml
@startuml
actor Assembler
actor Requester

usecase "get All Commands To Assemble" as UC1
usecase "Accept Command Validation" as UC2
usecase "Deny Command Validation " as UC3
usecase "Reject Command" as UC4
usecase "Update Stock " as UC5
usecase "Choose Component And Quantity" as UC6
usecase "Add in cart" as UC7
usecase "Create Command" as UC8
usecase "Get Commands Of Requester" as UC9
usecase "Delete Command" as UC10
usecase "Edit Command" as UC11
usecase "Delete Component" as UC12
usecase "Change Quantity by 1 at a time" as UC13
usecase "Manage Components Of Command" as UC14
usecase "Handle Stock Validation Error" as UC17

usecase "Add Component In Command" as UC16

Requester --> UC6 : <<include>>
UC6 --> UC7 : <<include>>
UC7 --> UC8 : <<include>>

Requester --> UC9 : <<include>>
UC9 --> UC10 : <<include>>
UC9 --> UC11 : <<include>>
UC11 --> UC12 : <<include>>

UC11 --> UC14 : <<include>>
UC14 --> UC16 : <<include>>
UC14 --> UC13 : <<include>>

Assembler --> UC1 : <<include>>
UC1 --> UC2 : <<include>>
UC1 --> UC3 : <<include>>
UC1 --> UC4 : <<include>>
UC2 --> UC5 : <<include>>
UC2 --> UC17 : <<extends>>
@enduml
```

### State Diagrams

```plantuml
@startuml
[*] --> Command_pending : Order created

Command_pending --> Command_Rejected : Assembler rejection

Command_pending --> Command_assembled : Assembler validation
@enduml
```

### Requester Sequence Diagrams

```plantuml
@startuml
actor aRequester as Requester
entity aCommand
entity aStock

Requester -> aStock : getAllComponents()
Requester -> Requester : addComponentInCart()
Requester -> aStock : CheckQuantities(Map<Component,Integer>)

loop For each component in Stock
    aStock -> aStock : isComponentValid(Component, Integer)

    alt NotValid
        aStock -> Requester : displayErrorMessage()
        return
    else
        Requester --> aCommand : <<create>> Command(Map<Component, Integer>)
    end
end

alt ChangeQuantity
    Requester --> aCommand : changeQuantity(int)
else deleteComponents
    Requester -> aCommand : chooseComponent()
    aCommand --> aCommand : deleteComponent()
else addComponentInCommand
    Requester -> Requester : addComponentInCart()
    Requester --> aCommand : addComponentToCommand(Command)
else deleteCommand
    Requester --> aCommand : deleteCommand(Command)
    destroy aCommand
end
@enduml
```

### Assembler Sequence Diagrams

```plantuml
@startuml
actor anAssembler as Assembler
entity aCommand
entity aStock

aStock -> Assembler : getAllComponentsInStock()

alt validateCommand
   aCommand -> aStock : checkCommandQuantities()
   aStock --> aCommand : Approved
   Par
      aCommand -> aStock : updateStock()
      aCommand -> aCommand : setStatus(Validated)
   end

else rejectCommand
      aCommand -> aCommand : setStatus(Rejected)
end
@enduml
```

### Orders

```plantuml
@startuml
[*] --> WaitingForApproval : "Order created by a Requester"

WaitingForApproval --> AcceptedAssembling : "Order accepted by the Assembler"

AcceptedAssembling -> Delivered : "Order delivered"

WaitingForApproval --> Rejected : "Order rejected by the Assembler"

Delivered --> [*]

Rejected --> [*]
@enduml
```

## Activity Diagrams

### Home and Authentication

```plantuml
@startuml
title Authentication

start
    :Initialize application;

    :Connect to the database;

    while (Back button pressed?) is (No)
        :Display home window;

        if (OK button pressed?) is (Yes)
            :Validate username and password;

            if (Authentication successful) then (Yes)
                If (User is an Administrator) then (Yes)
                    :Display Administrator window;

                    :...;
                elseif (User is a StoreKeeper) then (Yes)
                    :Display StoreKeeper window;

                    :...;
                elseif (User is an Assembler) then (Yes)
                    :Display Assembler window;

                    :...;
                elseif (User role is Requester) then (Yes)
                    :Display Requester window;

                    :...;
                else
                    :Display design error: unknown role;
                endif
            else (No)
                :Display authentication error;
            endif
        endif
    endwhile (Yes)

    :Release resources (database...);
stop
@enduml
```

### User Management

```plantuml
@startuml
actor Administrator

control AdministratorActivity
database Database

Administrator --> AdministratorActivity : Create a user

AdministratorActivity <--> Database : Check username uniqueness

alt Unique username
    AdministratorActivity <--> Database : Add the new user
    AdministratorActivity --> Administrator : Creation successful confirmation
else Username already exists
    AdministratorActivity --> Administrator : Display an error
end
@enduml
```

## Error Management

User management includes validations and error messages:

- **Authentication error**: Message displayed when username or password is incorrect.
- **Unauthorized role**: Message displayed if a user tries to access a feature not allowed for their role.
- **Username already exists**: Error displayed when trying to create a user with an existing username.
- **Order quantity exceeds stock quantity**: Error displayed when creating an order containing a component exceeding stock availability.

These measures ensure the application remains secure and user-friendly for all user types.

### Stock Management

```plantuml
@startuml
actor StoreKeeper

control StockActivity
database Database

StoreKeeper --> StockActivity : Add a component

StockActivity <--> Database : Check component ID uniqueness

alt Unique ID
    StockActivity <--> Database : Save the new component with the provided details
    StockActivity --> StoreKeeper : Add successful confirmation
else ID already exists
    StockActivity --> StoreKeeper : Display an error
end
@enduml
```

### Placing an Order

```plantuml
@startuml
actor Requester

control OrderActivity
database Database
control StockService

Requester --> OrderActivity : Create an order

OrderActivity --> StockService : Check component availability

alt All components are available
    OrderActivity --> Database : Save the order with details (initiator, components, creation date)
    Database --> OrderActivity : Save confirmation
    OrderActivity --> Requester : Order creation confirmation
else Components unavailable
    OrderActivity --> Requester : Display an error message
end
@enduml
```

### Processing an Order

```plantuml
@startuml
actor Assembler
actor StoreKeeper

control OrderService
database Database

Assembler --> OrderService : View an order\n(Pending approval)
OrderService --> Database : Load the order

alt The order is valid
    Assembler --> OrderService : Approve order
    OrderService --> Database : Update status to "Accepted for assembly"

    StoreKeeper --> OrderService : Prepare the order for assembly
    OrderService --> Database : Update status to "Assembled and ready for delivery"

    StoreKeeper --> OrderService : Deliver the order
    OrderService --> Database : Update status to "Delivered"

else The order is invalid
    Assembler --> OrderService : Reject the order
    OrderService --> Database : Update status to "Rejected"
end
@enduml
```

## Sequence Diagrams

### Home and Authentication

```plantuml
@startuml
actor Unknown

control MainActivity
control AdministratorActivity
control StoreKeeperActivity
control AssemblerActivity
control RequesterActivity

Unknown --> MainActivity : Authentication request\n(with username and password)

MainActivity <--> Database : Search for a user\nwith username and password

alt User exists
    MainActivity <--> Database : Get user information\n(including role)

    alt User role is Administrator
        MainActivity --> AdministratorActivity
    else User role is StoreKeeper
        MainActivity --> StoreKeeperActivity
    else User role is Assembler
        MainActivity --> AssemblerActivity
    else User role is Requester
        MainActivity --> RequesterActivity
    else Unknown role
        MainActivity --> Unknown : Display design error
    end
else Otherwise
    MainActivity --> Unknown : Display authentication error
end

database Database
@enduml
```

### Administrator Role

```plantuml
@startuml
actor Administrator

control AdministratorActivity

database Database

Administrator --> AdministratorActivity : Create a user

AdministratorActivity <--> Database : Get user list

alt User already exists
    AdministratorActivity --> Administrator: Display an error
else Otherwise
    AdministratorActivity --> Database : Add a row to the Users table
end
@enduml
```

### StoreKeeper Role

```plantuml
@startuml
actor StoreKeeper

control StoreKeeperActivity
control listComponent
database Database

StoreKeeper --> StoreKeeperActivity : Access component management

StoreKeeperActivity --> Database : Get stock component list
StoreKeeperActivity --> Database : Add components to the database
StoreKeeperActivity --> Database : Delete a component
StoreKeeperActivity --> Database : Modify a component
StoreKeeperActivity --> Database : Increase or reduce component quantity

alt Component already exists
    StoreKeeperActivity --> StoreKeeper : Display "Component already exists" error
else
    StoreKeeperActivity --> Database : Add component
end
@enduml
```

### Assembler Role

```plantuml
@startuml
actor Assembler

control AssemblerActivity
database Database

Assembler --> AssemblerActivity : Log in
AssemblerActivity --> Database : Verify credentials

alt Login successful
    AssemblerActivity --> Assembler : Display dashboard
else
    AssemblerActivity --> Assembler : Display "Invalid credentials" error
end

Assembler --> AssemblerActivity : Modify personal information
AssemblerActivity --> Database : Update information

Assembler --> AssemblerActivity : Log out
AssemblerActivity --> Database : End session
@enduml
```

### Requester Role

```plantuml
@startuml
actor Requester

control RequesterActivity
database Database

Requester --> RequesterActivity : Log in
RequesterActivity --> Database : Verify credentials

alt Login successful
    RequesterActivity --> Requester : Display dashboard
else
    RequesterActivity --> Requester : Display "Invalid credentials" error
end

Requester --> RequesterActivity : Modify personal information
RequesterActivity --> Database : Update information

Requester --> RequesterActivity : Log out
RequesterActivity --> Database : End session
@enduml
```

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

