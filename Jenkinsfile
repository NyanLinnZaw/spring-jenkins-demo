pipeline {
    agent any
    environment {
        MYSQL_ROOT_PASSWORD = 'root'
        MYSQL_DATABASE = 'TaskDB'
        MYSQL_USER = 'springuser'
        MYSQL_PASSWORD = 'springpass'
    }
    stages {
        stage('Start MySQL') {
            steps {
                sh '''
                docker run --name mysql-test -e MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD \
                -e MYSQL_DATABASE=$MYSQL_DATABASE \
                -e MYSQL_USER=$MYSQL_USER \
                -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
                -p 3307:3306 -d mysql:8
                sleep 20
                '''
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
    }
    post {
        always {
            sh 'docker stop mysql-test && docker rm mysql-test || true'
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
        }
    }
}
