FROM ubuntu:14.04
RUN apt-get update && apt-get -y upgrade

RUN apt-get -y install software-properties-common
RUN add-apt-repository ppa:webupd8team/java
RUN apt-get -y update

# Accept the license
RUN echo "oracle-java7-installer shared/accepted-oracle-license-v1-1 boolean true" | debconf-set-selections

RUN apt-get -y install oracle-java7-installer

# Here comes the tomcat installation
RUN apt-get -y install tomcat7
RUN echo "JAVA_HOME=/usr/lib/jvm/java-7-oracle" >> /etc/default/tomcat7

# Expose the default tomcat port
EXPOSE 8080

# Start the tomcat (and leave it hanging)
CMD service tomcat7 start && tail -f /var/lib/tomcat7/logs/catalina.out
