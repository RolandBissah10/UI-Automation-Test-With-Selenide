pipeline {
    agent {
        docker {
            image 'maven:3.9.6-eclipse-temurin-17'
            args '--shm-size=2g -v /dev/shm:/dev/shm'
        }
    }

    parameters {
        choice(
            name: 'SUITE',
            choices: ['smoke', 'regression', 'all'],
            description: 'Test suite to execute'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox'],
            description: 'Browser to run tests on'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode'
        )
    }

    environment {
        ALLURE_RESULTS = 'target/allure-results'
        SCREENSHOTS    = 'target/selenide-screenshots'
        MAVEN_OPTS     = '-Xmx2g'
    }

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Branch: ${env.BRANCH_NAME} | Suite: ${params.SUITE} | Browser: ${params.BROWSER}"
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'mvn dependency:go-offline -B --no-transfer-progress'
            }
        }

        stage('Install Chrome') {
            when {
                expression { params.BROWSER == 'chrome' }
            }
            steps {
                sh '''
                    apt-get update -qq
                    apt-get install -y -qq chromium chromium-driver
                    chromium --version
                '''
            }
        }

        stage('Install Firefox') {
            when {
                expression { params.BROWSER == 'firefox' }
            }
            steps {
                sh '''
                    apt-get update -qq
                    apt-get install -y -qq firefox-esr
                    firefox --version
                '''
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def profile = params.SUITE == 'all' ? 'all' : params.SUITE
                    sh """
                        mvn test -P${profile} \\
                          -Dbrowser=${params.BROWSER} \\
                          -Dheadless=${params.HEADLESS} \\
                          -B --no-transfer-progress
                    """
                }
            }
            post {
                always {
                    // Archive screenshots
                    archiveArtifacts(
                        artifacts: "${env.SCREENSHOTS}/**/*.png",
                        allowEmptyArchive: true
                    )
                }
            }
        }
    }

    post {
        always {
            script {
                // Generate Allure report
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: "${env.ALLURE_RESULTS}"]]
                ])
            }
        }
        success {
            echo "✅ Tests passed: ${params.SUITE} suite on ${params.BROWSER}"
        }
        failure {
            echo "❌ Tests failed — check Allure report and screenshots above"
            // Uncomment to send Slack/email notifications:
            // slackSend channel: '#qa-alerts', color: 'danger',
            //           message: "UI Tests FAILED on ${env.BRANCH_NAME}"
        }
        cleanup {
            cleanWs()
        }
    }
}
