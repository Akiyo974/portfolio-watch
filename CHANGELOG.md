# Changelog

Toutes les évolutions notables de l'app. Format inspiré de [Keep a Changelog](https://keepachangelog.com/fr/1.1.0/), versions selon [SemVer](https://semver.org/lang/fr/).

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
