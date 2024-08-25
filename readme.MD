# Smart Tailor BE

## Tech stack
* Build Tool: Maven 3.9.5 or higher
* Programming Language: Java 17
* Framework: Spring Boot 3.2.5
* Database Management System (DBMS): MySQL
* Authentication: OAuth2 (Google)
* Security: JWT (JSON Web Token)
* Email Service: Spring Thymeleaf Mail with Gmail
* Payment Gateway: PayOS, VNPAY
* Shipping Service: GHTK (Giao Hàng Tiết Kiệm)
* Deployment: Heroku
* Database Hosting: Railway
* Other Integrations: VietQR, WebSocket, ...

## Prerequisites
* Java SDK: Version 17
* MySQL Server: Version 8.0 or higher

## Start application
`mvn spring-boot:run`

## Build application
`mvn clean package`

## Docker Guidelines
### 1. Building the Docker Image
`docker build -t <dockerhub_username>/<docker_image_name>:<tag_name> .`

### 2. Pushing the Docker Image to Docker Hub
`docker image push <dockerhub_username>/<docker_image_name>:<tag_name>`

### 3. Creating a Docker Network
`docker network create <docker_network_name>`

### 4. Starting MySQL in Docker Network
`docker run --network <docker_network_name> --name mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.39-debian`

### 5. Running the Application in Docker Network
`docker run --name <docker_container_name> --network <docker_network_name> -p 6968:6969 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/smart_tailor_be <dockerhub_username>/<docker_image_name>:<tag_name>`

## Installing Docker on Ubuntu

### 1. Add Docker's Official GPG Key
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

### 2. Add Docker Repository to Apt Sources
echo \
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

### 3. Install Docker
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

### 4. Verify Docker Installation
sudo docker run hello-world
"# smart_tailor_be" 
