Toy project v.5
---

#MariaDB 설치 및 계정 생성

1. Maria repository 설정  
 
```shell script
[root@zabbix1 ~]# vi /etc/yum.repos.d/MariaDB.repo
[mariadb]
name=MariaDB
baseurl=http://yum.mariadb.org/10.4/centos6-amd64
gpgkey=https://yum.mariadb.org/RPM-GPG-KEY-MariaDB
gpgcheck=1
```

2. repository 설정된 네이밍으로 yum install  

```shell script
[root@localhost ~]# yum install MariaDB
[root@localhost ~]# rpm -qa | grep MariaDB
```

3. MaridDB 실행 및 root 비밀번호 설정    

```shell script
[root@localhost ~]# sudo service mysql start
[root@localhost ~]# /usr/bin/mysqladmin -u root password '123456'
```

4. mysql 로그인하여 database db 설정  
```shell script  
[root@localhost ~]# mysql -u root -p  
Enter password: 123456  
MariaDB [(none)]> CREATE DATABASE testdb default CHARACTER SET UTF8;  
MariaDB [(none)]> create user 'test'@'%' identified by '123456';  
MariaDB [(none)]> grant all privileges on *.* to test@'%';  
MariaDB [(none)]> flush privileges;  
MariaDB [(none)]> quit  
```  

5. mysql 이 정상적으로 설치되어 데몬 프로세스에 올라와있는지 확인.    
```shell script  
[root@localhost ~]# netstat -lnp | grep mysql
tcp        0      0 :::3306                     :::*                        LISTEN      1232/mysqld
unix  2      [ ACC ]     STREAM     LISTENING     11174  1232/mysqld         /var/lib/mysql/mysql.sock
```  

6. 3306 port를 외부 포트로 허용

```shell script  
[root@localhost ~]# iptables -I INPUT -p tcp --dport 3306 -m state --state NEW,ESTABLISHED -j ACCEPT
[root@localhost ~]# iptables -I OUTPUT -p tcp --sport 3306 -m state --state ESTABLISHED -j ACCEPT
```

7. 방화벽 규칙 저장 (6번 항목 3306) 

```shell script
[root@localhost ~]# service iptables save
iptables: 방화벽 규칙을 /etc/sysconfig/iptables에 저장 중: [  OK  ]
```

8. 방화벽 재시작   
```shell script
[root@localhost ~]# service iptables restart
iptables: 체인을 ACCEPT 규칙으로 설정 중:  filter          [  OK  ]
iptables: 방화벽 규칙을 지웁니다:                          [  OK  ]
iptables: 모듈을 언로드하는 중:                            [  OK  ]
iptables: 방화벽 규칙 적용 중:                             [  OK  ]
```

# Redis 설치 및 계정 생성


1. redis 설치 (epel, remi 저장소는 기본적으로  활성화되지 않으므로 --enablerepo 옵션을 줌.

```shell script
[root@localhost ~]# rpm -Uvh http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
....
...
..  
[root@localhost ~]# yum --enablerepo=epel,remi install redis  
```

2. 부팅시 자동 실행하도록 설정

```shell script
[root@localhost ~]# chkconfig redis on  
```

3. 비밀번호와 외부 접속을 허용

```shell script
[root@localhost ~]# vi /etc/redis.conf  
requirepass 1234
bind 0.0.0.0
```

4. redis 구동

```shell script
[root@localhost ~]# service redis restart    
[root@localhost ~]# netstat -nap | grep LISTEN
tcp   0   0   127.0.0.1:6379   0.0.0.0:*    LISTEN     1534/redis-server 1
```




  