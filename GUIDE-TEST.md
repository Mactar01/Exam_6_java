# Guide de Test - Simulation WhatsApp

## Prérequis
- ✅ Java 21 installé (vérifié)
- ❌ Maven non installé (nous utiliserons une alternative)

## Étape 1 : Télécharger les dépendances
```bash
# Double-cliquer sur le fichier
download-deps.bat
```

## Étape 2 : Compiler le projet
```bash
# Créer le dossier de compilation
mkdir target\classes

# Compiler tous les fichiers Java
javac -cp "lib/*" -d target/classes src/main/java/essai/org/*.java src/main/java/essai/org/*/*.java src/main/java/essai/org/*/*/*.java
```

## Étape 3 : Copier les ressources
```bash
# Copier les fichiers de configuration
copy src\main\resources\*.* target\classes\
```

## Étape 4 : Test de la base de données
```bash
java -cp "target/classes;lib/*" essai.org.DatabaseTest
```

**Résultat attendu :**
```
✅ SessionFactory créée avec succès
✅ Connexion à la base de données réussie
✅ Tables créées et accessibles
🎉 Base de données configurée correctement !
```

## Étape 5 : Test du serveur
```bash
java -cp "target/classes;lib/*" essai.org.ServerTest
```

**Résultat attendu :**
```
✅ Serveur créé avec succès
✅ Base de données connectée
✅ Serveur prêt à accepter les connexions sur le port 8080
🎉 Serveur démarré avec succès !
```

## Étape 6 : Test complet

### Terminal 1 - Serveur
```bash
java -cp "target/classes;lib/*" essai.org.server.WhatsAppServer
```

### Terminal 2 - Client 1
```bash
java -cp "target/classes;lib/*" essai.org.WhatsAppClient
```

### Terminal 3 - Client 2 (optionnel)
```bash
java -cp "target/classes;lib/*" essai.org.WhatsAppClient
```

## Tests à effectuer

### 1. Connexion
- [ ] Saisir un pseudo unique
- [ ] Cliquer sur "Connexion"
- [ ] Vérifier que le statut devient "Connecté !"

### 2. Envoi de messages
- [ ] Saisir un message
- [ ] Cliquer sur "Envoyer"
- [ ] Vérifier que le message apparaît dans la liste

### 3. Communication entre clients
- [ ] Connecter un deuxième client
- [ ] Envoyer un message depuis le premier client
- [ ] Vérifier que le message apparaît chez le deuxième client

### 4. Test des mots interdits
- [ ] Envoyer un message contenant "JAVA NEKHOUL"
- [ ] Vérifier que l'utilisateur est banni
- [ ] Vérifier la notification de bannissement

### 5. Test de la limite de membres
- [ ] Connecter 7 clients
- [ ] Essayer de connecter un 8ème client
- [ ] Vérifier le message d'erreur "Le groupe est plein"

## Dépannage

### Erreur de connexion MySQL
- Vérifier que MySQL est démarré
- Vérifier les paramètres dans `hibernate.cfg.xml`
- Vérifier que la base `whatsapp_db` existe

### Erreur de compilation
- Vérifier que tous les fichiers .jar sont dans le dossier `lib/`
- Vérifier que Java 21 est utilisé

### Erreur JavaFX
- Vérifier que tous les modules JavaFX sont téléchargés
- Vérifier que le module path est correct

## Scripts automatiques

### Test rapide (Windows)
```bash
test-simple.bat
```

### Démarrage serveur
```bash
start.bat
```

### Démarrage client
```bash
start-client.bat 