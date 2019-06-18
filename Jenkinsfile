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
          args '--privileged=true'
        }

      }
      steps {
        sh 'ls'
      }
    }
  }
}