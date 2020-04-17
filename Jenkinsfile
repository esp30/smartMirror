pipeline{
    agent any

    stages {
        stage('Build'){
            steps{
                sshagent(credentials: ['esp30-ssh-deploy']){
                    sh 'cat something'
                }
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
                sh 'docker stop esp30-smartmirror'
                sh 'docker rm esp30-smartmirror'
                sh 'mvn deploy -s settings.xml'
            }
        }
    }
}