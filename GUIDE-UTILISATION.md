# Guide d'utilisation - Simulation WhatsApp

## Prérequis
- Java 21 installé
- MySQL installé et démarré
- Base de données `whatsapp_db` créée

## Démarrage rapide

### **Étape 1 : Démarrer le serveur**
1. **Double-cliquez** sur le fichier `start-server.bat`
2. Une fenêtre de commande va s'ouvrir
3. Vous devriez voir :
   ```
   Démarrage du serveur WhatsApp...
   Serveur créé avec succès
   Le serveur va écouter sur le port 8080
   ```
4. **Laissez cette fenêtre ouverte** (ne la fermez pas !)

### **Étape 2 : Démarrer le client**

#### **Option A : Client JavaFX (Interface graphique moderne)**
1. **Double-cliquez** sur le fichier `start-client.bat`
2. L'interface JavaFX moderne va s'ouvrir avec :
   - **Design inspiré de WhatsApp** avec couleurs vertes
   - **Animations fluides** et effets visuels
   - **Messages stylisés** selon le type (utilisateur, système, etc.)
   - **Compteur de membres** en temps réel
   - **Notifications visuelles** pour les nouveaux messages
   - **Interface responsive** qui s'adapte à la taille de la fenêtre

#### **Option B : Client Console (Interface texte)**
1. **Double-cliquez** sur le fichier `start-client-console.bat`
2. Une fenêtre de commande va s'ouvrir
3. Suivez les instructions à l'écran

### **Étape 3 : Se connecter**
1. **Tapez un pseudo** dans le champ (ex: "Alice", "Bob", etc.)
2. **Cliquez** sur le bouton "🔗 Se connecter" (JavaFX) ou **appuyez sur Entrée** (Console)
3. Vous devriez voir "Connecté au serveur" dans la zone de chat

### **Étape 4 : Envoyer des messages**
1. **Tapez votre message** dans le champ de saisie
2. **Cliquez** sur "📤 Envoyer" ou **appuyez sur Entrée**
3. Votre message apparaît dans la zone de chat avec votre pseudo

## Utilisation

### Connexion
1. Entrez un pseudo unique (max 7 membres dans le groupe)
2. Cliquez sur "Se connecter"
3. Vous devriez voir "Connecté au serveur" dans la zone de chat

### Envoi de messages
1. Tapez votre message dans le champ de saisie
2. Cliquez sur "Envoyer" ou appuyez sur Entrée
3. Le message apparaît dans la zone de chat avec votre pseudo

### Déconnexion
- **JavaFX** : Cliquez sur "❌ Se déconnecter"
- **Console** : Tapez `quit` ou `exit`

## Fonctionnalités

### Limitation du groupe
- Maximum 7 membres simultanés
- Pseudo unique obligatoire

### Filtrage des mots interdits
- Certains mots sont automatiquement filtrés
- Les utilisateurs utilisant des mots interdits peuvent être bannis

### Sauvegarde
- Tous les messages sont sauvegardés en base de données
- L'historique est conservé entre les sessions

### Interface moderne (JavaFX)
- **Design WhatsApp** : Couleurs et style inspirés de WhatsApp
- **Animations** : Effets de fade-in et slide-in pour les messages
- **Messages stylisés** :
  - 🟢 Messages utilisateur (vous) : Alignés à droite, fond vert
  - ⚪ Messages autres : Alignés à gauche, fond gris
  - 🟠 Messages système : Centrés, fond orange
  - 🔵 Connexions : Fond bleu
  - 🔴 Déconnexions/Bannissements : Fond rouge
- **Compteur de membres** : Affiche le nombre de membres connectés
- **Notifications visuelles** : Clignotement lors de nouveaux messages
- **Auto-scroll** : Défilement automatique vers les nouveaux messages
- **Responsive** : Interface qui s'adapte à la taille de la fenêtre

## Dépannage

### Erreur "Module javafx.controls not found"
- ✅ **Résolu** : Les modules JavaFX ont été téléchargés et installés
- Si l'erreur persiste, utilisez le client console (`start-client-console.bat`)

### Erreur "Error initializing QuantumRenderer"
- **Solution** : Utilisez le client console (`start-client-console.bat`)
- Ou ajoutez `-Dprism.order=sw` dans le script JavaFX

### Erreur de connexion au serveur
- Vérifiez que le serveur est démarré
- Vérifiez que le port 8080 est libre

### Erreur de base de données
- Vérifiez que MySQL est démarré
- Vérifiez que la base `whatsapp_db` existe
- Vérifiez les paramètres de connexion dans `hibernate.cfg.xml`

## Structure du projet
```
essai/
├── src/main/java/essai/org/
│   ├── controller/     # Contrôleurs JavaFX
│   ├── dao/           # Accès aux données
│   ├── model/         # Entités JPA
│   ├── network/       # Communication réseau
│   └── server/        # Serveur et gestion des clients
├── src/main/resources/
│   ├── view/          # Fichiers FXML et CSS
│   │   ├── ClientView.fxml    # Interface utilisateur
│   │   └── styles.css         # Styles modernes
│   └── hibernate.cfg.xml
├── lib/               # Bibliothèques JAR (incluant JavaFX)
├── start-server.bat           # Script de démarrage serveur
├── start-client.bat           # Script de démarrage client JavaFX
├── start-client-console.bat   # Script de démarrage client console
├── start-multiple-clients.bat # Script pour plusieurs clients
├── test-new-interface.bat     # Test de la nouvelle interface
└── test-client.bat            # Script de test du client
```

## Fichiers de démarrage

### `start-server.bat`
- Démarre le serveur WhatsApp
- Se connecte à la base de données
- Écoute sur le port 8080

### `start-client.bat`
- Démarre le client JavaFX moderne
- Interface graphique avec design WhatsApp
- Animations et effets visuels

### `start-client-console.bat`
- Démarre le client console
- Interface texte simple
- Alternative si JavaFX ne fonctionne pas

### `start-multiple-clients.bat`
- Démarre automatiquement 3 clients
- 2 clients JavaFX + 1 client console
- Pour tester le chat de groupe

### `test-new-interface.bat`
- Test de la nouvelle interface améliorée
- Affiche les nouvelles fonctionnalités

### `test-client.bat`
- Test de compilation et lancement
- Diagnostic des problèmes
- Vérification des modules JavaFX

## Recommandations

### Pour un usage quotidien
- **Client JavaFX** : Interface plus agréable et moderne avec animations
- **Client Console** : Plus rapide et stable

### Pour le développement/test
- **Client Console** : Plus facile pour déboguer
- **Client JavaFX** : Pour tester l'interface utilisateur
- **Multiple clients** : Pour tester les interactions de groupe

## Nouvelles fonctionnalités (v2.0)

### Interface moderne
- 🎨 Design inspiré de WhatsApp avec couleurs vertes
- ✨ Animations fluides pour les messages et boutons
- 📱 Interface responsive qui s'adapte à la taille
- 🎯 Focus automatique sur le champ pseudo

### Messages améliorés
- 🟢 Messages utilisateur alignés à droite (fond vert)
- ⚪ Messages autres alignés à gauche (fond gris)
- 🟠 Messages système centrés (fond orange)
- 🔵 Notifications de connexion (fond bleu)
- 🔴 Notifications de déconnexion/bannissement (fond rouge)

### Fonctionnalités avancées
- 👥 Compteur de membres en temps réel
- 🔔 Notifications visuelles pour nouveaux messages
- 📜 Auto-scroll vers les nouveaux messages
- ⌨️ Envoi rapide avec la touche Entrée
- 🎭 Animations de connexion/déconnexion 