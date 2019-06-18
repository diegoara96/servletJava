pipeline {
  agent any
  stages {
    stage('build') {
      parallel {
        stage('server') {
          agent {
            docker {
              image 'maven:3.5-jdk-8-slim'
            }

          }
          steps {
            sh '''echo "test"
mvn -version
mkdir -p target
touch "target/server.war"'''
            stash(name: 'server', includes: '**/*.war')
          }
        }
        stage('client') {
          steps {
            sh 'echo "test"'
          }
        }
      }
    }
    stage('ffff') {
      steps {
        sh 'ls'
      }
    }
  }
}