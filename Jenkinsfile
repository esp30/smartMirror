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
                sh 'docker run -d -p 30010:8080 -p 30020:4848 --name esp30-smartmirror esp30-smartmirror '
                sh 'mvn deploy -s settings.xml'
            }
        }
    }
}