jbelt
=====
1. Add libbee2.so to ldconfig:
	```su -c "echo '/home/PATH/TO/FOLDER/CONTAINING/BEE2/' >  /etc/ld.so.conf.d/bee2.conf"```
	sudo ldconfig
2. Install openjdk-7 and maven2 ```sudo apt-get install openjdk-7-jdk maven2```

3. ```mvn test```