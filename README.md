#  RTj: a Java framework for detecting and refactoring rotten green test cases


If you use RTj, please cite our paper:


* *RTj: a Java framework for detecting and refactoring rotten green testcases*. Matias Martinez, Anne Etien, Stéphane Ducasse, and Christopher Fuhrman. 2019.


## Prerequisites

RTj needs to run on a Java Virtual Machine 8. 
Moreover, Maven must be installed on your machine and accessible from command line (test executing `mvn` on the console).
RTj 

## Installation

RTj depends on Astor project. So first, it's necessary to clone [Astor](https://github.com/SpoonLabs/astor) , and then to build it using maven: (mvn install -DskipTests=true)

Then, after cloning RTj project, go to the cloned project and run command `mvn package`. If the build sucessfully finishes, it should be the jar  `RTj-0.0.1-SNAPSHOT-jar-with-dependencies.jar` on folder `target`. (you can rename it to rtj.jar for simplicity).

Before running the test cases, you need to compile the project under test.
For that, go to `examples/rt-project` and execute `mvn test`.

RTj uses the Maven plugin [project-info-maven-plugin](https://github.com/tdurieux/project-info-maven-plugin) to get the information of the Maven projects to analyze.  So, for installing project-info-maven-plugin please clone the that repo from  https://github.com/tdurieux/project-info-maven-plugin, and then run `mvn install`. Note that it does not work if the installation is done as follows: `mvn org.apache.maven.plugins:maven-dependency-plugin:3.1.1:get -DremoteRepositories=https://tdurieux.github.io/maven-repository/snapshots/ -Dartifact=com.github.tdurieux:project-config-maven-plugin:1.0-SNAPSHOT`


## Execution

Before running RTj, please compile the project under test (e.g., mvn compile)  and execute the test (e.g., mvn test). 
It's necessary that all the dependencies of the project under test be downloaded on your machine (mvn compile will do that) before starting the analysis.

The current implementation of RTj analyzes JUNIT 4.x test cases.
For using other testing framework, please use the extension point [Override the Test execution](#override-the-test-execution)

There are two ways of executing RTj. 
The first one targets to Maven projects: RTj automatically extracts the properties of the (maven) project under analysis (e.g., dependencies).
The Second one targets other types of projects, and the task of extract project's information (such as dependencies) and passing them to RTj must be done by the user.


### Analysis of Maven project


This mode needs have installed the maven plugin [project-info-maven-plugin](https://github.com/tdurieux/project-info-maven-plugin).
This plugin resolves: the dependencies (jars) of the project under analysis, the folders with source code, etc. 
Then, launch RTj using this command:
```
java -cp /<absolute_path_to_jar>/rt.jar   fr.inria.jtanre.rt.RtMain   \
-location  <location_of_project to_analyzer> \
-out  <Folder_Output> 
``` 

This command will produces an output similar to this one:

```
End analysis Rotten green tests on project MyProject 
Number all test cases analyzed 272 from 111 test classes

Bad news! 6 Rotten green tests found

Summary of Rotten green tests:
Number Context_Dep_Rotten_Assertions: 0
Number Context Dep Rotten Helpers Call: 0
Number Context Dep Rotten Helpers Assertion: 3
Number Rotten Skip: 0
Number Rotten Missed Fail: 1
Number Full Rotten Test: 2
Detailed results saved on file: <Folder_Output>/rt_MyProject.json
```

The file `<Folder_Output>/rt_MyProject.json` is a JSON with all the information related to the Rotten green test found (Name of the test, location and code of assertions/helpers not executed, etc).
For instance, this portion of the JSON shows as example, the information related to  test `BucketLeapArrayTest.testListWindowsNewBucket`, which was labeled as `Fully Rotten`. 

```
{
	 "test_class": "com.alibaba.csp.sentinel.slots.statistic.metric.BucketLeapArrayTest",
      "test_name": "testListWindowsNewBucket",
      "github_link": "https://github.com/alibaba/Sentinel/tree/103fa307e57de1b6660a8a004e9d8f18283b18c9/sentinel-core/src/test/java/com/alibaba/csp/sentinel/slots/statistic/metric/BucketLeapArrayTest.java#L191", 
      "rotten_types_summary": [
        "Full_Rotten_Test_Rotten_Assertions"
      ],
      "rotten_info": [
        {
          "code": "org.junit.Assert.assertTrue(windowWraps.contains(wrap))",
          "line": 209,
          "path": "sentinel-core/src/test/java/com/alibaba/csp/sentinel/slots/statistic/metric/BucketLeapArrayTest.java",
          "other_branch_with_assert_executed": false,
          "github_link": "https://github.com/alibaba/Sentinel/tree/103fa307e57de1b6660a8a004e9d8f18283b18c9/sentinel-core/src/test/java/com/alibaba/csp/sentinel/slots/statistic/metric/BucketLeapArrayTest.java#L209",
          "type": "Full_Rotten_Test_Rotten_Assertions"
        }
      ],
  
 ```
 
  
By default, RTj only prints on standard output the summary of rotten found (e.g., 2 Fully Rotten), but does not print the information of them (which can be found in the JSON). 
RTj provides an option `-printrottentest` to print on the screen the rotten information.

### Analysis of Any project project

RTj can analyze any kind of Java project, not necessary Maven project. 
For that, it will be necessary to pass (via command line arguments) to RTj additional information about the project under analysis.
As by default, RTj calls [project-info-maven-plugin](https://github.com/tdurieux/project-info-maven-plugin),  to disable this feature, pass the argument `autoconfigure false`.
Then, use the following arguments to pass the information related to the project to analyze.

    -location "absolute location of the project to analyze" 

    -dependencies "folder with the dependencies of the application to analyze" 

    -javacompliancelevel "compliance level of source code e.g. 5"
    
    -srcjavafolder "source code folder"
    
    -srctestfolder "test source code folder"
    
    -binjavafolder "class folder"
    
    -bintestfolder "test class folder" 




## Extension

RTj provides extension points for adding new functionality.

### Add new Test Analyzer 

First, create a class that implements interface `TestAnalyzer`, which has 4 methods:
`findElements`, `classifyElements`, `labelTest`, and `refactor`.

Then, add this class (the bytecode) to the classpath (e.g., `java -cp /<absolute_path_to_jar>/rt.jar:myNewAnalyzwer.class`).

Finally, passing the canonical class name of the new Analyzer using argument `-analyzers`.
RTj will then load that class and use them to analyze the test cases.




### Add new Output

First, create a class that implements interface `RtOutput`, which has 1 method
`generateOutput` that receives as input the tests's classification, the refactored test (if any) and produces an output (e.g., store the information of JSON, Print on the standard out, etc). 

Then, add this class (the bytecode) to the classpath (e.g., `java -cp /<absolute_path_to_jar>/rt.jar:myNewOutput.class`).

Finally, passing the canonical name of the new Output using argument `-outputs`.
RTj will then load that class and use them to export the results.


### Override the Test execution

If you want that RTj analyzes test cases written in a particular testing framework F (different than junit 4.x) you need to override this extension point and to create an extension that is able to a) detect test from F, and b) run those test cases.

First, create a class that implements interface `TestCaseExecutor`, which has 2 methods: 
`runTests` and `findTestCasesToExecute`. That class will implement the dynamic analysis module: it runs the instrumented test cases.

Then, add this class (the bytecode) to the classpath (e.g., `java -cp /<absolute_path_to_jar>/rt.jar:myNewTestRunner.class`).

Finally, passing the canonical class name of the new Test Executor using argument `-testexecutor`.
RTj will then load that class and use them to do the dynamic analysis (i.e., , run instrumented test cases)


### Override the model generation 


# Contact

Matias Martinez (Université Polytechnique Hauts-de-France)
www.martinezmatias.com


