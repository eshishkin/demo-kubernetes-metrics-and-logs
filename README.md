# Kubernetes Demo Application

This is a simple application for demonstration main features of Kubernetes and related technologies (EFK, Prometheus/Grafana, etc). 
Also, it is a sandbox where I try different frameworks and tools. The application is pretty simple. 
It has two services - sender and receiver. Sender sends messages to RabbitMQ topic, receiver just reads the topic and 
saves messages to MongoDB.

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
  ./mvnw clean verify jib:dockerBuild
  ```
* Building sender service
  ```
  cd sender-service
  ./mvnw clean verify jib:dockerBuild
  ```

### Using Tekton pipeline

* Install Kubernetes somehow
* Install Tekton's resources to k8s cluster
  ```
  kubectl apply -f https://storage.googleapis.com/tekton-releases/pipeline/previous/v0.20.1/release.yaml
  ```
* Install Tekton's command line tool
* Execute build command that just builds two maven projects (nothing more)
  ```
  tkn task start build
  ```

### Using Skaffold

* Install Skaffold
* Execute `skaffold build` or `skaffold run`
 
### Running the application locally via Helm

* Go to `etc/k8s/charts/`
* Run a helm chart
  ```
  helm upgrade -i <name> app-with-console-logs/
  ```
