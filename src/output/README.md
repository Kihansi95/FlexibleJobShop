# Flexible Job Shop

## Authors
* Duc Hau NGUYEN
* Gael AYOUBA

## Output

References : [tex.stackexchange.com, in solution proposed by Nicolas 56](https://tex.stackexchange.com/questions/41609/tex-rendering-in-a-java-application)

### Prerequisites
l. Install Latex in computer: [link](https://miktex.org/), select complete install
l. Install Ghostscript: [link](http://www.01net.com/telecharger/windows/Utilitaire/imprimantes/fiches/38621.html), add Ghostscript to PATH
l. Test `pdflatex` in console : `pdflatex -shell-escape ex.tex`, you can see **ex.pdf**
l. For **Window user**, copy and paste the pdflatex's full directory into the conf path. For example your *pdflatex.exe* found in *C:\Program Files\MiKTeX 2.9\miktex\bin\x64* then put *output.pdflatex=C:\Program Files\MiKTeX 2.9\miktex\bin\x64\pdflatex*. **Attention** maybe it works without this configuration.
l. Specify `verbose` in fjs.conf file for debug : 
```
output.verbose=true
```
l. Specify the directory where you wish to have the .tex file and .pdf file : 
```
output.directory=tmp
output.filename=is
```
