//#!/usr/bin/env groovy    //表示该文件采用Groovy语言格式
/**
        * Sample Jenkinsfile for Maven project sprBootGrodemo
        * from https://github.com/jkchen-hub/sprBootGrodemo/sprBootGrodemo.groovy
        * by jkchen 
 */
node {
   def mvnHome
   stage('Preparation') { // for display purposes
      // Get Jenkinsfile from a GitHub repository
      git 'https://github.com/jkchen-hub/sprBootGrodemo.git'
      // Get the Maven tool.         
      mvnHome = tool 'apache server maven'
   }
   stage('Build') {
      // Run the maven build
      withEnv(["MVN_HOME=$mvnHome"]) {
         if (isUnix()) {
            sh '"$MVN_HOME/bin/mvn" -Dmaven.test.failure.ignore clean compile package'   //忽略测试结果直接编译和打包
         } else {
            bat(/"%MVN_HOME%\bin\mvn" -Dmaven.test.failure.ignore clean compile package/)
         }
      }
   }
   stage('Results') {
      archiveArtifacts 'target/*.jar'
   }
}
