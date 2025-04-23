set terminal pngcairo size 800,600 font 'Verdana, 20'
set output 'SWE3-Lasttest.png'
set y2tics font 'Verdana,18'
set key font 'Verdana,18'
set key top left
set grid
set yrange [0:100]
set xrange [0:600]
set xlabel 'Anzahl der User' font 'Verdana, 20'
set ylabel 'Benötigte Millisekunden' font 'Verdana,20'
set y2label 'Erfolgsrate (%)' font 'Verdana,20'

plot 'auswertung.dat' using 1:2 with linespoints title 'Durchschnittliche Antwortzeit' linecolor rgb 'blue' lw 5, \
     'erfolgsrate.dat' using 1:2 axes x1y2 with linespoints title 'Erfolgsrate' linecolor rgb 'red' lw 5
