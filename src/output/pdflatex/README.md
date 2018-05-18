# Flexible Job Shop

## Authors
* Duc Hau NGUYEN
* Gael AYOUBA

## Output

References : [tex.stackexchange.com, in solution proposed by Nicolas 56](https://tex.stackexchange.com/questions/41609/tex-rendering-in-a-java-application)

### Prerequisites
* Install Latex in computer: [link](https://miktex.org/), select complete install
* Install Ghostscript: [link](http://www.01net.com/telecharger/windows/Utilitaire/imprimantes/fiches/38621.html), add Ghostscript to PATH
* Test `pdflatex` in console : `pdflatex -shell-escape ex.tex`, you can see **ex.pdf**
* For **Window users** Copy and paste the pdflatex's full directory into the conf path. For example your *pdflatex.exe* found in *C:\Program Files\MiKTeX 2.9\miktex\bin\x64* then put `output.pdflatex=C:\Program Files\MiKTeX 2.9\miktex\bin\x64\pdflatex`. **Attention** maybe it works without this configuration.
* For **Linux users** just make it `output.pdflatex=pdflatex`

### Linux users
At your execution (especially in INSA machine) you receive error with unknown `tikz.sty` (don't forget to turn on verbose). To be sure it works in any environment, do the following steps:
* Unzip the **pgf** package, for example it's in **~\pgf_3.x.x.tds**
* Check the location of your personal texmf tree : `tlmgr conf texmf TEXMFHOME`, usually the result will be `~/texmf`, create it if not exist.
* Create folder `mkdir -p ~/texmf/tex/generic/pgf`
* Access to the new one `cd ~/texmf/tex/generic/pgf`
* Move the the unzipped package into the tex tree `mv ~\pgf_3.x.x.tds .`


### Configuration parameters
* Specify `verbose` in fjs.conf file for debug : 
```
output.verbose=true
```
* Specify the directory where you wish to have the .tex file and .pdf file : 
```
output.directory=tmp
output.filename=is
```
