pipeline {
    agent any

    tools {
        // These names MUST match what you set in 'Global Tool Configuration'
        maven 'Maven 3.x'
        jdk 'Java 17'
    }

    stages {
        stage('Checkout') {
            steps {
                // This pulls the code from the Git repo you configured in the job
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // We use 'sh' because our Jenkins container is running Linux
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // This builds the image locally on your machine via the Docker socket
                    // Replace 'ajith' with your Docker Hub username
                    sh "docker build -t ajith151020/ems-backend:jenkins-latest ."
                }
            }
        }
    }

    post {
        always {
            // Clean up the workspace after the build to save disk space
            cleanWs()
        }
        success {
            echo 'Build, Test, and Docker packaging were successful!'
        }
        failure {
            echo 'The pipeline failed. Check the console output above.'
        }
    }
}