def version = "${env.BUILD_NUMBER}"
def REPONAME = "${scm.getUserRemoteConfigs()[0].getUrl()}"

node {

    stage('Clone sources') {
      git(
           url: "${REPONAME}",
           credentialsId: 'dft-buildbot-valtech',
           branch: "${BRANCH_NAME}"
        )
     }

    stage ('Gradle build') {
        // Set Environment Vairable if the CI env variable is set.
        script {
            env.SPRING_APPLICATION_JSON = '{"spring":{"datasource":{"url":"jdbc:postgresql://postgresql:5432/bb_dev?currentSchema=message"}}}'
        }
        try {
            sh './gradlew clean build bootJar createDatabaseSchemaZip artifactoryPublish artifactoryDeploy'
        }
        finally {
            junit '**/TEST*.xml'
        }
    }

    stage('SonarQube analysis') {

        withSonarQubeEnv('sonarqube') {
            def ver = readFile('VERSION').trim()
            echo "Version: " + ver
            // requires SonarQube Scanner for Gradle 2.1+
            // It's important to add --info because of SONARJNKNS-281
            sh "./gradlew --info sonarqube -Dsonar.projectName=message-service -Dsonar.projectVersion=${ver} -Dsonar.branch=${BRANCH_NAME}"
        }
    }

    stage("Quality Gate") {
        timeout(time: 5, unit: 'MINUTES') {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }
        }
    }
    stage("Acceptance Tests") {
        node('Functional') {
            git(
               url: "${REPONAME}",
               credentialsId: 'dft-buildbot-valtech',
               branch: "${BRANCH_NAME}"
            )

            timeout(time: 10, unit: 'MINUTES') {
                try {
                    sh 'bash -c "echo $PATH && cd acceptance-tests && ./run-regression.sh"'
                }
                finally {
                    archiveArtifacts allowEmptyArchive: true, artifacts: '**/docker.log'
                    junit '**/TEST*.xml'
                }
            }
        }
    }
}
