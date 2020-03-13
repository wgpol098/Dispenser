# Dispenser


Lokalizacje:

APK - aplikacja na android
LocalDebugServer - lokalna aplikacja do debugowania
VPNServer - aplikacja, która jest aktualnie na serwerze
MakingServer - aplikacja, która nie działa i jest robiona

------------------

LocalDebugServer

Dostępne ścieżki:

"/api/Dispenser/Post/" - Zmienia zawartość pliku Test.txt		(Content)
"/api/Dispenser/Get/" - Zwraca zawartość pliku Test.txt  		(Content)
"/api/Dispenser/PostInt/" - Zmienia zawartość pliku Test.txt		(Int)
"/api/Dispenser/GetInt/" -  Zwraca zawartość pliku Test.txt  		(Int)
"/api/Dispenser/Display/" - Zwraca boolowskie TRUE			(Bool)

W pliku hosts (Ścieżka: C:\Windows\System32\drivers\etc) dodajemy na samym końcu linijkę:
(Twoje ip)	(Link strony)
np.
158.75.95.64		konfederacjaumk.pl

Ścieżka do Managera IIS: C:\Windows\System32\inetsrv
Aplikacja: iis.exe

------------------

MySQL Server

Database: Dispenser
Password: Tibia1234 albo Tibia123
Port: 3306
Tabela: Logowanie
Komórki: DispenserID, Login, Password, STATUS

Uruchamianie serwera:
service mysql start

mysql -u root -p - przejście do gui bazy danych

------------------

nginx

Lokalizacja: /etc/nginx/sites-available/default - plik konfiguracyjny nginx
nginx -t - sprawdzanie czy dana konfiguracja jest poprawna
