pipeline {
    agent any

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()  // THIS CLEARS the workspace before doing anything
            }
        }

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
