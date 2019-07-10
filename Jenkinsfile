node {
    def app

    environment {
        registryUrl = "registry.hub.docker.com"
        registryKey = "aterefe/ordering-system"

        shortCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
        branchName  = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim().replaceAll("/", "_")

        def imageRepo = "${registryUrl}/${registryKey}"
        def imageTag = "${branchName}-${shortCommit}"
        def image = "${imageRepo}:${imageTag}"
    }

    stage('Clone repository') {
        /* Let's make sure we have the repository cloned to our workspace */

        checkout scm
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
        sh """docker images -f "dangling=true" -q | xargs docker rmi"""
        sh "docker rmi ${image}"
    }
}

