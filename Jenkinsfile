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
        }

      }
      steps {
        sh 'ls'
      }
    }
  }
}