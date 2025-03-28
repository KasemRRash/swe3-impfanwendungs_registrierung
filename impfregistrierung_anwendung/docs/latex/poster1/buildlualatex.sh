#!/bin/bash

#lualatex poster.tex
latexmk -pdf -lualatex -shell-escape -interaction=nonstopmode -f -outdir=log poster.tex

mv log/poster.pdf ./
cp poster.pdf /var/www/html/$USER/
