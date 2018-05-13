# Flexible Job Shop

## Authors
* Duc Hau NGUYEN
* Gael AYOUBA

## Getting started
### Eclipse configuration
* Download testing data from [here}(http://people.idsia.ch/~monaldo/fjsp.html)
* Once downloaded, unzip the files into a folder (named for ex. `data`).
* Drag `data` to the **Package Explorer**, into the **FlexibleJobShop**, at the same level with **src**, **JRE System Library**
* Right click to the project name **FlexibleJobShop** > New > Folder, type `conf` in Folder name
* Now your `conf` folder is at the same level with `data` and **src**, create a file with name `fjs.conf`
* You can see an example of configuration in the below section. For the dev reason, we ignore to put track fjs.conf in the project.

## Configuration
See documentation [here](src/jobshopflexible/README)

## PDF generate

Pdf generation in principle will generate a `.tex` file and then compile the file in pdf
See more documentation [here](src/output/pdflatex)

## TODO
* :poop: Repr�sentation de probl�me (graphe)
* :poop: Repr�sentation de solution
* Impl�mentation de l'algorithme:
	* M�ta heuristique de recherche local
	* M�thode � population de solution
* Test avec jeu de donnée
* Visualiser mesure de performance (temps d'exec, espace m�moire occup�e)

* une solution
* evaluation, stockage
* -> faire evoluer