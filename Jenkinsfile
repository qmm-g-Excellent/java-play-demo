pipeline {
    agent any
    options{
        buildDiscarder(logRotator(numToKeepStr:'10'))
    }
   triggers{
       pollSCM('H/3 * * * 1-5')
   }

    environment {

        MYBRANCH = "${env.BRANCH_NAME}"

        GROUP_NAME = 'java-service'
        SERVICE_NAME = 'java-play-demo'
        
        BASE_IMAGE_TAG = "${GROUP_NAME}/${SERVICE_NAME}"
        BRANCH_IMAGE_TAG = "${GROUP_NAME}/${SERVICE_NAME}-${MYBRANCH}"

    }
    stages {   
        stage('Test') {
            steps {
                sh """
                    sbt clean test
                """
            }
        }

        stage('Build') {
            steps {
                script{
                    sh 'echo "-----Build stage-----"'
                    sh 'sbt clean dist'
                    sh 'unzip -o target/universal/java-play-demo-*.zip'
                }
            }
        }


    }


}