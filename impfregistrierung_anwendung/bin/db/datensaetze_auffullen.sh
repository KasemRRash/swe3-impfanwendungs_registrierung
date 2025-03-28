#!/bin/bash


#for i in $(seq 1 10); do
   # EMAIL="testuser$i@example.com"
  #  BEFEHL=$(echo "INSERT INTO user (email, password) VALUES ('$EMAIL', 'a460fe9d94976a3348661d0f8b469acf56b741b74c57bb2ee052f76cee613996967cd76ff0efdb8f762dacd9b3995a4023b6b2c92648043a2609170539c8f32e973168f3522080fe');")
 #   mariadb -e "$BEFEHL" 
#done


{
  echo "INSERT INTO user (email, password) VALUES"

  for i in $(seq 1 50000); do
    EMAIL="testuser$i@example.com"
    echo -n "('$EMAIL', 'testuser$i')"

    if [ "$i" -lt 50000 ]; then
      echo ","
    else
      echo ";"
    fi
  done
} | mariadb 






#BEFEHL=$(INSERT INTO user (email, password) VALUES ('$email', 'a460fe9d94976a3348661d0f8b469acf56b741b74c57bb2ee052f76cee613996967cd76ff0efdb8f762dacd9b3995a4023b6b2c92648043a2609170539c8f32e973168f3522080fe');)
