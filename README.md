# TablutCompetition
Software for the Tablut Students Competition

## Installazione su Ubuntu/Debian 

Queste sono le istruzioni per installare le librerie necessarie su ambiente
ubuntu/debian:

Da terminale, eseguire i seguenti comandi per installare JDK 8 e ANT.

```
sudo apt update
sudo apt install openjdk-8-jdk -y
sudo apt install ant -y
```

A questo punto, bisogna clonare la repository del progetto.

```
git clone https://github.com/AGalassi/TablutCompetition.git
```

## Eseguire il Server senza utilizzare Eclipse

Il metodo piu' comodo per eseguire il server consiste nell'utilizzare lo
script di configurazione ANT da terminale. In particolare:

```
# Entrare nella cartella del progetto
cd TablutCompetition/Tablut
```

Un modo per capire se si e' nella cartella giusta e' verificare che sia presente il file build.xml

A questo punto, per compilare il progetto:

```
ant clean
ant compile
```

Il progetto e' stato compilato nella cartella `build`. Per lanciare il server
si puo' utilizzare il comando:

```
ant server
```

Per provarne il funzionamento, si possono attivare i due giocatori Random:

```
# Da altre due finestre di terminale
ant randomwhite

# E nell'altra
ant randomblack
```

A questo punto, si dovrebbe aprire una finestra che mostra graficamente lo
stato della partita.

## Giocare una partita

Il primo passo e' avviare il server:

```
ant server
```

A questo punto, in due terminali differenti, si possono avviare i giocatori.
Questi possono essere di due tipi:

* Random
* Human

Il giocatore di tipo random giochera' la partita effettuando delle mosse casuali,
mentre il giocatore human fornisce un'interfaccia da terminale che permette di
muovere manualmente le pedine.

L'unica cosa importante, e' avviare **un giocatore di tipo nero ed uno di tipo bianco**.

Esempi:

#### Partita tra due esseri umani

In due terminali diversi, utilizzare i comandi:

```
ant humanwhite

ant humanblack
```

#### Partita tra un essere umano ed un giocatore random

In due terminali distinti:

```
ant humanblack

ant randomwhite
```
