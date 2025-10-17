pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'http://localhost/root/TaskManagement.git', credentialsId: 'gitlab-root-token'
            }
        }

        stage('Start MySQL') {
            steps {
                sh '''
                docker rm -f mysql-test || true
                docker run --name mysql-test -e MYSQL_ROOT_PASSWORD=root \
                -e MYSQL_DATABASE=TaskDB -e MYSQL_USER=springuser \
                -e MYSQL_PASSWORD=springpass -p 3307:3306 -d mysql:8
                echo "Waiting for MySQL to start..."
                sleep 25
                '''
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test || true'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
    post {
        always {
            sh 'docker rm -f mysql-test || true'
        }
    }
}
