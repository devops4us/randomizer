docker network create integration_net
docker run --rm -d -p 8080:8080 -p 50000:50000 -v //var/run/docker.sock:/var/run/docker.sock -v c:\\volumeshare\\jenkins_home:/var/jenkins_home --name jenkins --network="integration_net" jenkins-docker