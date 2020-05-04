String cron_job = BRANCH_NAME == "master" ? "@hourly" : ""
pipeline{
    agent any
    triggers { cron(cron_job) }
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

        stage('Deploy back-end'){
            steps{
                sh "docker build -t esp30-smartmirror-procunit python_src/."
                sh "docker tag esp30-smartmirror-procunit 192.168.160.99:5000/esp30-smartmirror-procunit"
                sh "docker push 192.168.160.99:5000/esp30-smartmirror-procunit"
            }
        }
        stage('Deploy on runtime'){
            steps{
                sshagent(credentials: ['esp30-ssh-deploy']){
                    sh "ssh -o 'StrictHostKeyChecking=no' -l esp30 192.168.160.103 curl -X GET http://192.168.160.99:8082/artifactory/libs-release-local/com/esp30/smartMirror/0.0.1.1/smartMirror-0.0.1.1.jar --output target/smartMirror-0.0.1.1.jar"
                    sh "scp /var/jenkins_home/workspace/es-2019-2020-P30-mb_master/Dockerfile esp30@192.168.160.103:/home/esp30"
                    sh "ssh -o 'StrictHostKeyChecking=no' -l esp30 192.168.160.103 ./stopCurrentSmartMirror"
                    sh "ssh -o 'StrictHostKeyChecking=no' -l esp30 192.168.160.103 docker build --target front-end -t esp30-smartmirror ."
                    sh "ssh -o 'StrictHostKeyChecking=no' -l esp30 192.168.160.103 docker pull 192.168.160.99:5000/esp30-smartmirror-procunit"
                    sh "ssh -o 'StrictHostKeyChecking=no' -l esp30 192.168.160.103 docker run -d -p 30010:8080 -p 30043:8443 --name esp30-smartmirror esp30-smartmirror"
                    sh "ssh -o 'StrictHostKeyChecking=no' -l esp30 192.168.160.103 docker run -d --name esp30-smartmirror-procunit 192.168.160.99:5000/esp30-smartmirror-procunit"
                }
            }
        }
    }
}