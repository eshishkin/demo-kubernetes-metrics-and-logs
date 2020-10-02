# Kubernetes Demo Application

This is a simple application for demonstration main features of Kubernetes and related technologies (EFK, Prometheus/Grafana, etc). 
The application is pretty simple. It has two services - sender and receiver. Sender sends messages to RabbitMQ topic, receiver just reads the topic and 
saves messages to Mongo

## How to build it locally

### Requirements
- Apache Maven 3.6.2 and above
- Java 11 and above
- Docker
- Kubernetes
- Helm


### Building docker images

* Set proper environment in case of Minikube (https://minikube.sigs.k8s.io/docs/commands/docker-env/)
  ```
  eval $(minikube docker-env)
  ```
* Building receiver service
  ```
  cd receiver-service
  ./mvnw clean verify
  ```
* Building sender service
  ```
  cd sender-service
  ./mvnw clean verify
  ```

### Running the application locally

* Go to `etc/k8s/charts/`
* Run a helm chart
  ```
  helm upgrade -i <name> app-with-console-logs/
  ```
