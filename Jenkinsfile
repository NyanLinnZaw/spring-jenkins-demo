pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
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
                sh '''
                # Stop and remove all containers
                docker ps -aq | xargs -r docker rm -f

                # Remove dangling images
                docker images -f "dangling=true" -q | xargs -r docker rmi -f

                # Remove dangling volumes
                docker volume ls -qf "dangling=true" | xargs -r docker volume rm
                '''
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo "Deploying containers using docker compose..."
                // Stop old containers, remove orphans, and rebuild
                sh 'docker compose down --remove-orphans'
                sh 'docker compose up -d --build'
            }
        }

        stage('Verify Deployment') {
            steps {
                echo "Listing running containers..."
                sh 'docker ps'
                echo "Showing last 50 lines of app logs..."
                sh 'docker compose logs --tail=50 app'
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
