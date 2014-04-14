
1.Add libbee2.so to ldconfig:
	su -c "echo '/home/path/to/bee2/linux/bin/Release64' >  /etc/ld.so.conf.d/bee2.conf"	
	sudo ldconfig
