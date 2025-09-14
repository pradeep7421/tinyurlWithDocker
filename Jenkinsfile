pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "pradeep7421/devtinyurlwithdocker"
        COMPOSE_FILE = "docker-compose-db2.yaml"
    }

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/pradeep7421/tinyurlWithDocker.git',
                    branch: "${env.BRANCH_NAME}",
                    credentialsId: 'github-cred'
            }
        }

        stage('Start Db (Mongo, MySQL, etc)') {
            when {
                branch 'master'
            }
            steps {
                sh '''
                    echo "[INFO] Removing any existing containers..."
                    docker rm -f mongo mongo-express adminer mysql-older || true

                    echo "[INFO] Starting db containers..."
                    docker-compose -f $COMPOSE_FILE up -d
                '''
            }
        }

        stage('Build JAR') {
            when {
                expression { env.BRANCH_NAME.startsWith("development") || env.BRANCH_NAME == "master" }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build & Push Docker Image') {
            when {
                branch 'master'
            }
            steps {
                sh 'docker build -t $DOCKER_IMAGE:${BUILD_NUMBER} .'
                sh 'docker tag $DOCKER_IMAGE:${BUILD_NUMBER} $DOCKER_IMAGE:latest'

                withCredentials([usernamePassword(credentialsId: 'dockerhub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push $DOCKER_IMAGE:${BUILD_NUMBER}'
                    sh 'docker push $DOCKER_IMAGE:latest'
                }
            }
        }

        stage('Deploy to Dev') {
            when { branch 'master' }
            steps {
                sh '''
                  docker rm -f tinyurl-dev || true
                  docker run -d --name tinyurl-dev \
                    --network=root_tinyurl-net \
                    -p 8081:8080 \
                    -e SPRING_PROFILES_ACTIVE=dev \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }

        stage('Deploy to QA') {
            when { branch 'master' }
            steps {
                input message: "Promote to QA?"
                sh '''
                  docker rm -f tinyurl-qa || true
                  docker run -d --name tinyurl-qa \
                    --network=root_tinyurl-net \
                    -p 8082:8080 \
                    -e SPRING_PROFILES_ACTIVE=qa \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }

        stage('Deploy to UAT') {
            when { branch 'master' }
            steps {
                input message: "Promote to UAT?"
                sh '''
                  docker rm -f tinyurl-uat || true
                  docker run -d --name tinyurl-uat \
                    --network=root_tinyurl-net \
                    -p 8083:8080 \
                    -e SPRING_PROFILES_ACTIVE=uat \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }

        stage('Deploy to Prod') {
            when { branch 'master' }
            steps {
                input message: "Promote to Prod?"
                sh '''
                  # Remove existing containers
                  docker rm -f tinyurl-prod1 tinyurl-prod2 tinyurl-prod3 tinyurl-prod4 tinyurl-prod5 || true

                  # Start 5 instances on different ports
                  docker run -d --name tinyurl-prod1 \
                    --network=root_tinyurl-net \
                    -p 8080:8080 \
                    -e SPRING_PROFILES_ACTIVE=proddocker \
                    $DOCKER_IMAGE:${BUILD_NUMBER}

                  docker run -d --name tinyurl-prod2 \
                    --network=root_tinyurl-net \
                    -p 8090:8080 \
                    -e SPRING_PROFILES_ACTIVE=proddocker \
                    $DOCKER_IMAGE:${BUILD_NUMBER}

                  docker run -d --name tinyurl-prod3 \
                    --network=root_tinyurl-net \
                    -p 8091:8080 \
                    -e SPRING_PROFILES_ACTIVE=proddocker \
                    $DOCKER_IMAGE:${BUILD_NUMBER}

                  docker run -d --name tinyurl-prod4 \
                    --network=root_tinyurl-net \
                    -p 8092:8080 \
                    -e SPRING_PROFILES_ACTIVE=proddocker \
                    $DOCKER_IMAGE:${BUILD_NUMBER}

                  docker run -d --name tinyurl-prod5 \
                    --network=root_tinyurl-net \
                    -p 8093:8080 \
                    -e SPRING_PROFILES_ACTIVE=proddocker \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }

        stage('Deploy to Prod on EC2') {
            when { branch 'master' }
            steps {
                input message: "Promote to Prod EC2 instance?"
                sshagent (credentials: ['ec2-ssh-key']) {
                    sh """
                      ssh -o StrictHostKeyChecking=no ec2-user@13.48.29.149 "
                        docker network inspect root_tinyurl-net >/dev/null 2>&1 || \
                        docker network create root_tinyurl-net &&
                        docker rm -f tinyurl-prod5 || true &&
                        docker run -d --name tinyurl-prod5 \
                          --network=root_tinyurl-net \
                          -p 8094:8080 \
                          -e SPRING_PROFILES_ACTIVE=proddocker \
                          pradeep7421/devtinyurlwithdocker:${BUILD_NUMBER}
                      "
                    """
                }
            }
        }
    } // <- closes stages block

    post {
        always {
            echo "Pipeline finished. Branch=${env.BRANCH_NAME}, Build #${BUILD_NUMBER}"
        }
    }
}
