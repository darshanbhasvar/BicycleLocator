echo "Enter your application name :"
read applicationName

echo "\nEnter your application tag :"
read tagNumber

echo "\nEnter your docker username :"
read baseRegistry


echo "\n================================================================================================================================"
echo "Building your application, please wait"
echo "================================================================================================================================\n"
cd ..

mvn clean install

cp ./target ./containerisation

cd ./containerisation

echo "\n================================================================================================================================"
echo "Containerising your application, please wait"
echo "================================================================================================================================\n"
docker build -t $applicationName:$tagNumber .

docker tag $applicationName:$tagNumber darshanbhasvar/$applicationName:$tagNumber

echo "\n================================================================================================================================"
echo "Pushing your application to docker registry, please wait"
echo "================================================================================================================================\n"
docker push $baseRegistry/$applicationName:$tagNumber