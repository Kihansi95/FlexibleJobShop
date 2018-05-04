# Flexible Job

## Getting started
### Eclipse configuration
* Download testing data from [here}(http://people.idsia.ch/~monaldo/fjsp.html)
* Once downloaded, unzip the files into a folder (named for ex. `data`).
* Drag `data` to the **Package Explorer**, into the **FlexibleJobShop**, at the same level with **src**, **JRE System Library**
* Right click to the project name **FlexibleJobShop** > New > Folder, type `conf` in Folder name
* Now your `conf` folder is at the same level with `data` and **src**, create a file with name `fjs.conf`
* You can see an example of configuration in the below section. For the dev reason, we ignore to put track fjs.conf in the project.

## Configuration
```
mode=DEBUG
dataPath=data/mt10c1.fjs
```

## TODO
* :poop: Représentation de problème (graphe)
* :poop: Représentation de solution
* Implémentation de l'algorithme:
	* Méta heuristique de recherche local
	* Méthode à population de solution
* Test avec jeu de donnée
* Visualiser mesure de performance (temps d'exec, espace mémoire occupée)

* une solution
* evaluation, stockage
* -> faire evoluer

## Authors
* Duc Hau NGUYEN
* Gael AYOUBA