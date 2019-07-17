pipelineJob('http4s-jenkins') {
  description('Build scala project')
    triggers {
      scm('*/5 * * * *')
    }

  definition {
    cpsScm {
      scm {

        git('git@github.com:abelbryo/hello-http4s-jenkins.git') {
          node -> node / extensions()
        }
      }

      scriptPath('Jenkinsfile')
    }
  }
}
