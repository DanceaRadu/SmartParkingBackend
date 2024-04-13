pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
        IMAGE_NAME = 'gonemesis/si-parking-backend'
    }
    stages {
        stage('Build') {
            steps {
                script {
                    docker.build("$IMAGE_NAME")
                }
            }
        }
        stage('Push') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIALS_ID) {
                        docker.image("$IMAGE_NAME").push("latest")
                        docker.image("$IMAGE_NAME").push("${env.BUILD_NUMBER}")
                    }
                }
            }
        }
    }
    post {
        success {
            echo 'Image pushed successfully!'
        }
        failure {
            echo 'Image push failed.'
        }
    }
}
