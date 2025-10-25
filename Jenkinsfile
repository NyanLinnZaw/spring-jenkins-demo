pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                // Pull latest code from Git
                checkout scm
            }
        }

        stage('Build App') {
            steps {
                echo "Building Spring Boot app..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Clean Docker Resources') {
            steps {
                echo "Cleaning old Docker containers, images, and volumes..."
                // Stop and remove old containers
                sh '''
                docker ps -aq | xargs -r docker rm -f
                docker images -f "dangling=true" -q | xargs -r docker rmi -f
                docker volume ls -qf "dangling=true" | xargs -r docker volume rm
                '''
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo "Deploying containers using docker-compose..."
                // Stop any old containers of this project
                sh 'docker-compose down --remove-orphans'
                // Build images and start containers in detached mode
                sh 'docker-compose up -d --build'
            }
        }

        stage('Verify Deployment') {
            steps {
                echo "Listing running containers..."
                sh 'docker ps'
                echo "Showing app logs (last 50 lines)..."
                sh 'docker-compose logs --tail=50 app'
            }
        }
    }

    post {
        success {
            echo "✅ Deployment completed successfully!"
        }
        failure {
            echo "❌ Deployment failed! Check logs above."
        }
    }
}
