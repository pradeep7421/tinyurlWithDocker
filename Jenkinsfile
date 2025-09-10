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
            #!/bin/bash
            echo "[INFO] Checking if DB containers exist..."
            
            # List of containers you want to check
            CONTAINERS=("mongo" "mongo-express" "adminer" "mysql-older")
            
            RUN_COMPOSE=false

            for c in "${CONTAINERS[@]}"; do
                if [ ! "$(docker ps -a -q -f name=^${c}$)" ]; then
                    echo "[INFO] Container $c does not exist."
                    RUN_COMPOSE=true
                else
                    echo "[INFO] Container $c exists."
                fi
            done

            if [ "$RUN_COMPOSE" = true ]; then
                echo "[INFO] Starting DB containers with docker-compose..."
                docker-compose -f $COMPOSE_FILE up -d
            else
                echo "[INFO] All DB containers already exist. Skipping docker-compose."
            fi
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
                  docker rm -f tinyurl-prod || true
                  docker run -d --name tinyurl-prod \
                    --network=root_tinyurl-net \
                    -p 8080:8080 \
                    -e SPRING_PROFILES_ACTIVE=proddocker \
                    $DOCKER_IMAGE:${BUILD_NUMBER}
                '''
            }
        }
    }

    post {
        always {
            echo "Pipeline finished. Branch=${env.BRANCH_NAME}, Build #${BUILD_NUMBER}"
        }
    }
}

