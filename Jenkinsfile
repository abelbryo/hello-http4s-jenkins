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
          branches: [[name: '*/*']],
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
