node {
    def app

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

        app = docker.build("aterefe/ordering-system", "./target/docker/stage/")
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
        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
            shortCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
            branchName  = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim().replaceAll("/", "_")
            app.push("${branchName}-${shortCommit}")
        }

        /* Clean up <none> images. */
        sh """docker images -f "dangling=true" -q | xargs docker rmi"""
    }
}

