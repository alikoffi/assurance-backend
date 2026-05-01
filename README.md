# Action'Elles Assurance - Backend

Backend Spring Boot de l'application Action'Elles Assurance. Il permet de gérer la simulation, la souscription et l'édition d'attestations pour des produits d'assurance automobile NSIAGO'ASSUR.

## Stack

- Java 17
- Spring Boot 3.2
- Spring Security avec JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- iText 7 pour les attestations PDF
- Swagger/OpenAPI via Springdoc

## Fonctionnalités

- Authentification JWT.
- Gestion des rôles `ADMIN` et `AMAZONE`.
- Simulation de prime automobile.
- Consultation et modification des simulations non souscrites.
- Souscription à partir d'un devis valide.
- Génération d'une attestation PDF avec logo et QR code.
- Gestion des utilisateurs par l'administrateur.
- Visibilité métier : une amazone voit uniquement ses simulations, souscriptions et assurés ; l'administrateur voit tout.

## Prérequis

- JDK 17
- PostgreSQL
- Gradle wrapper fourni dans `backend/`
- Docker et Docker Compose pour le lancement conteneurisé

## Base De Données

Créer une base PostgreSQL locale :

```sql
CREATE DATABASE assurance;
CREATE USER assurance WITH PASSWORD 'assurance';
GRANT ALL PRIVILEGES ON DATABASE assurance TO assurance;
```

Configuration par défaut dans `backend/src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/assurance
spring.datasource.username=assurance
spring.datasource.password=assurance
server.port=8075
```

Flyway crée automatiquement le schéma et insère les données de référence au démarrage. Les migrations sont séparées par usage :

- `backend/src/main/resources/flyway/schema` : création du schéma.
- `backend/src/main/resources/flyway/dev` : données de développement et de référence.

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

## Lancement Avec Docker

Depuis la racine du projet :

```bash
docker compose up --build
```

Cette commande lance :

- PostgreSQL sur le port `5432` ;
- le backend Spring Boot sur le port `8075`.

L'API reste disponible sur :

```text
http://localhost:8075/api/v1
```

Pour arrêter les conteneurs :

```bash
docker compose down
```

Pour arrêter les conteneurs et supprimer les données PostgreSQL locales :

```bash
docker compose down -v
```

## Comptes De Test

Les mots de passe initiaux des comptes insérés par Flyway sont identiques.

| Login | Rôle | Statut |
| --- | --- | --- |
| admin | ADMIN | ACTIF |
| amazone1 | AMAZONE | ACTIF |
| amazone2 | AMAZONE | ACTIF |

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

Exemple de création :

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

La réponse contient notamment :

- `quoteReference` au format `QT` + 12 caractères.
- `endDate`, avec une validité de 2 semaines.
- `price`.
- le détail des garanties retenues.
- l'utilisateur associé à la simulation.
- l'indication `modifiable` selon l'état de souscription.

### Souscriptions

```http
POST /api/v1/subscriptions
GET  /api/v1/subscriptions
GET  /api/v1/subscriptions/{id}
GET  /api/v1/subscriptions/status/{id}
GET  /api/v1/subscriptions/{id}/attestation
```

### Assurés

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

## Règles Métier

### Produits

| Produit | Garanties | Catégories |
| --- | --- | --- |
| Papillon | RC, DOMMAGES, VOL | 201 |
| Douby | RC, DOMMAGES, TIERCE COLLISION | 202 |
| Douyou | RC, DOMMAGES, TIERCE COLLISION, INCENDIE | 201, 202 |
| Toutourisquou | Toutes garanties | 201 |

### Garanties

- RC : tarif selon la puissance fiscale.
- DOMMAGES : 2,60 % de la valeur neuve, véhicules de 0 à 5 ans.
- TIERCE COLLISION : 1,65 % de la valeur neuve, véhicules de 0 à 8 ans.
- TIERCE PLAFONNÉE : 4,20 % de 50 % de la valeur vénale, minimum 100 000 F CFA, véhicules de 0 à 10 ans.
- VOL : 0,14 % de la valeur vénale.
- INCENDIE : 0,15 % de la valeur vénale.

Les garanties non éligibles selon l'âge du véhicule sont ignorées. Le prix final correspond à la somme des garanties retenues.

## Attestation PDF

L'attestation contient :

- le numéro unique d'attestation ;
- les informations de souscription ;
- les informations de l'assuré ;
- les informations du véhicule ;
- le produit souscrit ;
- la prime ;
- un QR code contenant les informations principales de l'attestation.

Le logo est chargé depuis :

```properties
attestation.logo.path=classpath:static/images/logo-action-elles.png
```

## Tests

Depuis le dossier `backend` :

```bash
./gradlew test
```

Sur Windows :

```powershell
.\gradlew.bat test
```

Les tests couvrent notamment :

- le calcul de simulation ;
- l'exclusion des garanties non éligibles par âge ;
- la visibilité des simulations, souscriptions et assurés par rôle ;
- la gestion des utilisateurs ;
- la génération d'attestation PDF.

## Build

Depuis le dossier `backend` :

```bash
./gradlew bootWar
```

Artefact généré :

```text
backend/build/libs/assurance-backend.war
```
