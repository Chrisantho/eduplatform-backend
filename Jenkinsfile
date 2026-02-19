pipeline {
    agent any

    stages {
        stage('Verify Docker') {
            steps {
                sh 'docker version'
            }
        }

        stage('Build Jar') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t edu-backend:latest .'
            }
        }
    }
}
