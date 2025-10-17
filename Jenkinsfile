pipeline {
    agent any

    environment {
        IMAGE_NAME = "taskmanagement-app"
        CONTAINER_NAME = "taskmanagement-app"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'http://localhost/root/TaskManagement.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }

        stage('Deploy Container') {
            steps {
                sh '''
                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true
                    docker run -d -p 8081:8081 --name ${CONTAINER_NAME} ${IMAGE_NAME}
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful!'
        }
        failure {
            echo '❌ Deployment failed. Check Jenkins logs.'
        }
    }
}
