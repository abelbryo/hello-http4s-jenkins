pipelineJob('http4s-jenkins') {
  description('Build scala project')
    triggers {
      scm('*/5 * * * *')
    }

  definition {
    cpsScm {
      scm {

        git {
          remote {
            name('origin')
              url('git@github.com:abelbryo/hello-http4s-jenkins.git')
          }

          branches("*")

            extensions {
              choosingStrategy {
                alternative()
              }
            }
        }
      }

      scriptPath('Jenkinsfile')
    }
  }
}
