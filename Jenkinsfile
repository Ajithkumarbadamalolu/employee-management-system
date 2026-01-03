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
                    }
                }
            }
        }
        stage('Deploy to K8s') {
            steps {
                script {
                    // 1. Ensure the Deployment and Service exist
                    sh 'kubectl apply -f deployment.yaml'

                    // 2. Force the update to pick up the new "jenkins-latest" image
                    sh 'kubectl rollout restart deployment/ems-backend'
                }
            }
        }
    }

    post {
        success {
            echo "Successfully pushed to dockerhub and deployed to kubernetes"
        }
        failure {
            echo "Build failed. Check 'Console Output' for errors."
        }
    }
}