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

# Create the .env file with the specified content
RUN echo "db_url=jdbc:oracle:thin:@asd?TNS_ADMIN=/Wallet" > /app/src/main/java/com/springboot/MyTodoList/config/.env && \
    echo "db_user=" >> /app/src/main/java/com/springboot/MyTodoList/config/.env && \
    echo "dbpassword=" >> /app/src/main/java/com/springboot/MyTodoList/config/.env

# Create the application.properties file
RUN echo "spring.datasource.username=" > /app/src/main/resources/application.properties && \
    echo "spring.application.name=MicroserviceDemo" >> /app/src/main/resources/application.properties && \
    echo "server.port=8080" >> /app/src/main/resources/application.properties && \
    echo "spring.datasource.driver-class-name=oracle.jdbc.OracleDriver" >> /app/src/main/resources/application.properties && \
    echo "spring.datasource.url=jdbc:oracle:thin:@?TNS_ADMIN=/Wallet" >> /app/src/main/resources/application.properties && \
    echo "spring.datasource.password=" >> /app/src/main/resources/application.properties && \
    echo "spring.jpa.database-platform=org.hibernate.dialect.OracleDialect" >> /app/src/main/resources/application.properties && \
    echo "spring.jpa.properties.hibernate.query.in_clause_parameter_padding=true" >> /app/src/main/resources/application.properties && \
    echo "spring.jpa.properties.hibernate.query.default_join_style=ANSI" >> /app/src/main/resources/application.properties && \
    echo "telegram.bot.token=" >> /app/src/main/resources/application.properties && \
    echo "telegram.bot.name=JOES1_TELEGRAM_BOT" >> /app/src/main/resources/application.properties && \
    echo "security.jwt.secret-key=" >> /app/src/main/resources/application.properties && \
    echo "security.jwt.expiration-time=80000000" >> /app/src/main/resources/application.properties && \
    echo "openai.api.key=" >> /app/src/main/resources/application.properties && \
    echo "openai.model=gpt-3.5-turbo" >> /app/src/main/resources/application.properties && \
    echo "openai.api.url=https://api.openai.com/v1/chat/completions "  >> /app/src/main/resources/application.properties

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