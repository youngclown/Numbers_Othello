Toy project v.3
---

#MariaDB 설치 및 계정 생성

[root@zabbix1 ~]# vi /etc/yum.repos.d/MariaDB.repo
```
[mariadb]
name = MariaDB
baseurl = http://yum.mariadb.org/10.4/centos6-amd64
gpgkey=https://yum.mariadb.org/RPM-GPG-KEY-MariaDB
gpgcheck=1
```
[root@localhost ~]# yum install MariaDB
[root@localhost ~]# rpm -qa | grep MariaDB
[root@localhost ~]# sudo service mysql start
[root@localhost ~]# /usr/bin/mysqladmin -u root password '123456'
[root@localhost ~]# mysql -u root -p
Enter password: 123456

MariaDB [(none)]> CREATE DATABASE testdb default CHARACTER SET UTF8;
MariaDB [(none)]> create user 'test'@'%' identified by '123456';
MariaDB [(none)]> grant all privileges on *.* to test@'%';
MariaDB [(none)]> flush privileges;
MariaDB [(none)]> quit

[root@localhost ~]# netstat -lnp | grep mysql
tcp        0      0 :::3306                     :::*                        LISTEN      1232/mysqld
unix  2      [ ACC ]     STREAM     LISTENING     11174  1232/mysqld         /var/lib/mysql/mysql.sock


[root@localhost ~]# iptables -I INPUT -p tcp --dport 3306 -m state --state NEW,ESTABLISHED -j ACCEPT
[root@localhost ~]# iptables -I OUTPUT -p tcp --sport 3306 -m state --state ESTABLISHED -j ACCEPT

[root@localhost ~]# service iptables save
iptables: 방화벽 규칙을 /etc/sysconfig/iptables에 저장 중: [  OK  ]

[root@localhost ~]# service iptables restart
iptables: 체인을 ACCEPT 규칙으로 설정 중:  filter          [  OK  ]
iptables: 방화벽 규칙을 지웁니다:                          [  OK  ]
iptables: 모듈을 언로드하는 중:                            [  OK  ]
iptables: 방화벽 규칙 적용 중:                             [  OK  ]

