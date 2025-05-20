# Projekt IRA von [TEAM-03]

Dieses Projekt ist Open-Source und steht unter der [MIT License](LICENSE.txt).

![image](https://github.com/user-attachments/assets/244996f3-5b81-4737-9ce4-a0e0888e2618)


Folgende Bibliotheken werden verwendet:

- commons-pool2 
- jakartaee-api 
- jedis
- json
- HikariCP
- slf4j-api
#pdf
- commons-logging
- fontbox
- pdfbox
#qrcode
- core
- javase


Abrufbar unter:
- [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)
- [MIT License](https://opensource.org/licenses/MIT)
- [Eclipse Public License](https://www.eclipse.org/legal/epl-2.0/)


DEPLOY:
## before you start
run
bin/configure.sh
to get config data from hopper to local

or run
bin/configure-work.sh
to create config for local network in docker environment

and run
bin/download-libs.sh
to download necessary java-libraries


## build cycle
bin/build.sh
- prepare
- compile
- assemble
- deploy
- check

## to clean build and target
bin/clean.sh

# to clean build, target, lib and app/WEB-INF/lib
bin/clean-all.sh
