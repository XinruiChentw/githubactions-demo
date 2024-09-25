pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-east-1'
        ROLE_ARN = 'arn:aws:iam::494526681395:role/xr-github-test'
        REPOSITORY = 'xinrui/demo'
        IMAGE_TAG = "${env.BUILD_ID}"
        WEBHOOK_URL = credentials('WEBHOOK_URL')
        AWS_ACCOUNT_ID = '494526681395'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Login to ECR') {
            steps {
                script {
                    def login = sh(script: 'aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com', returnStdout: true).trim()
                    echo "Login to ECR: ${login}"
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    def registry = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
                    sh """
                        docker build -t ${registry}/${REPOSITORY}:${IMAGE_TAG} .
                        docker push ${registry}/${REPOSITORY}:${IMAGE_TAG}
                    """
                }
            }
        }
    }

    post {
        failure {
            script {
                sh './auto/send_message'
            }
        }
    }
}
