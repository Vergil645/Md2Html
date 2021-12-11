### Converter from Markdown to HTML

A small training converter from Markdown markup to HTML markup on Java.  
It supports Markdown headers, fonts and links.

#### Usage

```
javac -d build src/*.java
java -cp build src.Md2Html <input filename> <output filename>
```