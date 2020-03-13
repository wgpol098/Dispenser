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
Password: Tibia1234
Port: 3306
root@localhost
baza danych póki co testowa: test
tabela poki co testowe: testowa
pola w niej: (klucz głowny to id)id smallint unsigned not null auto_increment, name varchar(20) not null


Uruchamianie serwera:
service mysql start
Przejście do gui bazy danych:
mysql -u root -p




------------------

nginx

Lokalizacja: /etc/nginx/sites-available/default - plik konfiguracyjny nginx
nginx -t - sprawdzanie czy dana konfiguracja jest poprawna
