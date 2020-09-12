# DispenserJSON - API

DispenserJSON jest to część projektu z pracowni programowania zespołowego. W tutejszym `README.MD` znajdziesz poszczególne opisy controllerów, posiadane metody oraz treści znajdujące się w bazie danych.

## Configuration

Zanim serwer zacznie działać, wymagane jest utworzenie bazy danych. Najprostrzym sposobem utworzenia bazy danych oraz utworzenia jej migracji jest:
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
POST("login") - sprawdza przesłane dane i przyznaje dostęp do konta dispensera
* Send: "idDispenser" - *int*, "name" - *string*
* Receive: "login" - *string*, "password" - *string*

POST("register") - metoda rejestrująca dispenser
* Send: "login" - *string*, "password" - *string*, "name" - *string*, "typeAccount" - *int*
* Receive: *ActionResult*
 
POST("check") - metoda sprawdzająca typ konta
* Send: "login" - *string*
* Receive: "typeAccount" - *int*

DELETE - metoda usuwająca konto dispensera
* Send: "login" - *string*, "password" - *string*
* Receive: *ActionResult*

#### Android
POST("GetPlan") - metoda pobierająca listę zaplanowanych leków, danego dispensera z tabeli *Plans*, dla opiekuna
* Send: "idDispenser" - *int*
* Receive: List("hour" - *int*, "minutes" - *int*, "description" - *string*, idRecord - *int*)

POST("GetPlanRecord") - metoda pobierająca dane pojedyńczego rekordu z tabeli *Plans*
* Send: "idRecord" - *int*
* Receive: "hour" - List(*int*, "minutes" - *int*, "days" - *int*, "description" - *string*, "count" - *int*, "periodicity" - *int*)

POST("GetHistory") - metoda pobierająca listę wziętych leków, danego dispensera z tabeli *History*, dla opiekuna
* Send: "idDispenser" - *int*
* Receive: "idDispenser" - *int*, "dateAndTime" - *Date*, "noWindow" - *int*, "description" - *string*, "flag" - *int*

POST("GetDoctorHistory") - metoda pobierająca listę wziętych leków, danego dispensera z tabeli *History*, dla lekarza
* Send: "idDispenser" - *int*
* Receive: List("description" - *string*, "start" - *string*, "end" - *string*, "tabDidnttake" - List("date" - *string*), "firstHour" - *string*, "periodicity" - *int*, "count" - *int*)

POST("GetDoctorPlan") - metoda pobierająca listę zaplanowanych leków, danego dispensera z tabeli *Plans*, dla lekarza
* Send: "idDispenser" - *int*
* Receive: List("description" - *string*, "start" - *string*, "daysLeft" - *int*, "firstHour" - *string*, "periodicity" - *int*, "tabDidnttake" - List("date" - *string*), "idRecord" - *int*)

POST("GetWindows") - metoda pobierająca informację o wolnych miejscach w dispenserze
* Send: "idDispenser" - *int*
* Receive: "freeWindows" - *int*, "occupiedWindows" - *int*

POST("GetDayInfo") - metoda pobierająca dane z danego dnia
* Send: "idDispenser" - *int*, "dateAndTime" - *string*
* Receive: List("hours" - *int*, "minutes" - *int*, "description" - *string*, "idRecord" - *int*, "noWindow" - *int*, "flag" - *int*, "tableFlag" - *int*)

POST("GetCallendarInfo") - metoda pobierająca dane z danego miesiąca
* Send: "idDispenser" - *int*, "month" - *int*, "year" - *int*
* Receive: List("dateAndTime" - *Date*, "flag" - *int*)

POST("UpdateCounter") - metoda sprawdzająca licznik wykonanych czynności, danego dispensera
* Send: "idDispenser" - *int*
* Receive: "noUpdate" - *int*

POST - dodanie leku do danego dispensera
* Send: "hour" - *int*, "minutes" - *int*, "count" - *int*, "days" - *int*, "periodicity" - *int*, description - *string*, "idDispenser" - *int*
* Receive: *ActionResult*

PUT - zmiana wartości leku danego rekordu
* Send: "hour" - *int*, "minutes" - *int*, "count" - *int*, "days" - *int*, "periodicity" - *int*, description - *string*, "idRecord" - *int*
* Receive: *ActionResult*

DELETE - usunięcie danego rekordu (leku)
* Send: "idRecord" - *int*
* Receive: *ActionResult*

#### Debug
GET("plans") - wyświetla zawartość rekordów w tabeli *Plans*
* Send: *No parameters*
* Receive: List("id" - *int*, "idDispenser" - *int*, "dateAndTime" - *DateTime*, "noWindow" - *int*, "description" - *string*)

GET("history") - wyświetla zawartość rekordów w tabeli *History*
* Send: *No parameters*
* Receive: List("id" - *int*, "idDispenser" - *int*, "dateAndTime" - *DateTime*, "noWindow" - *int*, "description" - *string*, "flag" - *int*)

GET("dispensers") - wyświetla zawartość rekordów w tabeli *Dispensers*
* Send: *No parameters*
* Receive: List("id" - *int*, "idDispenser" - *int*, "noWindow" - *string*, "noUpdate" - *int*)

GET("accounts") - wyświetla zawartość rekordów w tabeli *Accounts*
* Send: *No parameters*
* Receive: List("id" - *int*, "login" - *string*, "password" - *string*, "name" - *string*, "typeAccount" - *int*)

GET("listofdispensers") - wyświetla zawartość rekordów w tabeli *ListOfDispensers*
* Send: *No parameters*
* Receive: List("id" - *int*, "idAccount" - *int*, "idDispenser" - *int*, "name" - *string*)

#### Disp
GET - wysyła listę rekordów z tabeli *Plans* 
* Send: "idDispenser" - *string*
* Receive: List("DateAndTime" - *DateTime*, "NoWindow" - *int*)

POST - tworzenie rekordów do tabeli *History*, usunięcie starych rekordów w tabeli *Plans*
* Send: "idDispenser" - *int*, "dateAndTime" - *DateTime*, "noWindow" - *int*, "flag" - *int*
* Receive: *ActionResult*

POST("PresentationPost") - uruchomienie diody dispensera na żądanie 
* Send: "numberWindow" - *int*, "windowFlag" - *int*
* Receive: *ActionResult*

GET("PresentationGet") - pobieranie informacji o zapalonej diodzie na żądanie
* Send: *No parameters*
* Receive: List("id" - *int*, "flag" - *int*)

#### Dispenser
POST - stworzenie danych dla nowo zarejestrowanego dispensera
* Send: "login" - *string*, "idDispenser" - *int*, "name" - *string*
* Receive: *ActionResult*

DELETE - usunięcie danych istniejącego dispensera
* Send: "login" - *string*, "idDispenser" - *int*, "name" - *string*
* Receive: *ActionResult*

## Server Datatables

#### Plans
id | idDispenser | dateAndTime | noWindow | description
---|-------------|-------------|----------|------------
int | int | Date | int | string

#### History
id | idDispenser | dateAndTime | noWindow | description | flag
---|-------------|-------------|----------|-------------|-----
int | int | Date | int | string | int

#### Dispensers
id | idDispenser | noWindow | noUpdate
---|-------------|-------------|----------
int | int | Date | int 

#### Accounts
id | login | password | name | typeAccount
---|-------------|-------------|----------|-------------
int | string | string | string | string | int

#### ListOfDispensers
id | idAccount | idDispenser | name 
---|-------------|-------------|----------
int | int | int | string

### Todos
 - *Zrobić tłumaczenie readme na język angielski*