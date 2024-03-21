echo "Enter your application name :"
read applicationName

echo " "
echo "Enter your application tag :"
read tagNumber


echo "================================================================================================================================"
echo "Building your application, please wait"
echo "================================================================================================================================"
cd ..

mvn clean install

cp ./target ./containerisation

cd ./containerisation

echo "================================================================================================================================"
echo "Containerising your application, please wait"
echo "================================================================================================================================"
docker build -t $applicationName:$tagNumber .

docker tag $applicationName:$tagNumber darshanbhasvar/$applicationName:$tagNumber

echo "================================================================================================================================"
echo "Pushing your application to docker registry, please wait"
echo "================================================================================================================================"
docker push darshanbhasvar/$applicationName:$tagNumber