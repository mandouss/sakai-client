#!/bin/bash
# automatic set up enviroment for sakai server
# Author: Ziyi Wang

# install JDK 1.8
sudo apt-get update
sudo apt-get install default-jdk

# check and install git
if command -v git >/dev/null; then
    echo "git is installed"
else
    sudo apt-get install git
    echo "finsihed installing git"
fi

# check and instal MySQL
if command -v mysql >/dev/null; then
    echo "mysql is installed"
else
    sudo apt-get install mysql-server
    mysql_secure_installation
    echo "finsihed installing MySQL"
fi

# install Apache Maven
wget -P /opt https://archive.apache.org/dist/maven/maven-3/3.2.3/binaries/apache-maven-3.2.3-bin.tar.gz 

sudo tar -xvzf /opt/apache-maven-3.2.3-bin.tar.gz

sudo mv /opt/apache-maven-3.2.3 /opt/maven

echo "finished installing Maven"

# install Apache Tomcat

curl -O https://archive.apache.org/dist/tomcat/tomcat-8/v8.0.32/bin/apache-tomcat-8.0.32.tar.gz

sudo mkdir /opt/tomcat

sudo tar xzvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1

echo "finished installing tomcat"

# download jdbc
wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.45.tar.gz

sudo tar -xvzf mysql-connector-java-5.1.45.tar.gz

sudo mv ./mysql-connector-java-5.1.45/mysql-connector-java-5.1.45-bin.jar $CATALINA_HOME/lib

echo "finished placing the JDBC"

# add envirment variables
echo "export MAVEN_HOME=/opt/maven" >> ~/.bashrc
echo "export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64" >> ~/.bashrc
echo "export CATALINA_HOME=/opt/tomcat" >> ~/.bashrc
echo "export PATH=\$CATALINA_HOME/bin:\$JAVA_HOME/bin:\$MAVEN_HOME/bin:\$PATH" >> ~/.bashrc
echo "export MAVEN_OPTS='-Xms512m -Xmx1024m -Djava.util.Arrays.useLegacyMergeSort=true'" >> ~/.bashrc

# set up data base
echo "Now seting up MySQL, first you will need to input password"
echo "After that, please execute the following 5 lines of command"
echo "create database sakaidatabase default character set utf8;"
echo "grant all on sakaidatabase.* to sakaiuser@'localhost' identified by 'sakaipassword';"
echo "grant all on sakaidatabase.* to sakaiuser@'127.0.0.1' identified by 'sakaipassword';"
echo "flush privileges;"
echo "quit"
mysql -u root -p
echo "finished seting up MySQL"

# set up Tomcat
# -- sakai.properties setting
mkdir $CATALINA_HOME/sakai

touch $CATALINA_HOME/sakai/sakai.properties

echo "## MySQL settings
username@javax.sql.BaseDataSource=sakaiuser   #whatever username you assigned in the database setup
password@javax.sql.BaseDataSource=sakaipassword #whatever password you assigned to the sakaiuser in the database setup

# use your sakaidatabase name in the URI that starts url@javax.sql.BaseDataSource= , below.

vendor@org.sakaiproject.db.api.SqlService=mysql
driverClassName@javax.sql.BaseDataSource=com.mysql.jdbc.Driver
hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
url@javax.sql.BaseDataSource=jdbc:mysql://127.0.0.1:3306/sakaidatabase?useUnicode=true&characterEncoding=UTF-8
validationQuery@javax.sql.BaseDataSource=select 1 from DUAL
defaultTransactionIsolationString@javax.sql.BaseDataSource=TRANSACTION_READ_COMMITTED" >> $CATALINA_HOME/sakai/sakai.properties

# -- xml setting
sudo su

echo "Need your help 1****************************************************"
echo "please open up $CATALINA_HOME/conf/context.xml"
echo "Within the <Context> label add

   <JarScanner>
    <!-- This is to speedup startup so that tomcat doesn't scan as much -->
    <JarScanFilter defaultPluggabilityScan=\"false\" defaultTldScan=\"false\" tldScan=\"jsf-impl-*.jar,jsf-widgets-*.jar,myfaces-impl-*.jar,pluto-taglib-*.jar,sakai-sections-app-util-*.jar,spring-webmvc-*.jar,standard-*.jar,tomahawk*.jar,tomahawk-*.jar\"/>
</JarScanner>"

read -p "Press enter to continue, when you have done it"

echo "Need your help 2****************************************************"
echo "please open up $CATALINA_HOME/conf/server.xml"
echo "Modify CATALINA_HOME/conf/server.xml for international character support (Sakai is internationalized and has 20 languages available). Add URIEncoding to the Connector element. <Connector port=\"8080\" URIEncoding=\"UTF-8\" ..."

read -p "Press enter to continue, when you have done it"

# -- set up setenv
touch $CATALINA_HOME/setenv.sh

echo "export JAVA_OPTS='-server -Xms512m -Xmx1024m -XX:PermSize=128m -XX:NewSize=192m -XX:MaxNewSize=384m -Djava.awt.headless=true -Dhttp.agent=Sakai -Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false -Dsun.lang.ClassLoader.allowArraySyntax=true -Djava.util.Arrays.useLegacyMergeSort=true -Dsakai.demo=true'" >> setenv.sh

sudo mv $CATALINA_HOME/setenv.sh $CATALINA_HOME/bin

## compile sakai
echo "start compile Sakai"
echo "Please execute the following command one by one"

cd $CATALINA_HOME/sakai

git clone https://github.com/sakaiproject/sakai.git

cd sakai 

git checkout 11.2

cd master

mvn clean install

# deploying sakai
cd ..

mvn clean install sakai:deploy -Dmaven.tomcat.home=\$CATALINA_HOME -Djava.net.preferIPv4Stack=true -Dmaven.test.skip=true

# finish
echo "if the compile was all successful, now you can go to \$CATALINA_HOME and run startup.sh to open up sakai site"
