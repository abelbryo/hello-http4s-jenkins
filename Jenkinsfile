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

        imageUploadUrl = "https://${registryUrl}/${registryKey}"

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
                withDockerServer(uri: env.DOCKER_HOST, credentialsId: "") {
                    withDockerRegistery(credentialId: '', url: env.imageUploadUrl) {
                        app = docker.build("${env.registryKey}", "./target/docker/stage/")
                    }
                }
            }
        }


        stage('Push image') {
            steps {
                /* Finally, we'll push the image */
                docker.withRegistry("https://${env.registryUrl}", "docker-hub-credentials") {
                    app.push("${env.imageTag}")
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

    }
}
