set datafile separator ","
set terminal pngcairo size 1200,600 enhanced font 'Verdana,10'
set output "login_test.png"

set title "Login-Antwortzeiten pro Benutzer"
set xlabel "Benutzer"
set ylabel "Antwortzeit (ms)"
set grid

plot "auswertung.dat" using 0:2 with lines lw 2 lc rgb "#1f77b4" title "Login-Zeit"

