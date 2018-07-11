def version = "${env.BUILD_NUMBER}"
def REPONAME = "${scm.getUserRemoteConfigs()[0].getUrl()}"

node {

    stage('Clone sources') {
      git(
           url: "${REPONAME}",
           credentialsId: 'username***REMOVED***-github-automation-uk-gov-dft',
           branch: "${BRANCH_NAME}"
        )
     }

    stage ('Gradle build') {
        try {
            sh './gradlew clean build bootJar artifactoryPublish artifactoryDeploy'
        }
        finally {
            junit '**/TEST*.xml'
        }
    }

    stage ('Run acceptance tests') {
        try {
            sh './gradlew bootRun acceptanceTests'
        }
        finally {
            // junit '**/TEST*.xml'
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

}
