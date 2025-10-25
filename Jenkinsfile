pipeline {
    agent any

    environment {
        IMAGE_NAME = "taskmanagement-app"
        CONTAINER_NAME = "taskmanagement-app"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'http://localhost/root/TaskManagement.git', credentialsId: 'gitlab-root-token'
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

//         stage('Load Image into Minikube') {
//             steps {
//                 sh "minikube image load ${IMAGE_NAME}:${IMAGE_TAG}"
//             }
//         }
//
//         stage('Deploy to Kubernetes') {
//             steps {
//                sh """
//                    kubectl apply -f ${DEPLOYMENT_FILE}
//                    kubectl apply -f ${SERVICE_FILE}
//                """
//             }
//         }


//         stage('Deploy Container') {
//             steps {
//                 sh '''
//                     docker stop ${CONTAINER_NAME} || true
//                     docker rm ${CONTAINER_NAME} || true
//                     docker run -d -p 8081:8081 --name ${CONTAINER_NAME} ${IMAGE_NAME}
//                 '''
//             }
//         }

                stage('Deploy with Docker Compose') {
                    steps {
                        sh '''
                            echo "🚀 Stopping any existing containers..."
                            docker-compose -f ${COMPOSE_FILE} down

                            echo "🚀 Starting containers with Docker Compose..."
                            docker-compose -f ${COMPOSE_FILE} up -d

                            echo "✅ Application and MySQL started!"
                            echo "🔗 Access app at http://localhost:8081"
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
