We will keep the application logic very simplistic as we want to
concentrate on DevOps: automated build, test and integration locally and
on Jenkins.

Get the basic Tools
-------------------

Download and install Docker Desktop for Windows from
<https://docs.docker.com/docker-for-windows/install/>.

Download and install Maven for Windows from
<https://maven.apache.org/download.cgi>.

Get the Application
-------------------

Clone the git repository

 ```
 cd %homedrive%\\%homepath%\
 git clone https://github.ibm.com/devops4us/randomizer.git
 ````

A: Test the isolated UI (without Randomizer Service)
----------------------------------------------------

Execute the following in a DOS shell:

 ```
cd %homedrive%\\%homepath%\\randomizer\\ui\
mvn package tomee:run
 ```

Open this in your favorite browser:
<http://localhost:8080/randomizer-ui>. If you click "Get Random Number
from Server", you'll see -1 in the message box below. This is ok,
because the Randomizer Service is unavailable for now. We are just
testing the isolated UI.

B: Deploy and Test the integrated Application locally
-----------------------------------------------------

Execute the following in a DOS shell:

```
cd %homedrive%\\%homepath%\\randomizer\
docker-compose up -d
 ```

Enter this URL in your favorite browser:
<http://localhost:8080/randomizer-ui>. If you click "Get Random Number
from Server", a positive number should appear in the message box below..
If you still see a -1, the Randomizer Service is not functioning.

C: Run Local Integration Test
-----------------------------

To run the integration tests locally, execute the following in a DOS
shell:

```
cd %homedrive%\\%homepath%\\randomizer\
mvn -DPROFILE=integration -DRANDOMIZER\_UI\_NAME=localhost\^\
-DRANDOMIZER\_UI\_PORT=9090 verify
```

D: Run CI Pipeline with dockerized Jenkins 
-------------------------------------------

Build a customized Jenkins Docker Image
---------------------------------------

```
1 from jenkinsci/blueocean\
2 USER root\
3 RUN apk add py-pip\
4 RUN apk add python-dev libffi-dev openssl-dev gcc libc-dev make\
5 RUN pip install docker-compose\
6 RUN apk add maven\
7 RUN adduser jenkins root\
8 COPY settings.xml /usr/share/java/maven-3/conf
```

In the Dockerfile above, lines 3-5 install Docker Compose with the aid of
python. Line 6 installs Maven. In line 7, we add 'jenkins' to the group
of root users. This is necessary be able to access the docker daemon
within the Jenkins server. Line 8 copies the customized maven
configuration to Jenkins (this step is only necessary if you want to
customize Maven).

The Jenkins Blueocean image is available from Docker Hub:
<https://hub.docker.com/r/jenkinsci/blueocean/>. Look at this page:
<https://jenkins.io/doc/> for information on how to generally configure
Jenkins. For information on how to install Docker Compose on Jenkins
Blueocean, refer to this URL:
<https://wiki.alpinelinux.org/wiki/Docker>. For information on how to
manage users and groups in the Linux Alpine image, see
<https://stackoverflow.com/questions/49955097/how-do-i-add-a-user-when-im-using-alpine-as-a-base-image>.

The following DOS shell commands create local Docker image
jenkins-docker based on the above Dockerfile:

cd %homedrive%\\%homepath%\\randomizer\\Jenkins\
docker image build -t jenkins-docker -f Jenkins-Dockerfile .

To start the image jenkins-docker, run the following in a DOS shell:

```
docker network create integration\_net
 docker run\^
 -u jenkins\^
 -p 8080:8080 -p 50000:50000\^
 -v //var/run/docker.sock:/var/run/docker.sock\^
 -v c:/volumeshare/jenkins\_home:/var/jenkins\_home\^
 --name jenkins\^
 --network=\"integration\_net\"\^
 --rm\^
 -d\^
```
