docker network create integration_net 
mvn -DPROFILE=integration -DRANDOMIZER_UI_NAME=localhost -DRANDOMIZER_UI_PORT=9090 verify