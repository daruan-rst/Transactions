FROM openjdk:11

EXPOSE 7080

ADD build/libs/Transactions-0.0.1.jar Transactions-0.0.1.jar

ENTRYPOINT ["java","-jar","Transactions-0.0.1.jar"]