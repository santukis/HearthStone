# Android Clean Architecture

## Base project to use clean architecture on Android apps.

Project structure based on Martin's *Dependency Rule* and The *Dependency Inversion Principle*

![Clean Architecture Structure](/art/CleanArchitecture.jpg)

## Project Modules

* ui -> Is the application module. Uses Jetpack compose as render framework. Depends on all modules

* viewmodels -> Depends on **usecases** and **entities** module

* usecases -> Depends on **entities** module

* entities -> Independent from other modules

* repositories -> Depends on **usecases** and **entities** modules

* datasources -> Depends on **repositories** and **entities** modules


This modules can be also structured in three different layers:


* UI -> **ui** and **viewmodels** modules

* Domain -> **usecases** and **entities** modules

* Data -> **repositories** and **datasources** modules


UI and Data layer depends on Domain layer and Domain layer is independent.


An event originating from the UI (e.g. a user interaction with the app) that requires/modify some data
stored in local or remote data sources follows the next flow:

ui -> viewmodels -> usecases -> repositories -> datasources

datasources -> repositories -> usecases -> viewmodels -> ui

## References

Martin, R.C. 2009. **Clean Code. A Handbook of Agile Software Craftsmanship** Pearson

Martin, R.C. 2018. **Clean Architecture. A Craftsman's Guide to Software Structure and Design** Pearson
