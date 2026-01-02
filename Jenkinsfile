pipeline {
    agent any

    tools {
        // Must match names in Manage Jenkins -> Tools
        maven 'Maven 3.x'
        jdk 'Java 17'
        dockerTool 'docker-tool'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Creates your ems-backend-0.0.1-SNAPSHOT.jar
                sh 'mvn clean package'
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    // This block securely handles your Docker Hub login
                    // 'docker-hub-creds' is the ID you set in Jenkins Credentials
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-creds',
                                    passwordVariable: 'DOCKER_PWD',
                                    usernameVariable: 'DOCKER_USER')]) {

                        // 1. LOGIN: Jenkins "speaks" to Docker Hub
                        sh "echo ${DOCKER_PWD} | docker login -u ${DOCKER_USER} --password-stdin"

                        // 2. BUILD: Tags image with your username
                        sh "docker build -t ${DOCKER_USER}/ems-backend:jenkins-latest ."

                        // 3. PUSH: Uploads it to the cloud
                        sh "docker push ${DOCKER_USER}/ems-backend:jenkins-latest"

                        // 4. Pull the latest image we just pushed
                        sh "docker pull ${DOCKER_USER}/ems-backend:jenkins-latest"

                        // 5. Run the new container
                        // -d: detached mode, -p: port mapping, --name: easy to find later
                        sh "docker run -d -p 8081:8080 --name ems-app ${DOCKER_USER}/ems-backend:jenkins-latest"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Successfully pushed to dockerhub"
        }
        failure {
            echo "Build failed. Check 'Console Output' for errors."
        }
    }
}