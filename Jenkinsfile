pipeline {

  agent { label 'k8s-dind-worker' }

  environment {
      DOCKER_HOST = 'tcp://localhost:2375'
  }

  stages {
    stage('Clone repository') {
      steps {
        checkout([
          $class: 'GitSCM',
          branches: [[name: '*/master']],
          doGenerateSubmoduleConfigurations: false,
          extensions: [],
          submoduleCfg: [],
          userRemoteConfigs: [[
            credentialsId: 'jenkins-clearpoint',
            url: 'git@github.com:abelbryo/hello-http4s-jenkins.git'
          ]]
        ])
      }
    }

    stage('Build and push artifact') {
      steps {
        withCredentials([[
            $class          : 'UsernamePasswordMultiBinding',
            credentialsId   : 'nexus-credentials',
            usernameVariable: 'NEXUS_USR',
            passwordVariable: 'NEXUS_PSW'
        ]]) {
            withDockerServer(uri: env.DOCKER_HOST, credentialsId: "") {
                withDockerContainer(image: 'ikhripunov/connect-maven:latest', args: '-v "$PWD":/usr/src/mymaven -w /usr/src/mymaven') {
                  sh 'cp /usr/share/maven/ref/settings.xml ~/.m2/settings.xml'
                  sh 'mvn -DaltDeploymentRepository=connect-nexus::default::https://nexus.connect.cd/repository/connect clean deploy'
                }
            }
        }
      }
    }

    stage('Build image') {
      steps {
        sh '''
          echo 'build image if not already buit by Maven plugin'
        '''
      }
    }

    stage('Test image') {
      steps {
        sh '''
          echo 'This is a test!'
        '''
      }
    }
  }
}
