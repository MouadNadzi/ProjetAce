
# Documentation du Projet : Générateur de Code basé sur des Patrons de Conception

video demo
[demo](https://drive.google.com/file/d/1FhL6FAU83BWxZfVwChmhj1DD8LZP7eFf/view?usp=sharing)
https://drive.google.com/file/d/1FhL6FAU83BWxZfVwChmhj1DD8LZP7eFf/view?usp=sharing
## Table des matières

1. Introduction
   - Contexte et objectifs
   - Vue d'ensemble du projet

2. Architecture Technique
   - Structure du projet
   - Technologies utilisées
   - Conteneurisation et déploiement

3. Fonctionnalités
   - Génération de code
   - Patrons de conception implémentés
   - Interface utilisateur

4. Guide d'installation
   - Prérequis
   - Installation et configuration
   - Déploiement

5. Guide d'utilisation
   - Interface utilisateur
   - Génération de code
   - Téléchargement des projets

6. Test Unitaire 
7. Sonarqube

Je vais commencer par l'introduction et continuer section par section. Voici la première partie :

## 1. Introduction

### 1.1 Contexte et objectifs

Le projet de Générateur de Code basé sur des Patrons de Conception est une solution innovante conçue pour automatiser et simplifier le développement d'applications mobiles. Dans un contexte où la demande en applications mobiles est en constante augmentation, l'implémentation correcte des patrons de conception devient cruciale pour maintenir la qualité et la maintenabilité du code.

**Objectifs principaux :**
- Automatiser la génération de code suivant les patrons de conception standards
- Réduire le temps de développement initial des applications mobiles
- Assurer une structure de code cohérente et maintenable
- Faciliter l'adoption des bonnes pratiques de développement

### 1.2 Vue d'ensemble du projet

Le projet se compose de deux parties principales :

1. **Backend (Spring Boot)**
   - API RESTful pour la génération de code
   - Gestion des templates de code
   - Implémentation des patrons de conception
   - Gestion des fichiers et de la structure du projet

2. **Frontend (Next.js)**
   - Interface utilisateur intuitive
   - Visualisation du code généré
   - Gestion des paramètres de génération
   - Prévisualisation de la structure du projet

## 2. Architecture Technique

### 2.1 Architecture Globale

Le projet est construit selon une architecture moderne et modulaire, comprenant trois composants principaux :

1. **Backend (Spring Boot)**
   - Service de génération de code
   - API RESTful
   - Gestion des templates

2. **Frontend Web (Next.js)**
   - Interface utilisateur web
   - Visualisation de code
   - Gestion des projets

3. **Frontend Mobile (Flutter)**
   - Application mobile native
   - Interface adaptative
   - Fonctionnalités synchronisées avec la version web

### 2.2 Structure Détaillée du Projet

```plaintext
code-generator/
├── backend/                 # Service Spring Boot
├── frontend-web/           # Application Next.js
├── frontend-mobile/        # Application Flutter
└── docker/                 # Configuration Docker
```

### 2.3 Technologies Utilisées

#### Backend (Spring Boot)
- **Langage** : Java 17
- **Framework** : Spring Boot 2.7
- **Base de données** : H2 (en mémoire)
- **Outils de build** : Maven
- **Dépendances principales** :
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - Commons IO

#### Frontend Web (Next.js)
- **Langage** : JavaScript/React
- **Framework** : Next.js 13
- **UI Components** : shadcn/ui
- **Styling** : Tailwind CSS
- **Dépendances clés** :
  - JSZip
  - Lucide Icons
  - Prism React Renderer

#### Frontend Mobile (Flutter)
- **Langage** : Dart
- **Framework** : Flutter
- **Architecture** : Clean Architecture
- **État** : Provider/Bloc
- **Composants principaux** :
  - Material Design 3
  - Code Viewer
  - File Explorer
  - Custom Widgets

### 2.4 Patterns de Conception Implémentés

#### Patterns Supportés pour Android
1. **MVC (Model-View-Controller)**
   - Séparation claire des responsabilités
   - Facilité de maintenance
   - Structure traditionnelle Android

2. **MVVM (Model-View-ViewModel)**
   - Liaison de données bidirectionnelle
   - Meilleure testabilité
   - Support des composants d'architecture Android

#### Patterns Supportés pour iOS
1. **MVC (Model-View-Controller)**
   - Pattern natif iOS
   - Integration UIKit
   - Structure Apple standard

2. **MVVM (Model-View-ViewModel)**
   - Reactive Programming
   - Combine Framework
   - SwiftUI Support

### 2.5 Conteneurisation

#### Configuration Docker
```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    
  frontend-web:
    build: ./frontend-web
    ports:
      - "3000:3000"

  frontend-mobile:
    build: ./frontend-mobile
    ports:
      - "8081:8081"
```

### 2.6 Communication entre les Services

1. **API RESTful**
   - Endpoints standardisés
   - Format JSON
   - Gestion des erreurs HTTP

2. **Sécurité**
   - CORS Configuration
   - Rate Limiting
   - Validation des entrées

3. **Synchronisation**
   - WebSocket pour les mises à jour en temps réel
   - État partagé entre web et mobile
   - Cache cohérent

## 3. Fonctionnalités

### 3.1 Génération de Code

#### 3.1.1 Processus de Génération
1. **Configuration Initiale**
   - Sélection de la plateforme (Android/iOS)
   - Choix du pattern architectural (MVC/MVVM)
   - Définition du nom du projet
   - Spécification du package name

2. **Étapes de Génération**
```plaintext
Configuration → Validation → Génération → Structure → Archive
```

3. **Validation et Contrôles**
   - Vérification du nom du projet
   - Validation du format du package
   - Contrôle de compatibilité pattern/plateforme

### 3.2 Interface Web

#### 3.2.1 Composants Principaux
1. **Configuration Panel**
   - Sélecteurs de plateforme et pattern
   - Champs de saisie pour le projet
   - Boutons d'action

2. **Preview Panel**
   - Visualisation du code en temps réel
   - Coloration syntaxique
   - Navigation dans les fichiers

3. **Structure Panel**
   - Arborescence du projet
   - Liste des dépendances
   - Informations sur la configuration

### 3.3 Interface Mobile (Flutter)

#### 3.3.1 Écrans Principaux
1. **Écran d'Accueil**
   ```dart
   class HomeScreen extends StatelessWidget {
     @override
     Widget build(BuildContext context) {
       return Scaffold(
         appBar: AppBar(title: Text('Code Generator')),
         body: ProjectList(),
         floatingActionButton: AddProjectButton(),
       );
     }
   }
   ```

2. **Configuration du Projet**
   - Interface intuitive pour mobile
   - Formulaires adaptatifs
   - Validation en temps réel

3. **Visualisation du Code**
   - Mode sombre/clair
   - Zoom et navigation tactile
   - Partage de code

### 3.4 Fonctionnalités Avancées

#### 3.4.1 Gestion des Templates
1. **Templates Android**
   - Structure MVC
     ```java
     // Model
     public class ${projectName}Model {
         private String data;
         // Getters/Setters
     }
     
     // Controller
     public class ${projectName}Controller {
         private ${projectName}Model model;
         // Methods
     }
     ```
   - Structure MVVM avec ViewModel et LiveData

2. **Templates iOS**
   - Intégration UIKit/SwiftUI
   - Support Combine pour MVVM
   - Gestion des états

#### 3.4.2 Personnalisation
1. **Options de Configuration**
   - Style de code
   - Préférences de formatage
   - Choix des dépendances

2. **Extensions**
   - Support de nouveaux patterns
   - Templates personnalisés
   - Plugins additionnels

### 3.5 Fonctionnalités Communes Web/Mobile

1. **Gestion des Projets**
   - Création
   - Modification
   - Suppression
   - Historique

2. **Export et Partage**
   - ZIP des projets
   - Partage de code
   - QR Code pour mobile

3. **Synchronisation**
   - État en temps réel
   - Sauvegarde cloud
   - Multi-dispositifs

## 4. Guide d'Installation et Configuration

### 4.1 Prérequis

#### 4.1.1 Environnement de Développement
- Java JDK 17 ou supérieur
- Node.js 16.x ou supérieur
- Flutter SDK 3.x
- Docker et Docker Compose
- Git

#### 4.1.2 Configuration Système Recommandée
```plaintext
- CPU : 4 cœurs ou plus
- RAM : 8 Go minimum
- Espace disque : 10 Go minimum
- OS : Linux, macOS, ou Windows 10/11
```

### 4.2 Installation

#### 4.2.1 Installation Backend (Spring Boot)
```bash
# Cloner le repository
git clone https://github.com/username/code-generator.git

# Accéder au dossier backend
cd code-generator/backend

# Compiler le projet
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

#### 4.2.2 Installation Frontend Web (Next.js)
```bash
# Accéder au dossier frontend-web
cd code-generator/frontend-web

# Installer les dépendances
npm install

# Lancer en développement
npm run dev

# Build pour production
npm run build
```

#### 4.2.3 Installation Frontend Mobile (Flutter)
```bash
# Accéder au dossier frontend-mobile
cd code-generator/frontend-mobile

# Obtenir les dépendances
flutter pub get

# Lancer l'application
flutter run
```

### 4.3 Configuration Docker

#### 4.3.1 Build des Images
```bash
# Construction de toutes les images
docker-compose build

# Lancement des services
docker-compose up -d
```

#### 4.3.2 Variables d'Environnement
```env
# Backend
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
DB_HOST=localhost
DB_PORT=5432

# Frontend Web
NEXT_PUBLIC_API_URL=http://localhost:8080
NODE_ENV=production

# Frontend Mobile
API_BASE_URL=http://localhost:8080
```

### 4.4 Configuration des Services

#### 4.4.1 Configuration Backend
```properties
# application.properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.servlet.multipart.max-file-size=10MB
```

#### 4.4.2 Configuration Frontend Web
```javascript
// next.config.js
module.exports = {
  reactStrictMode: true,
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:8080/api/:path*',
      },
    ]
  }
}
```

#### 4.4.3 Configuration Flutter
```dart
// lib/config/app_config.dart
class AppConfig {
  static const String apiUrl = String.fromEnvironment(
    'API_URL',
    defaultValue: 'http://localhost:8080',
  );
}
```

### 4.5 Déploiement

#### 4.5.1 Déploiement Production
1. **Backend**
   ```bash
   # Build du JAR
   mvn clean package -DskipTests
   
   # Déploiement du JAR
   java -jar target/code-generator.jar
   ```

2. **Frontend Web**
   ```bash
   # Build production
   npm run build
   
   # Démarrage du serveur
   npm start
   ```

3. **Application Mobile**
   ```bash
   # Build APK
   flutter build apk --release
   
   # Build IPA
   flutter build ios --release
   ```

#### 4.5.2 Surveillance et Maintenance
- Monitoring des services
- Logs centralisés
- Sauvegardes régulières
- Mises à jour de sécurité

