def didTimeout = false

node {
    stage ("Build") {
        echo '*** Build Starting ***'
        openshiftBuild bldCfg: 'app-registry-mobilesso', buildName: '', checkForTriggeredDeployments: 'false', commitID: '', namespace: '', showBuildLogs: 'false', verbose: 'false', waitTime: ''
        openshiftVerifyBuild apiURL: 'https://openshift.default.svc.cluster.local', authToken: '', bldCfg: 'app-registry-mobilesso', checkForTriggeredDeployments: 'false', namespace: '', verbose: 'false'
        echo '*** Build Complete ***'
    }

    stage ("Deploy and Verify in Development Env") {
        echo '*** Deployment Starting ***'
        openshiftDeploy apiURL: 'https://openshift.default.svc.cluster.local', authToken: '', depCfg: 'app-registry-mobilesso', namespace: '', verbose: 'false', waitTime: ''
        openshiftVerifyDeployment apiURL: 'https://openshift.default.svc.cluster.local', authToken: '', depCfg: 'app-registry-mobilesso', namespace: '', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'false', waitTime: ''
        echo '*** Deployment Complete ***'

        echo '*** Service Verification Starting ***'
        openshiftVerifyService apiURL: 'https://openshift.default.svc.cluster.local', authToken: '', namespace: '2d95-d', svcName: 'app-registry-mobilesso', verbose: 'false'
        echo '*** Service Verification Complete ***'
        openshiftTag(srcStream: 'app-registry-mobilesso', srcTag: 'latest', destStream: 'app-registry-mobilesso', destTag: 'testready')
    }

    stage ("Deploy and Test in Val Env") {
        echo "*** Deploy testready build in 2d95-v project  ***"
        openshiftDeploy apiURL: 'https://openshift.default.svc.cluster.local', authToken: '', depCfg: 'app-registry-mobilesso', namespace: '2d95-v', verbose: 'false', waitTime: ''
        openshiftVerifyDeployment apiURL: 'https://openshift.default.svc.cluster.local', authToken: '', depCfg: 'app-registry-mobilesso', namespace: '2d95-v', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'false'
    }
    stage ("Promote to Production Env") {
        echo '*** Waiting for Input ***'
        try {
            timeout(time: 40, unit: 'SECONDS') {
                input 'Should we deploy to Production?'
            }
        } catch(err) { // timeout reached or input false
            didTimeout = true
        }

        if (didTimeout) {
            echo "Timeout reached or input abort"
            echo "Abort deploy to production"
        } else {
            openshiftTag(srcStream: 'app-registry-mobilesso', srcTag: 'testready', destStream: 'app-registry-mobilesso', destTag: 'prodready')
            echo '*** Deploying to Production ***'
            openshiftDeploy apiURL: 'https://openshift.default.svc.cluster.local', authToken: '', depCfg: 'app-registry-mobilesso', namespace: '2d95-p', verbose: 'false', waitTime: ''
            openshiftVerifyDeployment apiURL: 'https://openshift.default.svc.cluster.local', authToken: '', depCfg: 'app-registry-mobilesso', namespace: '2d95-p', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'false'
        }
    }
}
