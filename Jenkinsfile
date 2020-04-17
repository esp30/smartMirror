pipeline{
    agent any

    stages {
        stage('Build'){
            steps{
                sh 'mvn clean package'
                sh 'docker build -t esp30-smartMirror .'
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