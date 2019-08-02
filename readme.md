We will keep the application logic very simplistic as we want to concentrate on DevOps: automated build, test and integration locally and
on Jenkins.

Get the basic Tools
-------------------

* Download and install Java SE Development Kit 8 from <https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>.
* Download and install Docker Desktop for Windows from <https://docs.docker.com/docker-for-windows/install/>.
* Download and install Maven for Windows from <https://maven.apache.org/download.cgi>.

Get the Application
-------------------

Clone the git repository

 ```
 cd %homedrive%\%homepath%\
 git clone https://github.ibm.com/devops4us/randomizer.git
 ````

The following picture shows the architecture of our simple example application `Randomizer`.

<img src="pic001.jpg" width="50%" height="50%"/>

There are two servers in the runtime architecture.
`randomizer-ui` is a HTML5 user interface application server based on the Vaadin Framework (<https://vaadin.com/docs/index.html>) and the Apache TomEE platform (<http://tomee.apache.org/apache-tomee.html>).
The application is accessed from a browser. 
It demonstrates a simple UI visualising the generation of random numbers.
The random generator itself is hosted on another server and is accessed from the user interface as HTTP/REST API.
The `randomizer-ui` application servier calls a rest API from `randomizer-service`, which is a rest service also running on top of TomEE.

**Development Environment:** The developer can start each server locally on her development machine (Windows 10 Laptop in my case).

**Test Environment:** In order to be globally deployable for integration test, the servers are each packaged as docker images based on the TomeEE docker image (see <https://docs.docker.com/samples/library/tomee/>).
Each server image runs in a separate docker container. 

Develop UI (without Service) and Service in Isolation
-------------------------------------------------------------------------------------

Server Development often starts with isolated servers being started locally. 
This is especially true for the user interface server - the user interface can be tested by mocking away the backend services and provide test data.
For our simplified sample, it is not necessary to mock the `randomizer-service`.
In our UI application, in class `RandomizerBean`, if the service is not available, the value `-1` is simply generated as random number. 

To start to UI service in isolation, execute the following in a DOS shell:

 ```
cd %homedrive%\%homepath%\randomizer\ui\
mvn package tomee:run
 ```

If you see in the DOS shell that the server started successfully, open this URL in your favorite browser: <http://localhost:9090/randomizer-ui>. 
If you click "Get Random Number from Server", you'll see -1 in the message box in your browser. 
This is ok, because the Randomizer Service is unavailable for now. 
We are still able to test the isolated UI, even if we get a dummy value instead of real random numbers.

Note that we called the goal `tomee:run` in the maven command line above. 
The reason why we can do this is the TomEE Maven Plugin (<http://tomee.apache.org/tomee-maven-plugin.html>). 
If you include this plugin in your `pom.xml`, as we did, the above command line will start a local TomEE server which hosts just your web application, `randomizer-ui`in our case.
See the TomEE documentation and the file `randomizer\ui\pom.xml`.    

Deploy and Test the integrated Application locally
-----------------------------------------------------

Execute the following in a DOS shell:

```
cd %homedrive%\%homepath%\randomizer\
docker-compose up -d
 ```

Enter this URL in your favorite browser: <http://localhost:8080/randomizer-ui>. 
If you click "Get Random Number from Server", a positive number should appear in a message box.
If you still see a -1, the Randomizer Service is not functioning.

Run Local Integration Test
-----------------------------

To run the integration tests locally, execute the following in a DOS shell:

```
cd %homedrive%\%homepath%\randomizer\
mvn -DPROFILE=integration -DRANDOMIZER_UI_NAME=localhost\^\
-DRANDOMIZER_UI_PORT=9090 verify
```

Build a customized Jenkins Docker Image
---------------------------------------
The follofing ist the content of file `Jenkins\Jenkins-Dockerfile`. It is used to build the Jenkins Docker image.

```
1 from jenkinsci/blueocean
2 USER root
3 RUN apk add py-pip
4 RUN apk add python-dev libffi-dev openssl-dev gcc libc-dev make
5 RUN pip install docker-compose
6 RUN apk add maven
7 RUN adduser jenkins root
8 COPY settings.xml /usr/share/java/maven-3/conf
```

Lines 3-5 install Docker Compose with the aid of python. 
Line 6 installs Maven. 
In line 7, we add 'jenkins' to the group of root users. 
This is necessary be able to access the docker daemon within the Jenkins server. 
Line 8 copies the customized maven configuration to Jenkins (this step is only necessary if you want to customize Maven).

The Jenkins Blueocean image is available from Docker Hub: <https://hub.docker.com/r/jenkinsci/blueocean/>. 
Look at this page: <https://jenkins.io/doc/> for information on how to generally configure Jenkins. 
For information on how to install Docker Compose on Jenkins Blueocean, refer to this URL: <https://wiki.alpinelinux.org/wiki/Docker>. 
For information on how to manage users and groups in the Linux Alpine image, see <https://stackoverflow.com/questions/49955097/how-do-i-add-a-user-when-im-using-alpine-as-a-base-image>.

The following DOS shell commands create local Docker image
jenkins-docker based on the above Dockerfile:

```
cd %homedrive%\\%homepath%\\randomizer\\Jenkins\
docker image build -t jenkins-docker -f Jenkins-Dockerfile .
```

To start jenkins from the image the image `jenkins` image, run the following in a DOS shell:

```
docker network create integration_net
 docker run^
 -u jenkins^
 -p 8080:8080 -p 50000:50000\^
 -v //var/run/docker.sock:/var/run/docker.sock^
 -v c:/volumeshare/jenkins_home:/var/jenkins_home^
 --name jenkins^
 --network="integration_net"^
 --rm^
 -d^
 jenkins-docker
```
