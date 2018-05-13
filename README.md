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

## Solution Visualization

### Output.pdflatex
Pdf generation in principle will generate a `.tex` file and then compile the file in pdf
See more documentation [here](src/output/pdflatex)

### Output.swiftgantt
* Look into the **Project Explorer**
* Choose in the **lib** those files: `commons-collections-3.2.jar`, `commons-io-1.3.2.jar`, `commons-lang-2.2.jar`, `log4j-1.2.15.jar`, `swing-layout-1.0.3.jar`
* Right click on them > **Build Path** > **Add to Build Path** \\
This step is optional, do it when you have error:
* Right click in the **Referenced Libraries**, right click on one `.jar` file > **Build Path** > **Configure Build Path**
* Click to the tab **Order and export**, move all the `.jar` files up and let the project source at the final order.

## TODO
* :poop: Représentation de problème (graphe)
* :poop: Représentation de solution
* Implémentation de l'algorithme:
	* Méta heuristique de recherche local
	* Méthode à population de solution
* Test avec jeu de donnÃ©e
* Visualiser mesure de performance (temps d'exec, espace mémoire occupée)

* une solution
* evaluation, stockage
* -> faire evoluer