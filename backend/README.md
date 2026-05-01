# Action'Elles Assurance - Backend

Backend Spring Boot de l'application Action'Elles Assurance, realisee pour digitaliser la simulation et la souscription de produits d'assurance automobile NSIAGO'ASSUR.

## Stack

- Java 17
- Spring Boot 3.2
- Spring Security avec JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- iText 7 pour les attestations PDF
- Swagger/OpenAPI via Springdoc

## Fonctionnalites

- Authentification JWT.
- Gestion des roles `ADMIN` et `AMAZONE`.
- Simulation de prime automobile.
- Consultation et modification des simulations non souscrites.
- Souscription a partir d'un devis valide.
- Generation d'une attestation PDF avec logo et QR code.
- Gestion des utilisateurs par l'administrateur.
- Visibilite metier : une amazone voit uniquement ses souscriptions et ses assures, l'admin voit tout.

## Prerequis

- JDK 17
- PostgreSQL
- Gradle wrapper fourni dans le projet

## Base De Donnees

Creer une base PostgreSQL locale :

```sql
CREATE DATABASE assurance;
CREATE USER assurance WITH PASSWORD 'assurance';
GRANT ALL PRIVILEGES ON DATABASE assurance TO assurance;
```

Configuration par defaut dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/assurance
spring.datasource.username=assurance
spring.datasource.password=assurance
server.port=8075
```

Flyway cree automatiquement le schema et insere les donnees de reference au demarrage.

## Lancement

Depuis le dossier `backend` :

```bash
./gradlew bootRun
```

Sur Windows :

```powershell
.\gradlew.bat bootRun
```

L'API est disponible sur :

```text
http://localhost:8075/api/v1
```

Swagger est disponible sur :

```text
http://localhost:8075/swagger-ui.html
```

## Comptes De Test

Les mots de passe initiaux des comptes inseres par Flyway sont identiques.

| Login | Role |
| --- | --- |
| admin | ADMIN |
| abadou | ADMIN |
| ibamba | ADMIN |
| daka | ADMIN |
| akouadio | ADMIN |
| amazone1 | AMAZONE |
| amazone2 | AMAZONE |

Mot de passe initial :

```text
eburtis2020
```

## Endpoints Principaux

### Authentification

```http
POST /api/v1/securite/auth
```

Exemple :

```json
{
  "username": "admin",
  "password": "eburtis2020"
}
```

### Simulations

```http
GET  /api/v1/simulations
POST /api/v1/simulations
GET  /api/v1/simulations/{id}
PUT  /api/v1/simulations/{id}
```

Exemple de creation :

```json
{
  "produitCode": "PAPILLON",
  "categorieCode": "201",
  "datePremiereMiseEnCirculation": "2024-01-10",
  "puissanceFiscale": 3,
  "valeurNeuve": 10000000,
  "valeurVenale": 6000000
}
```

La reponse contient notamment :

- `quoteReference` au format `QT` + 12 caracteres.
- `endDate` a 2 semaines.
- `price`.
- le detail des garanties retenues.

### Souscriptions

```http
POST /api/v1/subscriptions
GET  /api/v1/subscriptions
GET  /api/v1/subscriptions/{id}
GET  /api/v1/subscriptions/status/{id}
GET  /api/v1/subscriptions/{id}/attestation
```

### Assures

```http
GET /api/v1/assures
GET /api/v1/assures/{id}
```

### Utilisateurs

```http
GET /api/v1/utilisateurs
GET /api/v1/utilisateurs/{id}
POST /api/v1/utilisateurs
PUT /api/v1/utilisateurs/{id}
PUT /api/v1/utilisateurs/me/mot-de-passe
```

## Regles Metier

### Produits

| Produit | Garanties | Categories |
| --- | --- | --- |
| Papillon | RC, DOMMAGES, VOL | 201 |
| Douby | RC, DOMMAGES, TIERCE COLLISION | 202 |
| Douyou | RC, DOMMAGES, TIERCE COLLISION, INCENDIE | 201, 202 |
| Toutourisquou | Toutes garanties | 201 |

### Garanties

- RC : tarif selon la puissance fiscale.
- DOMMAGES : 2,60% de la valeur neuve, vehicules de 0 a 5 ans.
- TIERCE COLLISION : 1,65% de la valeur neuve, vehicules de 0 a 8 ans.
- TIERCE PLAFONNEE : 4,20% de 50% de la valeur venale, minimum 100 000 F CFA, vehicules de 0 a 10 ans.
- VOL : 0,14% de la valeur venale.
- INCENDIE : 0,15% de la valeur venale.

Les garanties non eligibles selon l'age du vehicule sont ignorees. Le prix final correspond a la somme des garanties retenues.

## Attestation PDF

L'attestation contient :

- le numero unique d'attestation ;
- les informations de souscription ;
- les informations de l'assure ;
- les informations du vehicule ;
- le produit souscrit ;
- la prime ;
- un QR code contenant les informations principales de l'attestation.

Le logo est charge depuis :

```properties
attestation.logo.path=classpath:static/images/logo-action-elles.png
```

## Tests

```bash
./gradlew test
```

Sur Windows :

```powershell
.\gradlew.bat test
```

Les tests couvrent notamment :

- le calcul de simulation ;
- l'exclusion des garanties non eligibles par age ;
- la visibilite des souscriptions et assures par role ;
- la gestion des utilisateurs ;
- la generation d'attestation PDF.

## Build

```bash
./gradlew bootWar
```

Artefact genere :

```text
build/libs/assurance-backend.war
```
