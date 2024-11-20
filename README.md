This repository provides the ability to transform the data provided by the [qual-o-mat-data](https://github.com/gockelhahn/qual-o-mat-data) repository into scaled contexts for use with the [conexp-clj](https://github.com/tomhanika/conexp-clj) tool.


The [qual-o-mat-data](https://github.com/gockelhahn/qual-o-mat-data) repository contains archived data on the questions from the [Wahl-O-Mat](https://www.wahl-o-mat.de/) election quiz.

[conexp-clj](https://github.com/tomhanika/conexp-clj) provides a variety of algorithms and methods for [Formal Concept Analysis](http://www.upriss.org.uk/fca/fca.html).
The fundamental data structure underlying `Formal Concept Analysis` is the [formal context](https://upriss.github.io/fca/fcaintro.html). This repository provides the means to transform the set of quiz questions and answers of each election in the `qual-o-mat-data` dataset into such a formal context.

To make use of this repository, you need to execute `setup.sh` by running:

```
bash setup.sh
```
This will download the `qual-o-mat-data` repository, and create all required files.
To create the context files from the dataset, run:

```
bash update.sh
```

Since the `Wahl-O-Mat` permits "agree", "disagree" and "neutral" ()as answers, these contexts need to be created from `many valued contexts` through [scaling](https://github.com/tomhanika/conexp-clj/blob/master/doc/Scaling-Many-Valued-Contexts.org).

The two scalings offered by this repository are the `nominal scale`, and an `ordinal scale`:

Nominal Scale:
```
                  |Stimme zu   Stimme nicht zu  Neutral 
------------------+-------------------------------------
Stimme zu         |x                .              .   
Stimme nicht zu   |.                x              .   
Neutral           |.                .              x  
```

Ordinal Scale:
```
                  |Stimme zu   Stimme nicht zu  Neutral 
------------------+-------------------------------------
Stimme zu         |x                x              x   
Stimme nicht zu   |.                x              .   
Neutral           |.                x              x  
```

The ordinal scale can be interpreted as a ranking of "disagree -> neutral -> agree".

The script saves the contexts in the [Burmeister format](https://upriss.github.io/fca/fcafileformats.html#Burmeister).
`conexp-clj` can then be used to perform further operations on them.



This project was created for the [Knowledge & Data Engineering Group (KDE)](https://www.kde.cs.uni-kassel.de/en/), University of Kassel.