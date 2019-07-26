pipeline {

  agent { label 'k8s-dind-worker' }

  environment {

    registryUrl = "registry.hub.docker.com"
      registryKey = "aterefe/http4s"

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
         steps {
           sh "sbt docker:stage"
         }
       }

       stage('Build image') {
         /* This builds the actual image; synonymous to
          * docker build on the command line */

         steps {
           script {
             app = docker.build("${env.registryKey}", "./target/docker/stage/")
           }
         }
       }

       stage('Push image') {
         steps {
           /* Finally, we'll push the image */

           script {
             docker.withRegistry("https://${env.registryUrl}", "docker-hub-credentials") {
               app.push("${env.imageTag}")
             }
           }
         }
       }

       stage('Clean up space') {
         /* Clean up <none> images.
            Clean up images that've been pushed.*/
         steps {
           sh """docker images -f "dangling=true" -q | xargs -r docker rmi"""
             sh "docker rmi ${env.image}"
         }
       }


//     stage('Deploy') {
//       steps {
//         withKubeConfig([
//             credentialsId: 'minikube-crt',
//             serverUrl: 'https://192.168.99.100:8443',
//             namespace: 'http4s'
//         ]) {
//           sh "chmod +x run.sh"
//             sh "./run.sh ${registryKey}:${imageTag}"
//         }
//       }
//     }

    stage('Mortar fire') {

      agent {
        docker {
          image 'aterefe/mortar-kubectl:latest'
            args '--entrypoint=\'\''
        }
      }

      steps {
        withKubeConfig([
            credentialsId: 'minikube-crt',
            serverUrl: 'https://192.168.99.100:8443',
            namespace: 'http4s'
        ]) {
             sh "chmod +x mortar-run.sh"
             sh "./mortar-run.sh ${registryKey}:${imageTag}"
        }

      }
    }
  }
}
