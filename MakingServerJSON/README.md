# DispenserJSON - API

DispenserJSON jest to część projektu z pracowni programowania zespołowego. W tutejszym `READ.ME` znajdziesz poszczególne opisy controllerów, posiadane metody oraz treści znajdujące się w bazie danych.

## Configuration

Zanim server zacznie działać, wymagane jest utworzenie bazy danych w danym systemie operacyjnym. Najprostrzym sposobem utworzenia bazy danych oraz utworzenia jej migracji jest:
1. Uruchomienie projektu w Visual Studio
2. Uruchomienie terminala *Package Manager Console*
3. Wpisanie poniższych komend w terminalu *Package Manager Console*

Komendy do *Package Manager Console*:
```
> Add-Migration init
> Update-Database
```

## Server Controllers

Znajdują się w nim metody routingowe, które odpowiadają o np. rozkazach pobrania danych. Aktualnie server posiada 5 controllerów: 

* Dispenser     *(/api/Dispenser/)*
* Account       *(/api/Account/)*
* Android       *(/api/Android)*
* Disp          *(/api/Disp)*
* Debug         *(/api/Debug/)*

### List of avalible methods of controllers

#### Account
POST("login") - 
* Send:
* Receive:

POST("register") - 
* Send:
* Receive: *ActionResult*
 
POST("check") - 
* Send:
* Receive:

DELETE - 
* Send:
* Receive: *ActionResult*

#### Android
POST - 
* Send:
* Receive: *ActionResult*

POST("GetPlan") - 
* Send:
* Receive:

POST("GetPlanRecord") - 
* Send:
* Receive:

POST("GetHistory") - 
* Send:
* Receive:

POST("GetDoctorHistory") - 
* Send:
* Receive:

POST("GetDoctorPlan") - 
* Send:
* Receive:

POST("GetWindows") - 
* Send:
* Receive:

POST("UpdateCounter") - 
* Send:
* Receive:

PUT - 
* Send:
* Receive: *ActionResult*

DELETE - 
* Send:
* Receive: *ActionResult*

#### Disp
GET - wysyła listę rekordów z tabeli *Plans* 
* Send: "idDispenser" - *string*
* Receive: List("DateAndTime" - *DateTime*, "NoWindow" - *int*)

POST - tworzenie rekordów do tabeli History, usunięcie starych rekordów w tabeli Plans
* Send: "idDispenser" - *int*, "dateAndTime" - *DateTime*, "noWindow" - *int*, "flag" - *int*
* Receive: *ActionResult*

#### Dispenser
GET - 
* Send: 
* Receive: *ActionResult*

POST - 
* Send: 
* Receive: *ActionResult*

#### Debug
GET("plans") - dodaje nowy dispenser
* Send: *No parameters*
* Receive: List("id" - *int*, "idDispenser" - *int*, "dateAndTime" - *DateTime*, "noWindow" - *int*, "description" - *string*)

GET("history") - dodaje nowy dispenser
* Send: *No parameters*
* Receive: List("id" - *int*, "idDispenser" - *int*, "dateAndTime" - *DateTime*, "noWindow" - *int*, "description" - *string*, "flag" - *int*)

GET("dispensers") - dodaje nowy dispenser
* Send: *No parameters*
* Receive: List("id" - *int*, "idDispenser" - *int*, "noWindow" - *string*, "noUpdate" - *int*)

GET("accounts") - dodaje nowy dispenser
* Send: *No parameters*
* Receive: List("id" - *int*, "login" - *string*, "password" - *string*, "name" - *string*, "typeAccount" - *int*)

GET("listofdispensers") - dodaje nowy dispenser
* Send: *No parameters*
* Receive: List("id" - *int*, "idAccount" - *int*, "idDispenser" - *int*, "name" - *string*)

### Server Datatables

Możemy tutaj ustalać różne modele danych do bazy danych. Posiadamy aktualnie 2 modele danych, jeden od konta użytkownika, a drugie od dispenserów.

##### *Miejsce na tabele*

### Server Models

##### *Miejsce na modele*

### Todos

 - *Zrobić tłumaczenie readme na język angielski*
 - *Rozpisać readme dla controllerów Dispenser, Account, Android*
 - *Dopisać rodzaje tabel oraz modele w readme*