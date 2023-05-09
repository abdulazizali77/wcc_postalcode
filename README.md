# Run
requires JDK 11

`mvn spring-boot:run`

`mvn test`

# auth
## `/list` EP is open to all

`curl -v -X GET "http://localhost:8080/list"`

## distance calc `/postcodedistance` is open to any auth user

`curl -v -u user1:user1Pass -X GET "http://localhost:8080/postcodedistance?pc1=AB16%206SZ&pc2=AB21%200AL"`

`curl -v -u admin1:admin1Pass -X GET "http://localhost:8080/postcodedistance?pc1=AB16%206SZ&pc2=AB21%200AL"`

## pass a unit ('km' 'm' 'cm' 'mm') param to get different values

`curl -v -u user1:user1Pass -X GET "http://localhost:8080/postcodedistance?pc1=AB16%206SZ&pc2=AB21%200AL&unit=cm"`

## POST and PUT  `/updatepostcode` `/addpostcode` is only open to admin user

`curl -v -u admin1:admin1Pass -H "Content-Type: application/json" -X PUT "localhost:8080/updatepostcode" -d '{"postcode":"AB21 0AL", "coordinate":{"longitude":1.01, "latitude":2.01} }'`

`curl -v -u admin1:admin1Pass -H "Content-Type: application/json" -X POST "localhost:8080/addpostcode" -d '{"postcode":"TEST TEST", "coordinate":{"longitude":1.01, "latitude":2.01} }'`


# i18n
## try other locales. Messages only seen with errors currently
## non-existent postcode

`curl -v -H 'Accept-Language: nl' -u user1:user1Pass -X GET "http://localhost:8080/postcodedistance?pc1=AB16%206SZ&pc2=DEADBEEF"`
## empty coordinates

`curl -v -H 'Accept-Language: nl' -u admin1:admin1Pass -H "Content-Type: application/json" -X PUT "localhost:8080/updatepostcode" -d '{"postcode":"AB21 0AL" }'`

`curl -v -H 'Accept-Language: nl' -u admin1:admin1Pass -H "Content-Type: application/json" -X POST "localhost:8080/addpostcode" -d '{"postcode":"TEST TEST" }'`

### didnt manage to integrate load test 