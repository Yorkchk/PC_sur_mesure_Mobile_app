# uOttawa - 2024-2025 - SEG2505A - Projet - Groupe <à compléter>

Nom du projet : Pc sur mesure

## Membres du projet

| Prénom      | NOM         | Identifiant GitHub |
|-------------|-------------|--------------------|
|Youssef      |Cherkaoui    |Yorkchk             |
|Yasser       |El Mouatadir |yaselmo             |
|Sofia        |El Ouazzani  |Sofiaelouazzani     |
|Ismail       |Khayati      |Ismish87            |
|Amine        |Baba         |AmineBaba10         |

## Introduction

Le projet PC sur mesure a pour objectif de développer une application de commande de PC personnalisée, répondant aux besoins variés des utilisateurs en matière de choix de composants. Ce système permet aux utilisateurs de différents rôles (Administrator, StoreKeeper, Assembler, et Requester) d’interagir avec les fonctionnalités de l’application selon leur profil, facilitant ainsi une gestion collaborative et structurée.

Ce livrable se concentre spécifiquement sur la mise en œuvre des fonctionnalités du rôle de Assembler, permettant à ce dernier d'assembler des commandes en annalisant les détails de la commande. Ainsi que le role du Requester qui a la possiblilité de creer des commandes à partir de composantes sur le stock. Puis, l'Assembleur a le choix de valider ou de rejeter la commande. 

Les fonctionnalités ont été conçues pour garantir un processus fluide et intuitif, répondant aux exigences fonctionnelles et techniques. Le projet inclut également la structure et les outils nécessaires à une maintenance évolutive, tels que l'initialisation de la base de données et la gestion des utilisateurs.

Notre équipe est engagée à fournir une application robuste et fiable, qui respecte les spécifications et offre une expérience utilisateur efficace et sécurisée pour tous les intervenants.


## Clarifications sur les exigences

### Exigences explicites reformulées

Le systeme doit pouvoir au Requester de créer une commande
Le systeme doit pouvoir au Requester de consulter ces commandes
Le systeme doit pouvoir au Requester de changer la quantité d'une composante de sa commande
Le systeme doit pouvoir au Requester d'ajouter des composantes dans sa commande
Le systeme doit pouvoir au Requester de supprimer des composantes de sa commande
Le systeme doit pouvoir au Requester de supprimer sa commande

Le systeme doit pouvoir à l'Assembler de consulter les commandes à assembler
Le systeme doit pouvoir à l'Assembleur de rejeter les commandes qu'il souhaite pour n'importe quelle raison
Le systeme doit pouvoir à l'Assembleur de valider les commandes qu'il souhaite
Le systeme ne doit pas valider les commandes qui demandent des composantes en rupture de stock.
### Exigences implicites proposées

<à compléter (optionnel)>

### Hypothèses

<à compléter (optionnel)>

## Modélisation



### Diagrammes d'utilisation (optionnel)


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
usecase "Delete Componant" as UC12
usecase "Change Quantity by 1 at a time" as UC13
usecase "Manage Componants Of Command" as UC14
usecase "Handle Stock Validation Error" as UC17


usecase "Add Componant In Command" as UC16


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


### Diagrammes d'états
@startuml
[*] --> Command_pending : Commande créée

Command_pending -->  Command_Rejected : Assembler rejection



Command_pending --> Command_assembled : Assembler validation



@enduml

### Diagrammes de séquence du Requester

@startuml
actor aRequester as Requester
entity aCommand
entity aStock

Requester -> aStock : getAllComponants()
Requester -> Requester : addComponantInCart()
Requester -> aStock : CheckQuantities(Map<Componant,Integer>)

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
else deleteComponants
    Requester -> aCommand : chooseComponant()
    aCommand --> aCommand : deleteComponant()
else addComponantIn Command
    Requester -> Requester : addComponantInCart()
    Requester --> aCommand : addComponantToCommand(Command)
else deleteCommand
    Requester --> aCommand : deleteCommand(Command)
    destroy aCommand
    

