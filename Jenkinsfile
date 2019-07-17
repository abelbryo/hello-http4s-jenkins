pipeline {

  agent { label 'k8s-dind-worker' }

  environment {
    DOCKER_HOST = 'tcp://localhost:2375'

    registryUrl = "registry.hub.docker.com"
    registryKey = "aterefe/ordering-system"

    shortCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
    branchName  = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim().replaceAll("/", "_")

    imageRepo = "${registryUrl}/${registryKey}"
    imageTag = "${branchName}-${shortCommit}"
    image = "${imageRepo}:${imageTag}"

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


    stage('sbt docker:stage') {
        sh "sbt docker:stage"
    }

    stage('Build image') {
        /* This builds the actual image; synonymous to
         * docker build on the command line */

        app = docker.build("${registryKey}", "./target/docker/stage/")
    }

    stage('Test image') {
        /* Ideally, we would run a test framework against our image.
         * For this example, we're using a Volkswagen-type approach ;-) */

        app.inside {
            sh 'echo "Tests passed"'
        }
    }

    stage('Push image') {
        /* Finally, we'll push the image */
        docker.withRegistry("https://${registryUrl}", "docker-hub-credentials") {
            app.push("${imageTag}")
        }

    }

    stage('Clean up space') {
        /* Clean up <none> images.
           Clean up images that've been pushed.*/
        sh """docker images -f "dangling=true" -q | xargs -r docker rmi"""
        sh "docker rmi ${image}"
    }

  }
}
