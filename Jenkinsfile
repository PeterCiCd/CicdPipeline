pipeline {
    agent
     {
        label "mvn_agent"
    }
    environment {
        PACKAGE_VERSION = "${GIT_BRANCH}-${GIT_COMMIT}"
    }
    parameters {
        choice (
            name: 'DOCKER_BUILD',
            choices: ['no', 'yes'],
            description: 'Shall we build docker image'
        )

        string (
            name: 'DOCKER_TAG',
            defaultValue: 'latest',
            description: 'Tag of docker image'
        )
    }
    tools {
        dockerTool 'docker'
    }

    stages{

        stage("Run tests"){
            steps{
                sh 'mvn test'
                echo 'test finish'
            }
        }
        
        stage("Build maven"){
            steps {
                sh 'mvn package \
                -Dmaven.test.skip=true \
                -Dapp=cicd-demo-app \
                -Drevision=$PACKAGE_VERSION'
                sh 'ls -la target/'
            }
        }

        stage("Upload jar artifact") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'NEXUS_USER', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]){
                    sh 'ls target '
                    sh '''
                    curl -v \
                        -F maven2.groupId=cicd-demo \
                        -F maven2.asset1.extension=jar \
                        -F maven2.asset1=@target/cicd-demo-app-$PACKAGE_VERSION.jar \
                        -F maven2.artifactId=cicd-demo-app \
                        -F maven2.version=${GIT_COMMIT} \
                        -u $USERNAME:$PASSWORD http://nexus:8081/service/rest/v1/components?repository=maven-dev
                    '''
                }
            }
        }

       stage("Docker build and push"){
//           when {
//               anyOf {
//                   branch 'master'
//                   expression { params.DOCKER_BUILD == 'yes' }
//              }
//           }
           environment {
               TAG = "${params.DOCKER_TAG}"
           }
           steps {
               withCredentials([usernamePassword(credentialsId: 'NEXUS_USER', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]){
                   sh 'docker build -t demo-app:$TAG .'
                   sh 'docker tag demo-app:$TAG nexus:5000/repository/demo/demo-app:$TAG'
                   sh 'docker login --username $USERNAME --password $PASSWORD http://nexus:5000/repository/demo '
                   sh 'docker push nexus:5000/demo/demo-app:$TAG'
               }
           }
       }

    }
    post{
        always{
            junit 'target/surefire-reports/**/*.xml'
            cleanWs()
        }
    }
}