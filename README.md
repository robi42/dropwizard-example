# Dropwizard Example

## Running the Application

To check out the example application run the following commands (BTW, make sure to use JDK 7, and IntelliJ 12 FTW :) ).

 * To package the example run:

        mvn clean package

 * To set up the database run (and adapt config settings if applicable):

        psql -c 'create database dropwizard_helloworld'
        java -jar target/dropwizard-example.jar db migrate config.yml

 * To run the server run:

        java -jar target/dropwizard-example.jar server config.yml

 * Hints:

        curl -i http://localhost:8080/api/v1/hello-world
        curl -u j.doe@example.com:secret http://dw-example.herokuapp.com/api/v1/people | python -mjson.tool

 * IDE run config: app main class `com.example.helloworld.HelloWorldApplication` with args `server config.yml`


## Useful Resources

 * <http://postgresapp.com/>
 * <https://toolbelt.heroku.com/>
 * <http://dropwizard.codahale.com/>

This thing is based on: <https://github.com/codahale/dropwizard/tree/master/dropwizard-example>


## Notes

 * REST API resources are mounted at: `/api/v1/` (default credentials: `j.doe@example.com:secret`)
 * Admin tools are mounted at: `/admin` (default credentials: `admin:admin`)
 * Webapp assets are mounted at: `/app`
