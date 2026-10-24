# Gift Mailer

Aplikacja do przeprowadzania losowania **Mikołajek** oraz automatycznej wysyłki powiadomień e-mail.

---

## Główne funkcjonalności

- **Pewne losowanie (Derangement)** – kryptograficznie bezpieczny generator losowy (`SecureRandom`) z gwarancją, że nikt nie wylosuje samego siebie.
- **Dyskrecja i bezpieczeństwo** – organizator nie poznaje par w trybie produkcyjnym (aplikacja loguje wyłącznie fakt wysłania maila do darczyńcy, nie ujawniając odbiorcy).
- **Tryb testowy (`dry.run`)** – możliwość przetestowania wyników losowania na konsoli przed właściwą wysyłką wiadomości.
- **Format wiadomości HTML + Tekst** – responsywny, świąteczny szablon HTML z informacjami o zasadach i limicie budżetowym oraz wersja `text/plain` dla starszych klientów poczty.
- **Konfiguracja SMTP** – bezproblemowa integracja z serwerami pocztowymi (domyślnie Gmail przez STARTTLS/Port 587).

---

## Stack technologiczny

| Technologia | Opis |
| :--- | :--- |
| **Java 21 LTS** | Nowoczesne mechanizmy Javy (`record`, `Stream API`, Text Blocks) |
| **Maven** | Zarządzanie zależnościami i procesem budowania |
| **Simple Java Mail 9.x** | Nowoczesna i elastyczna biblioteka do wysyłki e-maili przez SMTP |
| **SLF4J + Simple Logger** | Przejrzyste logowanie zdarzeń |

---

## Struktura projektu

```text
gift-mailer/
├── members.txt.example         # Przykładowa lista uczestników
├── secrets.properties.example  # Wzorzec konfiguracji SMTP
├── pom.xml                     # Konfiguracja Mavena i zależności
└── src/main/java/dev/jakubbalinski/
    ├── Main.java               # Punkt wejścia aplikacji
    ├── config/
    │   ├── AppConfig.java      # Ładowanie konfiguracji ogólnej (w tym dry.run)
    │   └── MailConfig.java     # Konfiguracja połączenia z serwerem SMTP
    ├── models/
    │   ├── Member.java         # Reprezentacja uczestnika (imię, e-mail)
    │   └── DrawResult.java     # Wynik losowania (darczyńca -> obdarowany)
    ├── services/
    │   ├── DrawService.java    # Logika algorytmu losowania
    │   └── SendMailService.java# Przygotowanie i wysyłka wiadomości e-mail
    └── utils/
        └── FileReader.java     # Parsowanie pliku wejściowego uczestników
```

---

##  Wymagania wstępne

- **JDK 21** lub nowszy
- **Apache Maven** (lub wsparcie Mavena w IDE, np. IntelliJ IDEA)
- Skrzynka pocztowa z dostępem do SMTP (dla Gmaila wymagane jest wygenerowanie tzw. **Hasła do aplikacji** w ustawieniach konta Google)

---

## Konfiguracja

Przed uruchomieniem aplikacji przygotuj dwa pliki w głównym katalogu projektu:

### 1. `members.txt`
Lista uczestników w formacie CSV (`Imię,adres_email`):
```csv
Jan Kowalski,jan.kowalski@example.com
Anna Nowak,anna.nowak@example.com
Piotr Wiśniewski,piotr.wisniewski@example.com
```

### 2. `secrets.properties`
Skopiuj wzorzec z `secrets.properties.example` i uzupełnij dane:
```properties
# Konfiguracja serwera SMTP
smtp.host=smtp.gmail.com
smtp.port=587

# Dane uwierzytelniające
smtp.user=twoj.email@gmail.com
smtp.password=twoje_16_znakowe_haslo_aplikacji

# Tryb pracy:
# true  = tylko wypisuje wynik losowania w konsoli (brak wysyłki e-maili)
# false = faktycznie wysyła maile do wylosowanych osób
dry.run=true
```

> [!IMPORTANT]
> Pliki `secrets.properties` oraz `members.txt` zawierają poufne dane i są automatycznie ignorowane przez `.gitignore`. Nigdy nie commituj ich do repozytorium!

---

## ▶️ Uruchomienie

### Krok 1: Weryfikacja w trybie Dry-Run
Upewnij się, że w `secrets.properties` masz ustawione:
```properties
dry.run=true
```
Skompiluj i uruchom projekt (w terminalu lub bezpośrednio z poziomu IDE):
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="dev.jakubbalinski.Main"
```
W konsoli wyświetlą się wylosowane pary – możesz zweryfikować poprawność losowania bez ryzyka wysłania wiadomości.

### Krok 2: Właściwa wysyłka
Gdy wszystko jest gotowe, zmień flagę w `secrets.properties`:
```properties
dry.run=false
```
i uruchom aplikację ponownie. Wiadomości zostaną rozesłane, a w logach pojawi się potwierdzenie wysyłki do każdego z uczestników.
