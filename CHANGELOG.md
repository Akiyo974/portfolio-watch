# Changelog

Toutes les évolutions notables de l'app. Format inspiré de [Keep a Changelog](https://keepachangelog.com/fr/1.1.0/), versions selon [SemVer](https://semver.org/lang/fr/).

## [Non publié]

### Ajouté
- Version anglaise de l'app : tous les textes passent par les ressources Android (anglais par défaut, français dans `values-fr`), date affichée dans la langue de la montre.

### Modifié
- Les confirmations « ouvert sur le téléphone » et les échecs utilisent l'animation standard de Wear OS (`ConfirmationActivity`) au lieu des Toast.
- L'heure de l'accueil se met à jour à chaque minute (`ACTION_TIME_TICK`, réveil de l'écran, changement d'heure ou de fuseau) au lieu de chaque seconde, pour économiser la batterie.

### Corrigé
- Un glissement vers la droite pendant une partie ne ferme plus le jeu ; le bouton retour abandonne la partie au lieu de quitter l'écran.
- Le message d'échec n'affirme plus qu'aucun téléphone n'est connecté quand l'ouverture échoue pour une autre raison.

## [0.1.0] — 2026-10-25

Première version de l'app Wear OS.

### Ajouté
- Écran d'accueil façon cadran : heure et date en direct, graduation des minutes, texte courbe, complications « projets » et « km ».
- Menu numéroté (Projets, Parcours, Contact, Étoiles).
- Liste des 7 projets du portfolio et étude de cas résumée, avec ouverture de la page complète sur le téléphone.
- Parcours La Réunion → Saguenay (14 420 km) tracé le long de la lunette, et expériences récentes.
- Contact : e-mail, appel, LinkedIn et GitHub.
- Jeu des étoiles : 20 secondes, anneau de temps, vibration à chaque étoile, record enregistré sur la montre.
- Thème du portfolio web : palette encre/papier, polices Big Shoulders Display, Geist et Geist Mono.
- Intégration continue GitHub Actions : compilation, lint et APK téléchargeable.

### Corrigé
- Plantage au démarrage sur Wear OS 5 (Android 15) : passage à Wear Compose 1.4.1.
- L'heure en haut d'écran s'efface au défilement au lieu de chevaucher le contenu.
- Le message « Ouvert sur votre téléphone » n'apparaît plus quand aucun téléphone n'est connecté.

[0.1.0]: https://github.com/Akiyo974/portfolio-watch/releases/tag/v0.1.0
