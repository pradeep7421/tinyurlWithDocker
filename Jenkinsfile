pipeline {
    agent any

    environment {
        // Your DockerHub repo
        DOCKER_IMAGE = "pradeep7421/devtinyurlwithdocker"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/pradeep7421/tinyurlWithDocker.git'
            }
        }

        stage('Build JAR') {
            steps {
                // Assuming Maven project
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE:${BUILD_NUMBER} .'
                sh 'docker tag $DOCKER_IMAGE:${BUILD_NUMBER} $DOCKER_IMAGE:latest'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push $DOCKER_IMAGE:${BUILD_NUMBER}'
                    sh 'docker push $DOCKER_IMAGE:latest'
                }
            }
        }

        stage('Deploy to Dev') {
            steps {
                sh '''
                  docker rm -f tinyurl-dev || true
                  docker run -d --name tinyurl-dev -p 8081:8080 \
                    -e SPRING_PROFILES_ACTIVE=dev \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }

        stage('Deploy to QA') {
            steps {
                input message: "Deploy to QA?"
                sh '''
                  docker rm -f tinyurl-qa || true
                  docker run -d --name tinyurl-qa -p 8082:8080 \
                    -e SPRING_PROFILES_ACTIVE=qa \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }

        stage('Deploy to UAT') {
            steps {
                input message: "Deploy to UAT?"
                sh '''
                  docker rm -f tinyurl-uat || true
                  docker run -d --name tinyurl-uat -p 8083:8080 \
                    -e SPRING_PROFILES_ACTIVE=uat \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }

        stage('Deploy to Prod') {
            steps {
                input message: "Deploy to Prod?"
                sh '''
                  docker rm -f tinyurl-prod || true
                  docker run -d --name tinyurl-prod -p 8080:8080 \
                    -e SPRING_PROFILES_ACTIVE=prod \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }

    }

    post {
        always {
            echo "Pipeline finished. Build #${BUILD_NUMBER}"
        }
        failure {
            echo "Build failed! Check Jenkins logs."
        }
    }
}
