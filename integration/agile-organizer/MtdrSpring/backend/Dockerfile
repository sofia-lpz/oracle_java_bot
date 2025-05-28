FROM openjdk:21

# Set working directory inside the container
WORKDIR /app

# Copy wallet and application files
COPY ./src/Wallet /Wallet 
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

# Use HERE document to write bashrc entries (headless Linux compatible)
RUN cat >> ~/.bashrc << 'EOF'
export M2_HOME=/usr/local/apache-maven-3.9.9
export M2=/usr/local/apache-maven-3.9.9/bin
export MAVEN_OPTS="-Xms256m -Xmx512m"
export PATH=$M2:$PATH
EOF

# Verify Maven installation
RUN mvn -version

# List files after copying them
RUN ls -l /app

# Build the application
RUN mvn clean install
RUN mvn verify

# Set database environment variables
ENV db_user="ADMIN" \
    dbpassword="None00010001" \
    db_url="jdbc:oracle:thin:@reacttodox774w_tp?TNS_ADMIN=/Wallet" \
    driver_class_name="oracle.jdbc.OracleDriver"

# Expose ports
EXPOSE 8080 9898 9090 5173 3306

# Start the application
ENTRYPOINT ["mvn", "spring-boot:run"]