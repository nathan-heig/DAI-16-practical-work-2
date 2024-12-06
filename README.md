# DAI-07-practical-work-1

### Programme de Chat par ROOMS.  
Le programme conçu lors de ce labo permet la discussion au travers de différentes ROOM.
Chaque utilisateur peut se créer un compte avec un nom et un mdp afin de pouvoir ce reconnecter par la suite.
Ensuite, chaque personne peut créer une ROOM, elle doit être initialisée avec son nom et un mdp, ceux-ci permettrons de s'y connecter.
Chaque utilisateur à donc la possibilité de se connecté à une ROOM dont il connait le nom et le mdp afin de pouvoir y écrire des messages.
Si un ou plusieurs autre(s) utilisateur(s) est/sont connecté(s) à la même ROOM, chacun pourra voir les messages écrits dans la ROOM.
Un historique des discussions de chaque ROOM est save afin de pouvoir réaccéder à une discussion.

### Comment utiliser le programme ?

#### Côté server :

| Exemples de commandes                                               | Utilité                              |
|---------------------------------------------------------------------|--------------------------------------|
| java -jar .\target\pratical_work_02-1.0-SNAPSHOT.jar server -p 2345 | ouverture du server sur le port 2345 |
| java -jar .\target\pratical_work_02-1.0-SNAPSHOT.jar server         | ouverture du server sur le port 1234 |

#### Côté client :

| Exemples de commandes                                                              | Utilité                                                            |
|------------------------------------------------------------------------------------|--------------------------------------------------------------------|
| java -jar .\target\pratical_work_02-1.0-SNAPSHOT.jar client -p 2345 -a 192.54.97.0 | connexion du client sur le port 2345 avec l'adresse IP 192.54.97.0 |
| java -jar .\target\pratical_work_02-1.0-SNAPSHOT.jar client                        | connexion du client sur le port 1234 avec l'adresse IP localhost   |

Le client peut ensuite lancer plusieurs commandes :

Première selection :

| Exemples de commandes | Utilité                                   |
|-----------------------|-------------------------------------------|
| 1                     | le client se login au server (nom et mdp) |
| 2                     | le client se register (nom et mdp)        |
| 3                     | le client se déconnecte                   |
  
Deuxième selection

| Exemples de commandes | Utilité                |
|-----------------------|------------------------|
| 1                     | connexion à une ROOM   |
| 2                     | création de ROOM       |
| 3                     | le client se déconnecte |

Troisième selection

| Exemples de commandes | Utilité                    |
|-----------------------|----------------------------|
| 1                     | le client écrit un message |
| 2                     | le client lit un message   |
| 3                     | le client quitte la ROOM   |

#### Utilisation avec Docker :

| Exemples de commandes                  | Utilité                            |
|----------------------------------------|------------------------------------|
| docker build -t my-app .               | création du docker                 |
| docker run -p 1234:1234  my-app server | possibilité de lancement du server |


### Comment clone et build le projet ?  
Pour ce faire, il suffit d'utiliser la commande
```text
git clone git@github.com:nathan-heig/DAI-16-practical-work-2.git 
```
et le projet sera cloner.

Pour build l'application, il faut utiliser la commande 
```text
./mvnw package.
``` 