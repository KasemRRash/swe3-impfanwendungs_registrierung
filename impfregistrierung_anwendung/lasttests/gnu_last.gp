set terminal pngcairo size 800,600 font 'Verdana, 12'
set output 'SWE3-Lasttest.png'
<<<<<<< Updated upstream
set yrange [0:300]
set xrange [0:2000]
set xlabel 'Anzahl der Iterationen' font 'Verdana,12'
set yrange [0:50]
set xrange [0:600]
set xlabel 'Anzahl der User' font 'Verdana,12'
set ylabel 'Benötigte Millisekunden' font 'Verdana,12'

set grid
plot 'auswertung.dat' using 1:2   with lines linecolor rgb 'black'
