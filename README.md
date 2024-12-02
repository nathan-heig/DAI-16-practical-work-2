# DAI-07-practical-work-1

### Programme de Chat par ROOMS.  
Le programme conçu lors de ce labo permet la discussion au travers de différentes ROOM.
Chaque utilisateur peut se créer un compte avec un nom et un mdp afin de pouvoir ce reconnecter par la suite.
Ensuite, chaque personne peut créer une ROOM, elle doit être initialisée avec son nom et un mdp, ceux-ci permettrons de s'y connecter.
Chaque utilisateur à donc la possibilité de se connecté à une ROOM dont il connait le nom et le mdp afin de pouvoir y écrire des messages.
Si un ou plusieurs autre(s) utilisateur(s) est/sont connecté(s) à la même ROOM, chacun pourra voir les messages écrits dans la ROOM.
Un historique des discussions de chaque ROOM est save afin de pouvoir réaccéder à une discussion.

### Comment utiliser le programme ?  
Différentes commandes sont disponibles, voici un exemple ainsi que le résultat obtenu.  
Nous partons du principe que nous avons un fichier test.txt et que c'est ce dernier qui 
doit être encrypté. Nous allons utiliser l'algorithme AES pour ce faire.

| Exemple de commandes                                                                  | Utilité               | Fichier crée     | Contenu du fichier  |
|---------------------------------------------------------------------------------------|-----------------------|------------------|---------------------|
| java -jar .\target\pratical_work_01-1.0-SNAPSHOT.jar -a AES test.key createKey -l 128 | Création d'une clé    | test.key         | o+U9NaN8zUEajdfuidyp/Q==  |
| java -jar .\target\pratical_work_01-1.0-SNAPSHOT.jar -a AES test.txt encrypt -k test.key  | Encryptage du fichier | test.txt.enc     | ��N�_�}�#[�с��نj ��'�˘=�Ь  |
| java -jar .\target\pratical_work_01-1.0-SNAPSHOT.jar -a AES test.txt.enc decrypt -k test.key| Décryptage du fichier | test.txt.enc.dec | ceci est un test.  |
  
### Comment clone et build le projet ?  
Pour ce faire, il suffit d'utiliser la commande git clone git@github.com:BKo1706/DAI-07-practical-work-1.git et le projet sera cloner.  
Pour build l'application, il faut utiliser la commande ./mvnw package.