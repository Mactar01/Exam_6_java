# Simulation WhatsApp - Projet Java L3IAGE

## Description
Application de simulation d'un groupe WhatsApp avec les fonctionnalités suivantes :
- Communication en temps réel via sockets TCP
- Interface graphique JavaFX
- Base de données MySQL avec Hibernate
- Limitation à 7 membres maximum
- Filtrage des mots interdits avec bannissement automatique
- Historique des messages

## Technologies utilisées
- **Java 21**
- **JavaFX** - Interface graphique
- **Hibernate/JPA** - Persistance des données
- **MySQL** - Base de données
- **Lombok** - Réduction du code boilerplate
- **Jackson** - Sérialisation JSON
- **Maven** - Gestion des dépendances

## Prérequis
1. **Java 21** installé
2. **MySQL** installé et configuré
3. **Maven** installé

## Configuration de la base de données
1. Utiliser votre base de données MySQL existante `whatsapp_db`
2. Modifier les paramètres de connexion dans `src/main/resources/hibernate.cfg.xml` :
   ```xml
   <property name="hibernate.connection.username">votre_utilisateur</property>
   <property name="hibernate.connection.password">votre_mot_de_passe</property>
   ```

## Installation et compilation
```bash
# Cloner le projet
git clone <url-du-projet>

# Aller dans le répertoire du projet
cd essai

# Compiler le projet
mvn clean compile

# Créer le package JAR
mvn package
```

## Test de la configuration

### Test de la base de données
```bash
mvn exec:java -Dexec.mainClass="essai.org.DatabaseTest"
```

### Test du serveur
```bash
mvn exec:java -Dexec.mainClass="essai.org.ServerTest"
```

### Démarrage rapide (Windows)
1. Double-cliquer sur `start.bat` pour démarrer le serveur
2. Double-cliquer sur `start-client.bat` pour démarrer le client

## Lancement de l'application

### 1. Démarrer le serveur
```bash
# Depuis le répertoire du projet
mvn exec:java -Dexec.mainClass="essai.org.server.WhatsAppServer"
```

### 2. Démarrer le client
```bash
# Dans un autre terminal
mvn exec:java -Dexec.mainClass="essai.org.WhatsAppClient"
```

Ou utiliser le JAR généré :
```bash
java -jar target/whatsapp-simulation-1.0-SNAPSHOT.jar
```

## Utilisation

### Connexion
1. Saisir un pseudo unique
2. Cliquer sur "Connexion"
3. L'application se connecte au serveur et charge l'historique des messages

### Envoi de messages
1. Saisir le message dans le champ de texte
2. Cliquer sur "Envoyer" ou appuyer sur Entrée
3. Le message est diffusé à tous les autres membres

### Déconnexion
- Cliquer sur "Déconnexion" pour quitter proprement le groupe

## Fonctionnalités

### Limitation des membres
- Maximum 7 membres simultanés
- Message d'erreur si le groupe est plein

### Filtrage des mots interdits
Les mots suivants sont interdits et entraînent un bannissement automatique :
- GENOCID
- TERRORISM
- ATTACK
- CHELSEA
- JAVA NEKHOUL

### Historique des messages
- Chargement automatique des 15 derniers messages à la connexion
- Persistance en base de données

### Notifications
- Messages formatés avec horodatage
- Notifications visuelles pour les nouveaux messages
- Emojis pour les événements (rejoindre/quitter/bannissement)

## Structure du projet
```
src/
├── main/
│   ├── java/essai/org/
│   │   ├── controller/     # Contrôleurs JavaFX
│   │   ├── dao/           # Accès aux données
│   │   ├── model/         # Entités JPA et modèles
│   │   ├── network/       # Communication réseau
│   │   ├── server/        # Serveur et gestion des clients
│   │   ├── Main.java      # Point d'entrée serveur
│   │   └── WhatsAppClient.java # Point d'entrée client
│   └── resources/
│       ├── view/          # Fichiers FXML
│       ├── sounds/        # Fichiers audio
│       └── hibernate.cfg.xml # Configuration Hibernate
```

## Développement

### Ajout de nouvelles fonctionnalités
1. Modifier les modèles dans `model/`
2. Mettre à jour les DAO si nécessaire
3. Ajouter la logique réseau dans `network/`
4. Mettre à jour l'interface dans `view/` et `controller/`

### Tests
Pour tester l'application :
1. Démarrer le serveur
2. Lancer plusieurs instances du client
3. Tester la connexion/déconnexion
4. Tester l'envoi de messages
5. Tester le filtrage des mots interdits

## Auteur
M GAYE Abdoulaye - ISI Java L3IAGE & L3GDA
Date de soutenance : 09/07/2025 