# Smart Tailor BE

## Tech stack
* Build tool: Maven 3.9.5 or higher
* Java: 17
* Framework: Spring boot 3.2.5
* DBMS: MySQL
* Authentication: OAuth2 (Google)
* Security: JWT (JSON Web Token)
* Email Service: Spring Thymeleaf Mail with Gmail
* Payment Gateway: PayOS, VNPAY
* Shipping Service: GHTK
* Deployment: Heroku
* Database: Railway
* Other: VietQR, WebSocket,...

## Prerequisites
* Java SDK 17
* A MySQL server

## Start application
`mvn spring-boot:run`

## Build application
`mvn clean package`

## Docker guideline
### 1. Build Docker Image
`docker build -t <account>/smart_tailor_be:<tag_name> .`

### 2. Push Docker Image to Docker Hub
`docker image push <account>/smart_tailor_be:<tag_name>`

### 3. Create Docker Network
`docker network create smart_tailor_be_network`

### 4. Start MySQL in Docker Network
`docker run --network smart_tailor_be_network --name mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.39-debian`

### 5. Run Your Application in Docker Network
`docker run --name smart_tailor_be --network smart_tailor_be_network -p 6968:6969 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/smart_tailor_be <account>/smart_tailor_be:<tag_name>`

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
