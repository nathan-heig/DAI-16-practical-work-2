# THE "ROOM MESSENGER" PROTOCOL

## Overview

Le "ROOM messenger" protocol est un protocol permettant une discussion entre un nombre indéfini de personnes au travers de ROOMS.

## Transport protocol

Ce protocol utilise TCP pour la communication.\
L'adresse IP connectée par défaut est "localhost", mais chaque client peut utiliser une autre adresse si il le souhaite.\
Pour ce qui est du port, le protocol se connecte par défaut au port 1234 mais comme pour l'adresse IP, le client peut choisir 
le port de connexion.\
Les messages sont encodés en UTF-8, permettant l'utilisation de symboles dans les messages.\
Le client se connecte au serveur grace à son pseudo et son mot de passe. Il a également la possibilité de créer ces derniers 
si c'est sa première connexion.\
Si lors d'une connexion le nom d'utilisateur n'existe pas ou que le mot de passe entrée est faux, une erreur sera envoyée au client.\
Pareil pour une première connexion, si le nom entrée existe déjà, une erreur sera envoyée au client.\
Il pourra ensuite se connecter à une ROOM ou en créer une, au choix.\
Si la ROOM n'existe pas ou que le mot de passe entré est faux, une erreur sera envoyée au client.\
Chaque ROOM est définie par un nom et un mot de passe permettant d'y accéder.\
Lorsqu'un client rejoins une ROOM, il peut alors soit envoyer des messages, soit demander de recevoir les messages ayant été écrits dans la ROOM 
ou encore un historique complet des discussions de cette ROOM. \
Le client peut quitter la ROOM quand il le souhaite.