FROM openjdk:21

# Set working directory inside the container
RUN /bin/bash
WORKDIR /app

# Copy your Java application files
COPY ./src/Wallet /Wallet
COPY . /app

# Create the config directory and add the .env file
RUN mkdir -p /app/src/main/java/com/springboot/MyTodoList/config
RUN mkdir -p /app/src/main/resources


# Copy Maven and extract it
COPY maven.tar.gz /usr/local/
RUN tar -xzf /usr/local/maven.tar.gz -C /usr/local/ && \
    rm /usr/local/maven.tar.gz

# Set environment variables for Maven
ENV M2_HOME=/usr/local/apache-maven-3.9.9 \
    M2=/usr/local/apache-maven-3.9.9/bin \
    MAVEN_OPTS="-Xms256m -Xmx512m"

# Update PATH
ENV PATH="$M2:$PATH"

# Reload environment variables (simulating source ~/.bashrc)
RUN /bin/bash -c 'echo "export M2_HOME=$M2_HOME" >> ~/.bashrc && \
    echo "export M2=$M2" >> ~/.bashrc && \
    echo "export MAVEN_OPTS=\"$MAVEN_OPTS\"" >> ~/.bashrc && \
    echo "export PATH=$M2:\$PATH" >> ~/.bashrc'
 
# Verify Maven installation
RUN mvn -version

WORKDIR /app

# List files after copying them
RUN ls -l /app

# Verify .env file and application.properties were created correctly
RUN cat /app/src/main/java/com/springboot/MyTodoList/config/.env
RUN echo "--- Application Properties ---"
RUN cat /app/src/main/resources/application.properties

# Build the application
RUN mvn clean install
RUN mvn verify

# Environment variables (keeping your original ones as well)
ENV db_user=""
ENV dbpassword=""
ENV db_url="jdbc:oracle:thin:@re_tp?TNS_ADMIN=/Wallet"
ENV driver_class_name="oracle.jdbc.OracleDriver"

# Expose ports
EXPOSE 8080
EXPOSE 9898
EXPOSE 9090
EXPOSE 5173
EXPOSE 3306

# Start the application
ENTRYPOINT ["mvn","spring-boot:run"]


asd