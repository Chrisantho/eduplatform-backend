pipeline {
    agent any

    stages {

        stage('Verify Docker') {
            steps {
                bat 'docker version'
            }
        }

        stage('Build Jar') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t edu-backend:latest .'
            }
        }
    }
}
