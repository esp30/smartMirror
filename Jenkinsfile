pipeline{
    agent any

    stages {
        stage('Build'){
            steps{
                sh 'docker build -t esp30-smartmirror .'
            }
        }
        stage('Test'){
            steps{
                sh 'mvn test'
            }
        }
        stage('Deploy'){
            steps{
                sh 'mvn deploy -s settings.xml'
            }
        }
    }
}