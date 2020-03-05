# DispenserJSON

Rozpisanie poszczególnych klas, metod, danych dispensera. Aktualnie jest w formie WIP (Projekt + .md)

### Jak użyć

Serwer wymaga migracji bazy danych oraz zaaktualizowania jego danych poprzez *Package Manager Console*.
```
> Add-Migration init
> Update-Database
```

### DispenserController

Znajdują się w nim metody routingowe, które odpowiadają o np. rozkazach pobrania danych. 

Aktualnie, nasza główna strona `/api/Dispenser/` przedstawia wszystkie dostępne zasoby danych (nie pełnych), które znajdują się na serwerze. Używa podstawowej metody `GetAllAsync()` przy użyciu atrybutu `HttpGet`. Czyli krótko opisując, zwraca JSON'a ze wszystkimi dostępnymi "skróconymi" danymi. W tej metodzie jest użyte mapowanie zasobów za pomocą `AutoMappera`.

Aby zdobyć cały dostępny model konta potrzeba użyć formy `/api/Dispenser/{login}/{password}/`. W ten sposób możemy przejść do JSON'a ze wszystkimi dostępnymi zmiennymi. W tej metodzie (`GetByLoginAndPassword(string login, string password)`) jest sprawdzane hasło, jeżeli będzie błędne nie przekieruje do dalszej strony. Za pomocą loginu szuka czy istnieje także dany dispenser.