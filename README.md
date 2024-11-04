# DAI-07-practical-work-1

### Programme de cryptage.  
Le programme conçu lors de ce labo permet d'encrypter de de décrypter 
des fichiers texts à l'aide d'une clé, cette dernière pouvant également
être crée grâce à notre programme.

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