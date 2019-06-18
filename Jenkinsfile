pipeline {
  agent any
  stages {
    stage('test') {
      steps {
        echo 'testing'
      }
    }
    stage('build image') {
      agent {
        docker {
          image 'ubuntu'
          args '-it'
        }

      }
      steps {
        sh '''mkdir test
touch file.txt
ls'''
      }
    }
  }
}