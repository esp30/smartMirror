pipeline{
    agent any

    stages {
        stage('Test'){
            steps{
                sshagent(credentials: ['esp30-ssh-deploy']){
                    sh 'echo "testing ssh connection"'
                    sh "ssh -o 'StrictHostKeyChecking=no' -l esp30 192.168.160.103 uname -a"
                }
            }
        }
        stage('Deploy to Artifactory'){
            steps{
                sh 'mvn deploy -s settings.xml -DskipTests'
            }
        }
        stage('Deploy on runtime'){
            steps{
                sshagent(credentials: ['esp30-ssh-deploy']){
                    sh 'mvn clean package -DskipTests'
                    sh "scp /var/jenkins_home/workspace/es-2019-2020-P30/target/smartMirror-0.0.1.1.war esp30@192.168.160.103:/home/esp30/target"
                }
            }
        }
    }
}