end

@enduml



### Diagrammes de séquence de l'Assembler

@startuml
actor anAssembler as Assembler
entity aCommand
entity aStock

aStock -> Assembler : getAllComponantsInStock()

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





#### Commandes


@startuml
    [*] -->  WaitingForApproval : "Création de la commande par un Requester"

    WaitingForApproval --> AcceptedAssembling : "Acceptation de la commande par l'Assembler"

    AcceptedAssembling -> Delivered : "Livraison de la commande"
    
    WaitingForApproval --> Rejected : "Rejet de la commande par l'Assembler"
    
    Delivered --> [*]
    
    Rejected --> [*]
@enduml

### Diagrammes d'activités

#### Accueil et authentification


@startuml
    title Authentification

    start
        :Initialiser l'application;

        :Se connecter à la base de données;
            
        while (Appui sur la touche de retour ?) is (Non) 
            :Afficher de la fenêtre d'accueil;
        
            if (Appui sur le bouton "OK" ?) is (Oui)
                :Valider de l'identifiant et du mot de passe;
        
                if (Authentification validée) then (Oui)
                    If (L'utilisateur est un Administrator) then (Oui)
                        :Afficher la fenêtre d'un Administrator;

                        :...;
                    elseif (L'utilisateur est un StoreKeeper) then (Oui)
                        :Afficher la fenêtre d'un StoreKeeper;

                        :...;
                    elseif (L'utilisateur est un Assembler) then (Oui)
                        :Afficher la fenêtre d'un Assembler;

                        :...;
                    elseif (L'utilisateur a le rôle Requester) then (Oui)
                        :Afficher la fenêtre d'un Requester;

                        :...;
                    else
                    :EAfficher une rreur de conception: rôle inconnu;
                    endif
                else (Non)
                    :Afficher une erreur d'authentification;
                endif
            endif
        endwhile (Oui)

        :Libérer les ressources (base de données...);
    stop
@enduml

#### Gestion des utilisateurs

@startuml
actor Administrator

control AdministratorActivity
database Database

Administrator --> AdministratorActivity : Créer un utilisateur

AdministratorActivity <--> Database : Vérifier l'unicité de l'identifiant

alt Identifiant unique
    AdministratorActivity <--> Database : Ajouter le nouvel utilisateur
    AdministratorActivity --> Administrator : Confirmation de création réussie
else Identifiant déjà existant
    AdministratorActivity --> Administrator : Afficher une erreur
end
@enduml

### Gestion des erreurs

La gestion des utilisateurs comprend des validations et messages d’erreur :
- **Erreur d’authentification** : Message affiché en cas d’identifiant ou mot de passe incorrect.
- **Rôle non autorisé** : Message affiché si un utilisateur tente d’accéder à une fonctionnalité non autorisée pour son rôle.
- **Identifiant déjà existant** : Erreur affichée lors de la tentative de création d’un nouvel utilisateur avec un identifiant déjà présent dans la base de données.
- **Quantité de Commande dépasse celui du stock** : Erreur affichée lors de la création d'une commande qui contient une composante qui dépasse la qantité du Stock
  
Ces mesures assurent que l'application reste sécurisée et intuitive pour tous les types d'utilisateurs.


#### Gestion du stock

@startuml
actor StoreKeeper

control StockActivity
database Database

StoreKeeper --> StockActivity : Ajouter un composant

StockActivity <--> Database : Vérifier l'unicité de l'identifiant du composant

alt Identifiant unique
    StockActivity <--> Database : Enregistrer le nouveau composant avec les détails fournis
    StockActivity --> StoreKeeper : Confirmation d'ajout réussie
else Identifiant déjà existant
    StockActivity --> StoreKeeper : Afficher une erreur
end
@enduml

#### Passage d'une commande

@startuml
actor Requester

control OrderActivity
database Database
control StockService

Requester --> OrderActivity : Créer une commande

OrderActivity --> StockService : Vérifier disponibilité des composants

alt Tous les composants sont disponibles
    OrderActivity --> Database : Enregistrer la commande avec les détails (initiateur, composants, date de création)
    Database --> OrderActivity : Confirmation d'enregistrement
    OrderActivity --> Requester : Confirmation de création de la commande
else Composants indisponibles
    OrderActivity --> Requester : Afficher un message d'erreur
end
@enduml

#### Traitement d'une commande

@startuml
actor Assembler
actor StoreKeeper

control OrderService
database Database

Assembler --> OrderService : Consulter une commande\n (En attente d'approbation)
OrderService --> Database : Charger la commande

alt La commande est valide
    Assembler --> OrderService : Approve commande
    OrderService --> Database : Mettre à jour le statut "Acceptée pour assemblage"
    
    StoreKeeper --> OrderService : Préparer la commande pour assemblage
    OrderService --> Database : Mettre à jour le statut "Assemblé et prêt pour livraison"
    
    StoreKeeper --> OrderService : Livrer la commande
    OrderService --> Database : Mettre à jour le statut "Livrée"
    
else La commande est invalide
    Assembler --> OrderService : Rejeter la commande
    OrderService --> Database : Mettre à jour le statut "Rejetée"
end
@enduml

### Diagrammes de séquences

#### Pour l'accueil et l'authentification

@startuml
    actor Inconnu

    control MainActivity
    control AdministratorActivity
    control StoreKeeperActivity
    control AssemblerActivity
    control RequesterActivity

    Inconnu --> MainActivity : Demande d'authentification\n(avec identifiant et mot de passe)

    MainActivity <--> Database : Rechercher un utilisateur\navec un identifiant et un mot de passe

    alt L'utilisateur existe
        MainActivity <--> Database : Obtenir des informations sur l'utilisateur\n(dont son rôle) 
        
        alt Le rôle de l'utilisateur est Administror
            MainActivity --> AdministratorActivity
        else Le rôle de l'utilisateur est StoreKeeper
            MainActivity --> StoreKeeperActivity
        else Le rôle de l'utilisateur est Assembler
            MainActivity --> AssemblerActivity
        else Le rôle de l'utilisateur est Requester
            MainActivity --> RequesterActivity
        else Rôle inconnu 
            MainActivity --> Inconnu : Afficher une erreur de conception
        end
    else Sinon
        MainActivity --> Inconnu : Afficher une erreur d'authentification
    end

    database Database
@enduml

#### Pour le rôle Administrator


@startuml
    actor Administrator

    control AdministratorActivity
    
    database Database

    Administrator --> AdministratorActivity : Créer un utilisateur

    AdministratorActivity <--> Database : Obtenir la liste des utilisateurs

    alt L'utilisateur existe déjà
        AdministratorActivity --> Administrator: Afficher une erreur
    else Sinon
        AdministratorActivity --> Database : Ajouter une ligne à la table Users
    end
@enduml

#### Pour le rôle StoreKeeper

@startuml actor StoreKeeper

control StoreKeeperActivity control listComposant database Database

StoreKeeper --> StoreKeeperActivity : Accès à la gestion des composants

StoreKeeperActivity --> Database : Obtenir la liste des composants du stock StoreKeeperActivity --> Database : Ajouter des composants dans la base de données StoreKeeperActivity --> Database : Supprimer un composant StoreKeeperActivity --> Database : Modifier un composant StoreKeeperActivity --> Database : Augmenter ou réduire la quantité d'un composant

alt Le composant existe déjà StoreKeeperActivity --> StoreKeeper : Afficher une erreur "Composant déjà existant" else StoreKeeperActivity --> Database : Ajouter le composant end

@enduml

#### Pour le rôle Assembler

@startuml actor Assembler

control AssemblerActivity database Database

Assembler --> AssemblerActivity : Se connecter AssemblerActivity --> Database : Vérifier les identifiants

alt Connexion réussie AssemblerActivity --> Assembler : Afficher le tableau de bord else AssemblerActivity --> Assembler : Afficher une erreur "Identifiants invalides" end

Assembler --> AssemblerActivity : Modifier les informations personnelles AssemblerActivity --> Database : Mettre à jour les informations

Assembler --> AssemblerActivity : Se déconnecter AssemblerActivity --> Database : Terminer la session @enduml


#### Pour le rôle Requester

@startuml actor Requester

control RequesterActivity database Database

Requester --> RequesterActivity : Se connecter RequesterActivity --> Database : Vérifier les identifiants

alt Connexion réussie RequesterActivity --> Requester : Afficher le tableau de bord else RequesterActivity --> Requester : Afficher une erreur "Identifiants invalides" end

Requester --> RequesterActivity : Modifier les informations personnelles RequesterActivity --> Database : Mettre à jour les informations

Requester --> RequesterActivity : Se déconnecter RequesterActivity --> Database : Terminer la session @enduml

## Eléments de conception

Architecture du système L'application suit une architecture similaire à celle du mvc model (model-view-controller), où les interfaces utilisateurs communiquent avec des classes controllers qui gèrent l'intéraction avec les utilisateurs. On a regroupé ces classes dans le dossier ui. Puis, ces classes communiquent à leurs tours avec des classes qui contiennent le business code spécifique à l'application. Ces classes représentent aussi les entités participantes dans cette application. Enfin, ces classes manipulent les données en communiquant avec une base de donnée no sql firebase.

Choix technologiques Langage de programmation : Java Plateforme : Android Studio Base de données : Firebase Firestore pour la gestion des données en temps réel.

Conception des bases de données Table Users :

username (String) password (String) role (String, peut être "Requester", "StoreKeeper", "Administrator", "Assembler") email (String, unique) firstName (String) lastName (String)

Table Components :

type (String) subtype (String) description (String, unique) comment (String, optionnel) creationDate (Timestamp) modificationDate (Timestamp) quantity (int) 4. Interfaces utilisateur L'application propose des interfaces distinctes pour chaque rôle, avec des fonctionnalités adaptées. Par exemple, l'interface de l'Administrator comprend des options pour ajouter ou supprimer des utilisateurs, tandis que l'interface du StoreKeeper permet de gérer le stock.

Gestion des erreurs Des messages d'erreur appropriés sont affichés à l'utilisateur en cas d'échec de l'authentification ou d'une erreur lors de la mise à jour des données. Par exemple, si un utilisateur essaie de se connecter avec des identifiants invalides, un message d'erreur s'affiche indiquant que les informations saisies sont incorrectes

## Eléments d'implémentation

<à compléter (optionnel)>

## Eléments de tests unitaires

<à compléter (outils utiliser, comment les lancer, etc.)>

## Comment reconstruire la solution

<à compléter (optionnel)>

## Comment installer et utiliser la solution

<à compléter (optionnel)>

## Eléments de démonstration

### Scénario ("storyboard") suggéré

<à compléter (optionnel)>

### Valeurs de test

#### Utilisateurs

| Rôle           | Identifiant de connexion | Mot de passe |
|----------------|--------------------------|--------------|
| Administrateur |yasser@elmouatadir.com     |yasser123    | 
| StoreKeeper    |Ismail_Khayati@gmail.com   |Ismail       |
| Assembler      |Sofia_Elouazzani@gmail.com |sofia123     |
| Requester      |youssef_cherkaoui@gmail.com|youssef      |

#### Fichier de données exemple

Vous pouvez trouver des fichiers xml qui contiennent des informations sur des users et des composantes en guise de test dans le dossier data dans groupe-8/PCsurmesure/data

Vous pouvez aussi trouver l'état actuel de la base de donnée dans un fichier json dans le meme dossier

## Limites et problèmes connus

Parfois en appliquant une opération les toast messages peuvent ne pas apparaitre ou donner une mauvaise iddée à l'utilisation meme si l'opération a été exécuté parfaitement.

## Information destinées aux correcteurs

<à compléter (optionnel)>