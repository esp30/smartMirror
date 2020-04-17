pipeline{
    agent any

    stages {
        stage('Build'){
            steps{
                sshagent(credentials: ['esp30-ssh-deploy']){
                    sh "ssh -o 'StrictHostKeyChecking=no' -l esp30 192.168.160.103 uname -a"
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