FROM openjdk

# Set working directory inside the container
WORKDIR /app


COPY ./src/Wallet /Wallet 
# Copy your Java application JAR file into the container
COPY . /app
COPY maven.tar.gz /usr/local/

# Extract Maven
RUN tar -xzf /usr/local/maven.tar.gz -C /usr/local/ && \
    rm /usr/local/maven.tar.gz

# Set environment variables for Maven
ENV M2_HOME=/usr/local/apache-maven-3.9.9 \
    M2=/usr/local/apache-maven-3.9.9/bin \
    MAVEN_OPTS="-Xms256m -Xmx512m"

# Update PATH
ENV PATH="$M2:$PATH"

# Reload environment variables (simulating source ~/.bashrc)
RUN echo "export M2_HOME=$M2_HOME" >> ~/.bashrc && \
    echo "export M2=$M2" >> ~/.bashrc && \
    echo "export MAVEN_OPTS=$MAVEN_OPTS" >> ~/.bashrc && \
    echo "export PATH=$M2:\$PATH" >> ~/.bashrc && \
    source ~/.bashrc
RUN
 
# Verify Maven installation
RUN mvn -version


WORKDIR /app
# List files after copying them
RUN ls -l /app


RUN mvn clean install
RUN mvn verify

# RUN java -jar target/MyTodoList-0.0.1-SNAPSHOT.jar mytodo.jar >> output
ENV db_user="ADMIN"
ENV dbpassword="None00010001"
ENV db_url="jdbc:oracle:thin:@reacttodox774w_tp?TNS_ADMIN=/Wallet"
ENV driver_class_name="oracle.jdbc.OracleDriver"



# Expose a port if your app runs a server
EXPOSE 8080
EXPOSE 9898
EXPOSE 9090 
EXPOSE 5173
EXPOSE 3306

# CMD ["java", "-jar", "/app/myapp.jar"]
ENTRYPOINT ["mvn","spring-boot:run"]