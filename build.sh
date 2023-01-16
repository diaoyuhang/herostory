git pull
mvn clean
mvn package
docker rm -f game
docker rmi -f diaoyuhang/herostory:1
docker build -t diaoyuhang/herostory:1 .
docker run -p 8080:8080 -d  --name game diaoyuhang/herostory:1