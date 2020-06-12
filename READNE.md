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

MariaDB [(none)]> create user 'test'@'%' identified by '123456';
Query OK, 0 rows affected (0.004 sec)

MariaDB [(none)]> grant all privileges on *.* to test@'%';
Query OK, 0 rows affected (0.001 sec)

MariaDB [(none)]>  flush privileges;
Query OK, 0 rows affected (0.000 sec)
