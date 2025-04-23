#!/usr/bin/env bash
echo "set terminal pngcairo size 800,600 font 'Verdana, 12'
set output 'SWE3-Lasttest.png'
set yrange [0:300]
set xrange [0:2000]
set xlabel 'Anzahl der Iterationen' font 'Verdana,12'
set ylabel 'Benötigte Millisekunden' font 'Verdana,12'

set grid
plot 'auswertung.dat' using 1:2 \
  with lines linecolor rgb 'black'" > gnu_last.gp

gnuplot gnu_last.gp
mv SWE3-Lasttest.png /html/$USER/
