pipeline {
  agent any
  stages {
    stage('integration-test') {
      steps {
        sh 'mvn -v -DPROFILE=integration -DRANDOMIZER_UI_NAME=randomizer-ui -DRANDOMIZER_UI_PORT=8080 verify'
      }
    }
  }
}