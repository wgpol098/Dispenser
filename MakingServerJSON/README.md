# DispenserJSON - API

Rozpisanie poszczególnych klas, metod, danych dispensera. Aktualnie jest w formie WIP (Projekt + .md)

### Configuration

Server wymaga migracji bazy danych oraz zaaktualizowania jego danych poprzez *Package Manager Console*.
```
> Add-Migration init
> Update-Database
```

### Controllers

Znajdują się w nim metody routingowe, które odpowiadają o np. rozkazach pobrania danych. 

Aktualnie, posiadamy 3 controllery: `/api/Dispenser/`, `/api/Account/` oraz `/api/disp/`. Controller Dispensera przedstawia wszystkie dostępne zasoby danych (nie pełnych), które znajdują się na serwerze. Używa podstawowej metody `GetAllAsync()` przy użyciu atrybutu `HttpGet`. Czyli krótko opisując, zwraca JSON'a ze wszystkimi dostępnymi "skróconymi" danymi. W tej metodzie jest użyte mapowanie zasobów za pomocą `AutoMappera`.

Aby zdobyć dostępny model dispensera, trzeba zalogować się poprzez wysłanie JSON'a (login+password) pod adres `/api/Account/`. W ten sposób możemy przejść do JSON'a ze wszystkimi dostępnymi zmiennymi. W tej metodzie jest sprawdzane hasło, więc jeżeli będzie błędne nie przekieruje do dalszej strony, oddany zostanie null. Za pomocą loginu szuka czy istnieje także dany dispenser (aktaulnie *FirstOrDefault*).

##### *Dispenser* 
###### (Prawdopodnie zostanie usunięte w najbliższym czasie)
GET - pobiera listę wszystkich dispenserów
POST - dodaje nowy dispenser
PUT (z id) - zmienia wartości danego dispensera
DELETE (z id) - kasuje dany dispenser

##### *Account*
###### (Początkowy schemat kont)
GET - przyjmuję json (login + password) i wyświetla wszystkie dispensery

##### *disp*
GET - odbiera żądanie z dispensera (przesyła mu także json), wysyła listę jsonów danego dispensera (zapisywanie do History i usuwanie z Plans do zrobienia)
GET("debugplans") - pobiera i wyświetla wszystkie rekordy z tabeli Plans
GET("debughistory") - pobiera i wyświetla wszystkie rekordy z tabeli History


### Model

Możemy tutaj ustalać różne modele danych do bazy danych. Posiadamy aktualnie 2 modele danych, jeden od konta użytkownika, a drugie od dispenserów.

##### Dispenser

| Pole | Typ |
| ---- | --- |
| Id | int |
| Name | string |
| TurnDiode | bool |

##### Account

| Pole | Typ |
| ---- | --- |
| Id | int |
| Login | string |
| Password | string |
| Dispensers | Dispenser |
| DispenserId | int |

##### Plan

| Pole | Typ |
| ---- | --- |
| Id | int |
| DispenserId | int |
| dateTime | DateTime |
| Nr_Okienka | int |
| Opis | string |

##### History

| Pole | Typ |
| ---- | --- |
| Id | int |
| Login | string |
| Password | string |
| Dispensers | Dispenser |
| DispenserId | int |

### Repositories

Tutaj znajdują się interfejsy dotyczące metod controllera. Interfejs `IDispenserRepository` jest głównie przeznaczony dla `DispenserController`, lecz możemy go wykorzystać wśród innych controllerów. Posiada podstawową formę deklaracji metod, aby przetwarzać rekordy w bazie danych.

Tutaj przechowujemy główne instrukcje wobec bazy danych. Używamy *contextu*, aby wejść do tabel baz danych.

`IUnitOfWork` jest interfejsem, który deklaruję metodę zabezpieczającą wysyłania/odbierania żądań asynchronicznych.

### Services

W folderze Communiaction znajdują się pliki, które określają czy dane żądanie jest zakończone sukcesem lub napotkał dany problem.

### Extensions

Głównym celem tej klasy jest zwracanie opisów błędów modeli. Używane jest przy `BadRequestach`.

### Mapping

W tym miejscu tworzymy/mapujemy połączenia pomiędzy modelem głównym, a modelem z ograniczonym zasobem. Istnieje, aby żądania zwrotne były mniejsze oraz żeby nie zwracały ważnych danych przez sieć, jak np. hasło do konta.

### Persistence

Pliki dotyczące podstawowych danych dla bazy danych. Właściwości pól w bazie oraz seed z podstawowymi danymi.

### Resources

W tym miejscu przedstawiamy modele z ograniczonymi zasobami. Ustalamy co może dane żądanie zwrócić, z jakimi polami. Tutaj także ustalamy jakie dane JSONa są wysyłane do serwera, jakie pola można wysłać z danymi atrybutami np. `Required`, `MaxLenght`.

### Services 

Implementacja logiki wobec bazy danych.

### Migrations

Automatycznie generowane pliki, które przetwarzają dane wobec bazy danych serwera.

### Todos

 - *ModelResource, który posiada informacje T/F (czy istnieje użytkownik) oraz z ID dispensera*
 - *Zapytanie, które zwróci godziny przyjmowania leków (trzeba ustalić format czasu dla ASP .NET core, javy oraz pythona)*
 - *Funkcje z modelem, który posiada dokładną datę (dzień, miesiąc, rok, godzina) oraz czy dana osoba wzięła lek*
 - *Zwracanie listy dispenserów wobec danego użytkownikca (akutalnie 1 dispenser na 1 konto)*