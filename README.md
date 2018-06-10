# Flexible Job Shop

## Authors
* Duc Hau NGUYEN - Computer Engineering student at INSA Toulouse, promo 52
* Gael AYOUBA - Computer Engineering student at INSA Toulouse, promo 52

## Getting started

### Requirement
* Java JDK 1.8.x
* Eclipse (Oxygen for preference)

### Eclipse configuration
* Download testing data from [here](http://people.idsia.ch/~monaldo/fjsp.html)
* Once downloaded, unzip the files into a folder (named for ex. `data`).
* Drag `data` to the **Package Explorer**, into the **FlexibleJobShop**, at the same level with **src**, **JRE System Library**
* Right click to the project name **FlexibleJobShop** > New > Folder, type `conf` in Folder name
* Now your `conf` folder is at the same level with `data` and **src**, create a file with name `fjs.conf`
* You can see an example of configuration in the below section. For the dev reason, we ignore to put track fjs.conf in the project.

## Configuration
The project's solution is configured by a `.conf` file. We propose a `fjs.conf` file template with all the parameter. Each line, the parameter must set by the format `key=value`, the key of parameter are defined by developer. The following section explain the parameters.


### data.*
Parameter for initializing fjs problem.
```
data.path=data/Dauzere_Data/11a.fjs
data.verbose=false
```
* **data.path** specify the relative path to the dataset of problem. In the data folder, we placed some example of dataset from the idsia's proposal.
* **data.verbose** will display log process to console.

### is.*
Parameter for initializing glutton research.
```
is.verbose=false
```
* **is.verbose** will display log process to console.

### output.*
Parameter for visualizing the found solution.
```
output.directory=tmp
output.filename=is
output.pdflatex=C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\x64\\pdflatex
output.verbose=false
```
* **output.directory** the folder where we write down the visualisation of solution (in pdf file or png/jpg images, etc.)
* **output.filename** the output file name 
* **output.pdflatex** path to execute the pdflatex. In window this path must lead to the .exe file. In Linux the instruction is given in the section [Solution Visualisation](#solution-visualization).
* **output.verbose** will display log process to console.

### localsearch.*
Parameter for initializing the method of local search.
```
localsearch.verbose=false
```
* **localsearch.verbose** will display log process to console.


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

## Licence
No licence (or MIT???). Just give us a start for our hard work if you use or study something in our code.