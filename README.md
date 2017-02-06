API-Engine: API Management Services based on Mashape's KONG gateway
===================================================================
Author: Michallis Pashidis, Guillaume Vandecasteele
Technologies: EAR, JPA, Java EE, CDI
Summary: API Engine
Target Project: Digipolis API Engine
Source: <https://bitbucket.org/Trust1T/digi-api-engine-javaee>

Kong version
------------
Kong 0.9.3

Build
-----
In order to build the project, the ejb module contains the necessary profiles. Example maven build for production environment:
`mvn clean install`

Example build for dev - skipping tests can be done:
`clean install -DskipTests=true`

You can customize the artifact name adding a 'targetenv' property for building:
`clean install -DskipTests=true -Dtargetenv=t1t`

You can customize artifact and define profile at the same time, for example:
`clean install -DskipTests=true -Dtargetenv=dev`

Build and prepare a docker container
`clean install -DskipTests=true -Pdocker`

If you want to build an API-Engine with specific profile, but prepare it for a docker container, you can combine profiles:
`clean install -DskipTests=true -Pt1t-dev,docker`

Docker - Demo
-------------
TODO provision docker through repository
TODO docker compose for demo setup

Docker - Development environment
--------------------------------
In the target folder of API-Engine-distro you can find 2 artifacts:
- docker
- docker-postgres

Off course first you should provide a postgres container, a kong container, and after that run an api-engine container linked to the postgres container.
### Prerequisites
Install docker on your machine

* [Mac OSX](https://docs.docker.com/engine/installation/mac/)
* [Windows](https://docs.docker.com/engine/installation/windows/)

### Create a new docker-machine (virtual image)
```sh
$ docker-machine create --driver virtualbox apiengine
```
### Login to your docker-machine
```sh
$ docker-machine ls
$ docker-machine env apiengine
$ eval $(docker-machine env apiengine)
```

### More information
More information, related to docker, can be found in the README.md file of the API-Engine-disto module.

### Pull a Cassandra container
```sh
$ docker pull cassandra:2.2.4
``` 

### Pull a Kong container
```sh
$ docker pull mashape/kong
```

### Start Cassandra
```sh
$ docker run -p 9042:9042 -d --name cassandra cassandra:2.2.4
```

### Start and link Kong to Cassandra
```sh
$ docker run -d --name kong \
            --link cassandra:cassandra \
            -p 8000:8000 \
            -p 8443:8443 \
            -p 8001:8001 \
            -p 7946:7946 \
            -p 7946:7946/udp \
            mashape/kong
```
```sh
$ docker run -d --name kong --link cassandra:cassandra -p 8000:8000 -p 8443:8443 -p 8001:8001 -p 7946:7946 -p 7946:7946/udp mashape/kong
```

### Build docker container
In the API-Engine-distro target folder, go to the docker-postgres folder and execute:
```sh
$ docker build -t api-db .
``` 

### See images
Verify the images has been created
```sh
$ docker images
```
Remark: You'll see an image for api-db and for postgres (where it depends on)

### Run docker container
Run the Postgres container:
```sh
$ docker run -p 5432:5432 --name api-engine-db -t api-db
``` 
If you have already a container with this name you can easily remove it with:
```sh
$ docker rm api-engine-db
```
or inspect more info
```sh
$ docker inspect api-engine-db
```

### Verify running docker containers
```sh
$ docker ps
```

### Build Apiengine in target folder
In the API-Engine-distro target folder, go to the docker folder and execute:
```sh
$ docker build -t api-engine .
```

### Run and connect API Engine to the running postgres instance
```sh
$ docker run -p 8080:8080 -p 9990:9990  --name api-engine-inst1 --link api-engine-db:postgres --link kong:kong -d api-engine
```

### Verify your ip of the running machine
```sh
$ docker-machine ip apiengine
```
By default the management console is enabled.
Management console on IP:9990 username:admin password:admin123!
Web API on IP:8080/API-Engine-web
Auth API on IP:8080/API-Engine-auth

### remove a running container and its image
```sh
$ docker stop api-engine-inst1
$ docker rm api-engine-inst1
$ docker rmi api-engine
```
Using the API Marketplace
-------------------------
Start your Kong container by adding your localhost IP:
```sh
$ docker run --add-host=localhost:84.198.85.191 -d --name konglocal --link cassandra:cassandra -p 8000:8000 -p 8443:8443 -p 8001:8001 -p 7946:7946 -p 7946:7946/udp mashape/kong 
```

See [Docker-info](https://docs.docker.com/engine/reference/commandline/run/#add-entries-to-container-hosts-file-add-host)

Release Notes - Trust1Gateway - Version T1G-APIM-v0.9.5
-------------------------------------------------------

## Bug

*   [[T1G-294](https://trust1t.atlassian.net/browse/T1G-294)] - Syncing does not take into account whether or not an application already has an oauth credential id
*   [[T1G-295](https://trust1t.atlassian.net/browse/T1G-295)] - Syncing users and applications does not take into account existing JWT keys and secrets

Release Notes - APIe - Version API/SDK engine 0.8.0
---------------------------------------------------

## Bug

*   [[APIE-827](https://jira.antwerpen.be/browse/APIE-827)] - missing url forward(redirect) in store and pub
*   [[APIE-841](https://jira.antwerpen.be/browse/APIE-841)] - IP restriction policy not working as expected !
*   [[APIE-923](https://jira.antwerpen.be/browse/APIE-923)] - cleanup_double_organizations_acc
*   [[APIE-924](https://jira.antwerpen.be/browse/APIE-924)] - administration screen accessible without admin rights
*   [[APIE-926](https://jira.antwerpen.be/browse/APIE-926)] - cannot Publish V4orV5 outputgenerator
*   [[APIE-975](https://jira.antwerpen.be/browse/APIE-975)] - members toevoegen via e-mail lukt niet
*   [[APIE-994](https://jira.antwerpen.be/browse/APIE-994)] - Een gebruiker kan meermaals worden toegevoegd aan een organisatie met als gevolg dat er telkens ook een mail wordt gestuurd

## Story

*   [[APIE-429](https://jira.antwerpen.be/browse/APIE-429)] - Verschillende versies voor services applicaties weergeven in dashboard
*   [[APIE-533](https://jira.antwerpen.be/browse/APIE-533)] - As a developer I want to sync Cassandra cluster with data from the API Engine backend - Postgres (cassandra re-initialisation)
*   [[APIE-534](https://jira.antwerpen.be/browse/APIE-534)] - As a support desk I want to verify the status of the service and it's dependency using a GET request on the API
*   [[APIE-548](https://jira.antwerpen.be/browse/APIE-548)] - Mogelijk maken om contracten toe te voegen aan al geregistreerde applicaties
*   [[APIE-708](https://jira.antwerpen.be/browse/APIE-708)] - Proxy for API-GW for external access
*   [[APIE-727](https://jira.antwerpen.be/browse/APIE-727)] - As a visitor I have an option to register for a new a-stad user
*   [[APIE-728](https://jira.antwerpen.be/browse/APIE-728)] - As a visitor I can login with my a-stad user or Dgpls account
*   [[APIE-730](https://jira.antwerpen.be/browse/APIE-730)] - As a logged in user I am guided into the creation of an organisation and app
*   [[APIE-752](https://jira.antwerpen.be/browse/APIE-752)] - Email validation and resend validation mail functionality
*   [[APIE-837](https://jira.antwerpen.be/browse/APIE-837)] - Verwijderen Plan in een organisatie
*   [[APIE-865](https://jira.antwerpen.be/browse/APIE-865)] - Sync PostgresDB -> Cassandra
*   [[APIE-908](https://jira.antwerpen.be/browse/APIE-908)] - Upgrade Swagger spec to v2.1.4
*   [[APIE-945](https://jira.antwerpen.be/browse/APIE-945)] - uri niet duidelijk leesbaar
*   [[APIE-969](https://jira.antwerpen.be/browse/APIE-969)] - As a developer I don't want to see private orgs when 'search orgs'
*   [[APIE-986](https://jira.antwerpen.be/browse/APIE-986)] - UI tweaks for new layout

Release Notes - APIe - Version API/SDK engine 0.7.1
---------------------------------------------------

## Test

*   [[APIE-294](https://jira.antwerpen.be/browse/APIE-294)] - [TEST] : Verify Bug fixes - APIe

## Bug

*   [[APIE-97](https://jira.antwerpen.be/browse/APIE-97)] - Notification setting is not saved correctly by the Marketplace & Publisher applications.
*   [[APIE-403](https://jira.antwerpen.be/browse/APIE-403)] - API store : Request membership button voor een organisatie werkt niet
*   [[APIE-414](https://jira.antwerpen.be/browse/APIE-414)] - Add Policy - CORS allows incorrect values in form.
*   [[APIE-415](https://jira.antwerpen.be/browse/APIE-415)] - OAuth2 policy form accepts negative "Token Expiration" values.
*   [[APIE-418](https://jira.antwerpen.be/browse/APIE-418)] - New Announcement fails to create an announcement and an Unexpected error is thrown.
*   [[APIE-421](https://jira.antwerpen.be/browse/APIE-421)] - Deleting Applications/Service does not always work correctly
*   [[APIE-423](https://jira.antwerpen.be/browse/APIE-423)] - Creation of Tickets with redundant names possible.
*   [[APIE-450](https://jira.antwerpen.be/browse/APIE-450)] - API publisher toegevoegde member (via userid) krijgt organisatie niet te zien
*   [[APIE-490](https://jira.antwerpen.be/browse/APIE-490)] - A policy with empty values is possible. This leads to malfunction and service cannot be published.
*   [[APIE-521](https://jira.antwerpen.be/browse/APIE-521)] - Application cannot be deleted.
*   [[APIE-746](https://jira.antwerpen.be/browse/APIE-746)] - Hidden-External marketplace service is available on marketplace.
*   [[APIE-747](https://jira.antwerpen.be/browse/APIE-747)] - Something gone wrong page shown when contract is created with a deleted service.
*   [[APIE-763](https://jira.antwerpen.be/browse/APIE-763)] - creation of new version not possible
*   [[APIE-774](https://jira.antwerpen.be/browse/APIE-774)] - OAuth client credential demands scope which is useless
*   [[APIE-783](https://jira.antwerpen.be/browse/APIE-783)] - Contract cannot be created via marketplace application link.(see video)
*   [[APIE-796](https://jira.antwerpen.be/browse/APIE-796)] - developer kan een plan aanmaken maar niet locken
*   [[APIE-799](https://jira.antwerpen.be/browse/APIE-799)] - rare situatie bij definitie van endpoint in service
*   [[APIE-811](https://jira.antwerpen.be/browse/APIE-811)] - publisher : delete service is not possible name already in use
*   [[APIE-828](https://jira.antwerpen.be/browse/APIE-828)] - inconsistent organisation names
*   [[APIE-830](https://jira.antwerpen.be/browse/APIE-830)] - IP restriction Policy –allowing empty –null values
*   [[APIE-831](https://jira.antwerpen.be/browse/APIE-831)] - User's "My organizations" shows friendly names, not organization names
*   [[APIE-832](https://jira.antwerpen.be/browse/APIE-832)] - pub : service cannot be deleted
*   [[APIE-833](https://jira.antwerpen.be/browse/APIE-833)] - duplicate ip ranges possible in ip restriction
*   [[APIE-834](https://jira.antwerpen.be/browse/APIE-834)] - administration expiration change possible but not saved
*   [[APIE-835](https://jira.antwerpen.be/browse/APIE-835)] - error could not create contract name already in use
*   [[APIE-839](https://jira.antwerpen.be/browse/APIE-839)] - Service version 1 was published in External but it is available in Internal
*   [[APIE-844](https://jira.antwerpen.be/browse/APIE-844)] - service identifier should be unique in 1 organisation
*   [[APIE-850](https://jira.antwerpen.be/browse/APIE-850)] - nginx config and SSL cert/key path should not be exposed via the system/status endpoint
*   [[APIE-870](https://jira.antwerpen.be/browse/APIE-870)] - api store : number of applications not correct
*   [[APIE-879](https://jira.antwerpen.be/browse/APIE-879)] - Pending contracts application images are rounded but should not be
*   [[APIE-880](https://jira.antwerpen.be/browse/APIE-880)] - Categories are alphabetically sorted, but captials are sorted first
*   [[APIE-886](https://jira.antwerpen.be/browse/APIE-886)] - Publisher : '*' in filter bij 'my organizations' geeft foutmelding
*   [[APIE-892](https://jira.antwerpen.be/browse/APIE-892)] - Overzicht organization members : 'joined on' zonder datum
*   [[APIE-920](https://jira.antwerpen.be/browse/APIE-920)] - Invalid authentication credentials met geldige apikey

## Story

*   [[APIE-400](https://jira.antwerpen.be/browse/APIE-400)] - Verwijderen van niet gepubliceerde services
*   [[APIE-401](https://jira.antwerpen.be/browse/APIE-401)] - Verwijderen van een applicatie indien deze nog niet geregistreerd is voor gebruik
*   [[APIE-406](https://jira.antwerpen.be/browse/APIE-406)] - Als service provider voorzien we ACL policies op de geregistreerde services
*   [[APIE-413](https://jira.antwerpen.be/browse/APIE-413)] - redundante API key niet meenemen in de applicatie configuratie van zodra OAuth2 client credential geactiveerd is op een service
*   [[APIE-597](https://jira.antwerpen.be/browse/APIE-597)] - Backend service token validatie : API engine endpoint voorzien
*   [[APIE-599](https://jira.antwerpen.be/browse/APIE-599)] - Backend service token refresh : endpoint voorzien
*   [[APIE-762](https://jira.antwerpen.be/browse/APIE-762)] - SAML login endpoint should check on query params and return with query extension
*   [[APIE-813](https://jira.antwerpen.be/browse/APIE-813)] - Update status info admin
*   [[APIE-814](https://jira.antwerpen.be/browse/APIE-814)] - OAuth provide UI for revoke, revoke all
*   [[APIE-842](https://jira.antwerpen.be/browse/APIE-842)] - Migration to Kong 8.1
*   [[APIE-874](https://jira.antwerpen.be/browse/APIE-874)] - Move JWT refresh endpoint to JWT protected API

Release Notes - APIe - Version API/SDK engine 0.7.0
---------------------------------------------------

## Story

*   [[APIE-806](https://jira.antwerpen.be/browse/APIE-806)] - Remodel Organization concept
*   [[APIE-812](https://jira.antwerpen.be/browse/APIE-812)] - Default to internal marketplace for service publication
*   [[APIE-815](https://jira.antwerpen.be/browse/APIE-815)] - Integrate consent page
*   [[APIE-816](https://jira.antwerpen.be/browse/APIE-816)] - Introduce token issuance flow

Release Notes - APIe - Version API/SDK engine 0.6.4
---------------------------------------------------

## Bug

*   [[APIE-492](https://jira.antwerpen.be/browse/APIE-492)] - Application is shown as RETIRED (although 2 versions are Registered) Marketplace
*   [[APIE-511](https://jira.antwerpen.be/browse/APIE-511)] - Confirm Contract enabled when no application exists. clicking at confirm contract results in nothing.
*   [[APIE-518](https://jira.antwerpen.be/browse/APIE-518)] - Error- Service definition type not supported
*   [[APIE-527](https://jira.antwerpen.be/browse/APIE-527)] - Application deleted but still the Api key is available for testing in marketplace.
*   [[APIE-678](https://jira.antwerpen.be/browse/APIE-678)] - Identityserver-a.antwerpen.be Login mechanism security issue. [see video attached.]
*   [[APIE-731](https://jira.antwerpen.be/browse/APIE-731)] - Organisaties zijn Case sensitive --> er kunnen dus meerdere keren dezelfde organisatie worden aangemaakt met verschillende casing
*   [[APIE-836](https://jira.antwerpen.be/browse/APIE-836)] - No results in API Metrics Manager

## Story

*   [[APIE-110](https://jira.antwerpen.be/browse/APIE-110)] - De Oauth grants opvragen en kunnen revoken via een endpoint
*   [[APIE-350](https://jira.antwerpen.be/browse/APIE-350)] - Service endpoint van een API veranderen nadat die werd gepubliceerd
*   [[APIE-446](https://jira.antwerpen.be/browse/APIE-446)] - Max grootte Avatar/iconen
*   [[APIE-468](https://jira.antwerpen.be/browse/APIE-468)] - Extra info over de consumers van mijn service
*   [[APIE-565](https://jira.antwerpen.be/browse/APIE-565)] - Gateway policy - OAuth -> JWT

Release Notes - APIe - Version API/SDK engine 0.6.3
---------------------------------------------------

## Sub-task

*   [[APIE-425](https://jira.antwerpen.be/browse/APIE-425)] - Nagaan indien SCIM nog steeds werkt over de IDP broker
*   [[APIE-426](https://jira.antwerpen.be/browse/APIE-426)] - Enable signed SAML response on IS
*   [[APIE-574](https://jira.antwerpen.be/browse/APIE-574)] - Userfacade add admin
*   [[APIE-575](https://jira.antwerpen.be/browse/APIE-575)] - Update IDMStorage
*   [[APIE-576](https://jira.antwerpen.be/browse/APIE-576)] - Update Userbean model
*   [[APIE-577](https://jira.antwerpen.be/browse/APIE-577)] - Update Request filter
*   [[APIE-578](https://jira.antwerpen.be/browse/APIE-578)] - Update User context
*   [[APIE-579](https://jira.antwerpen.be/browse/APIE-579)] - Update IDMstorage implementation
*   [[APIE-580](https://jira.antwerpen.be/browse/APIE-580)] - Fallback for consumer creation
*   [[APIE-581](https://jira.antwerpen.be/browse/APIE-581)] - Update web resources
*   [[APIE-582](https://jira.antwerpen.be/browse/APIE-582)] - Update auth resources
*   [[APIE-584](https://jira.antwerpen.be/browse/APIE-584)] - Differentiate scope in views
*   [[APIE-585](https://jira.antwerpen.be/browse/APIE-585)] - update ngHide/ngShow for publisher screens
*   [[APIE-633](https://jira.antwerpen.be/browse/APIE-633)] - Kong 0.7.0 upgrade and migrate in PROD
*   [[APIE-635](https://jira.antwerpen.be/browse/APIE-635)] - Friendly names voorzien in ACC

## Bug

*   [[APIE-416](https://jira.antwerpen.be/browse/APIE-416)] - IP Restriction policy accepts same IP's for both White and Black list.
*   [[APIE-417](https://jira.antwerpen.be/browse/APIE-417)] - Error: Could not create Service Policy[TCP log policy & UDP policy] : unexpected error.
*   [[APIE-445](https://jira.antwerpen.be/browse/APIE-445)] - Uploaden van een ongeldige Swagger file geeft geen error
*   [[APIE-476](https://jira.antwerpen.be/browse/APIE-476)] - JWT token is not expiring after 60mins
*   [[APIE-495](https://jira.antwerpen.be/browse/APIE-495)] - Incorrect API URL is shown
*   [[APIE-526](https://jira.antwerpen.be/browse/APIE-526)] - login fails for publisher (identity server)
*   [[APIE-532](https://jira.antwerpen.be/browse/APIE-532)] - No JWT token is sent - Grant error is shown on Publisher and marketplace.
*   [[APIE-537](https://jira.antwerpen.be/browse/APIE-537)] - Callback not working - DNS problem
*   [[APIE-546](https://jira.antwerpen.be/browse/APIE-546)] - SAML authentication exception error
*   [[APIE-555](https://jira.antwerpen.be/browse/APIE-555)] - Multilanguage engine calls do not work in acceptance environment
*   [[APIE-588](https://jira.antwerpen.be/browse/APIE-588)] - Meerdere API keys voor een application als er meerdere service contracten zijn (1 per service)
*   [[APIE-595](https://jira.antwerpen.be/browse/APIE-595)] - Not possible to add a new IP restriction policy
*   [[APIE-617](https://jira.antwerpen.be/browse/APIE-617)] - Not all API's are visible on Marketplace
*   [[APIE-636](https://jira.antwerpen.be/browse/APIE-636)] - Issue when using 'oauth' in application or service name
*   [[APIE-681](https://jira.antwerpen.be/browse/APIE-681)] - Toevoegen van een Jwt policy aan een service wordt niet opgenomen in Kong (in ieder geval niet zichtbaar in Kong-Dashboard) (bv DEV : acpaas - outputgenerator - v2)
*   [[APIE-738](https://jira.antwerpen.be/browse/APIE-738)] - In Plans "Add Policy" button is always enabled.

## Story

*   [[APIE-4](https://jira.antwerpen.be/browse/APIE-4)] - Ontwikkeling API engine
*   [[APIE-7](https://jira.antwerpen.be/browse/APIE-7)] - Overdracht & acceptatie - API/SDK engine
*   [[APIE-132](https://jira.antwerpen.be/browse/APIE-132)] - API Mgr gebruikt Identity Server als beveiligingssysteem
*   [[APIE-272](https://jira.antwerpen.be/browse/APIE-272)] - Geautomatiseerde installatie van de API Mgr in de verschillende omgevingen
*   [[APIE-278](https://jira.antwerpen.be/browse/APIE-278)] - Identity server heeft meerdere identiteit bronnen
*   [[APIE-288](https://jira.antwerpen.be/browse/APIE-288)] - Werken met OAuth scopes
*   [[APIE-352](https://jira.antwerpen.be/browse/APIE-352)] - Conventie omtrent het groeperen van services en apps in organizatie groepen
*   [[APIE-405](https://jira.antwerpen.be/browse/APIE-405)] - Externe marketplace beschikbaar
*   [[APIE-411](https://jira.antwerpen.be/browse/APIE-411)] - versie onafhankelijke application consumer naam
*   [[APIE-412](https://jira.antwerpen.be/browse/APIE-412)] - Als ontwikkelaar wil ik de ACL policy impliciet activeren op de services
*   [[APIE-456](https://jira.antwerpen.be/browse/APIE-456)] - API Engine in PROD voor extern gebruik
*   [[APIE-569](https://jira.antwerpen.be/browse/APIE-569)] - Admin rol in de API engine
*   [[APIE-570](https://jira.antwerpen.be/browse/APIE-570)] - Te herformatteren : User geen default org : Based on application environment - update user access
*   [[APIE-571](https://jira.antwerpen.be/browse/APIE-571)] - Te herformatteren : Rework the consumer creation on Kong for JWT access
*   [[APIE-572](https://jira.antwerpen.be/browse/APIE-572)] - API Manager REST endpoints aanpassen voor admin rol
*   [[APIE-573](https://jira.antwerpen.be/browse/APIE-573)] - API manager UI's (Publisher / Store) aanpassen met admin rol functionaliteiten
*   [[APIE-586](https://jira.antwerpen.be/browse/APIE-586)] - User registratie : email adres verplicht
*   [[APIE-587](https://jira.antwerpen.be/browse/APIE-587)] - URL Branding (Friendly name) voorzien cfr 352
*   [[APIE-592](https://jira.antwerpen.be/browse/APIE-592)] - Deprecated status voor een service/engine
*   [[APIE-693](https://jira.antwerpen.be/browse/APIE-693)] - Integration Notification Engine
*   [[APIE-708](https://jira.antwerpen.be/browse/APIE-708)] - Proxy for API-GW for external access
*   [[APIE-745](https://jira.antwerpen.be/browse/APIE-745)] - CORS - default values are not provided anymore by Kong
*   [[APIE-750](https://jira.antwerpen.be/browse/APIE-750)] - Change session storage keys for different API Engine consumers
*   [[APIE-751](https://jira.antwerpen.be/browse/APIE-751)] - Provide an GET querystring based variant for idp login request

## Task

*   [[APIE-506](https://jira.antwerpen.be/browse/APIE-506)] - JWT token not sent and internal error is shown when Relaystate:https%3A%2F%2Fapi-pub-a.antwerpen.be

Release Notes - APIe - Version API/SDK engine 0.6.0
---------------------------------------------------

## Sub-task

*   [[APIE-425](https://jira.antwerpen.be/browse/APIE-425)] - Nagaan indien SCIM nog steeds werkt over de IDP broker
*   [[APIE-426](https://jira.antwerpen.be/browse/APIE-426)] - Enable signed SAML response on IS
*   [[APIE-574](https://jira.antwerpen.be/browse/APIE-574)] - Userfacade add admin
*   [[APIE-575](https://jira.antwerpen.be/browse/APIE-575)] - Update IDMStorage
*   [[APIE-576](https://jira.antwerpen.be/browse/APIE-576)] - Update Userbean model
*   [[APIE-577](https://jira.antwerpen.be/browse/APIE-577)] - Update Request filter
*   [[APIE-578](https://jira.antwerpen.be/browse/APIE-578)] - Update User context
*   [[APIE-579](https://jira.antwerpen.be/browse/APIE-579)] - Update IDMstorage implementation
*   [[APIE-580](https://jira.antwerpen.be/browse/APIE-580)] - Fallback for consumer creation
*   [[APIE-581](https://jira.antwerpen.be/browse/APIE-581)] - Update web resources
*   [[APIE-582](https://jira.antwerpen.be/browse/APIE-582)] - Update auth resources
*   [[APIE-584](https://jira.antwerpen.be/browse/APIE-584)] - Differentiate scope in views
*   [[APIE-585](https://jira.antwerpen.be/browse/APIE-585)] - update ngHide/ngShow for publisher screens
*   [[APIE-633](https://jira.antwerpen.be/browse/APIE-633)] - Kong 0.7.0 upgrade and migrate in PROD
*   [[APIE-635](https://jira.antwerpen.be/browse/APIE-635)] - Friendly names voorzien in ACC

## Bug

*   [[APIE-416](https://jira.antwerpen.be/browse/APIE-416)] - IP Restriction policy accepts same IP's for both White and Black list.
*   [[APIE-417](https://jira.antwerpen.be/browse/APIE-417)] - Error: Could not create Service Policy[TCP log policy & UDP policy] : unexpected error.
*   [[APIE-445](https://jira.antwerpen.be/browse/APIE-445)] - Uploaden van een ongeldige Swagger file geeft geen error
*   [[APIE-495](https://jira.antwerpen.be/browse/APIE-495)] - Incorrect API URL is shown
*   [[APIE-526](https://jira.antwerpen.be/browse/APIE-526)] - login fails for publisher (identity server)
*   [[APIE-527](https://jira.antwerpen.be/browse/APIE-527)] - Application deleted but still the Api key is available for testing in marketplace.
*   [[APIE-588](https://jira.antwerpen.be/browse/APIE-588)] - Meerdere API keys voor een application als er meerdere service contracten zijn (1 per service)
*   [[APIE-617](https://jira.antwerpen.be/browse/APIE-617)] - Not all API's are visible on Marketplace

## Story

*   [[APIE-4](https://jira.antwerpen.be/browse/APIE-4)] - Ontwikkeling API engine
*   [[APIE-7](https://jira.antwerpen.be/browse/APIE-7)] - Overdracht & acceptatie - API/SDK engine
*   [[APIE-132](https://jira.antwerpen.be/browse/APIE-132)] - API Mgr gebruikt Identity Server als beveiligingssysteem
*   [[APIE-278](https://jira.antwerpen.be/browse/APIE-278)] - Identity server heeft meerdere identiteit bronnen
*   [[APIE-288](https://jira.antwerpen.be/browse/APIE-288)] - Werken met OAuth scopes
*   [[APIE-352](https://jira.antwerpen.be/browse/APIE-352)] - Conventie omtrent het groeperen van services en apps in organizatie groepen
*   [[APIE-405](https://jira.antwerpen.be/browse/APIE-405)] - Externe marketplace beschikbaar
*   [[APIE-411](https://jira.antwerpen.be/browse/APIE-411)] - versie onafhankelijke application consumer naam
*   [[APIE-456](https://jira.antwerpen.be/browse/APIE-456)] - API Engine in PROD voor extern gebruik
*   [[APIE-569](https://jira.antwerpen.be/browse/APIE-569)] - Admin rol in de API engine
*   [[APIE-570](https://jira.antwerpen.be/browse/APIE-570)] - Te herformatteren : User geen default org : Based on application environment - update user access
*   [[APIE-571](https://jira.antwerpen.be/browse/APIE-571)] - Te herformatteren : Rework the consumer creation on Kong for JWT access
*   [[APIE-572](https://jira.antwerpen.be/browse/APIE-572)] - API Manager REST endpoints aanpassen voor admin rol
*   [[APIE-573](https://jira.antwerpen.be/browse/APIE-573)] - API manager UI's (Publisher / Store) aanpassen met admin rol functionaliteiten

## Task

*   [[APIE-506](https://jira.antwerpen.be/browse/APIE-506)] - JWT token not sent and internal error is shown when Relaystate:https%3A%2F%2Fapi-pub-a.antwerpen.be

Release Notes - Digipolis-APIM - Version APIM-v0.5.2
----------------------------------------------------

## Sub-task

*   [[APIE-425](https://jira.antwerpen.be/browse/APIE-425)] - SCIM IDP protocol
*   [[APIE-426](https://jira.antwerpen.be/browse/APIE-426)] - Enable signed SAML response on IS

## Bug

*   [[APIE-414](https://jira.antwerpen.be/browse/APIE-414)] - Add Policy - CORS allows incorrect values in form.
*   [[APIE-415](https://jira.antwerpen.be/browse/APIE-415)] - OAuth2 policy form accepts negative "Token Expiration" values.
*   [[APIE-416](https://jira.antwerpen.be/browse/APIE-416)] - IP Restriction policy accepts same IP's for both White and Black list.
*   [[APIE-417](https://jira.antwerpen.be/browse/APIE-417)] - Error: Could not create Service Policy[TCP log policy & UDP policy] : unexpected error.
*   [[APIE-418](https://jira.antwerpen.be/browse/APIE-418)] - New Announcement fails to create an announcement and an Unexpected error is thrown.
*   [[APIE-421](https://jira.antwerpen.be/browse/APIE-421)] - Deleting Applications/Service does not always work correctly
*   [[APIE-423](https://jira.antwerpen.be/browse/APIE-423)] - Creation of Tickets with redundant names possible.
*   [[APIE-445](https://jira.antwerpen.be/browse/APIE-445)] - Uploaden van een ongeldige Swagger file geeft geen error

## Story

*   [[APIE-4](https://jira.antwerpen.be/browse/APIE-4)] - Ontwikkeling API engine
*   [[APIE-7](https://jira.antwerpen.be/browse/APIE-7)] - Overdracht & acceptatie - API/SDK engine
*   [[APIE-132](https://jira.antwerpen.be/browse/APIE-132)] - API Mgr gebruikt Identity Server als beveiligingssysteem
*   [[APIE-272](https://jira.antwerpen.be/browse/APIE-272)] - Geautomatiseerde installatie van de API Mgr in de verschillende omgevingen
*   [[APIE-278](https://jira.antwerpen.be/browse/APIE-278)] - Identity server heeft meerdere identiteit bronnen

Release Notes - Digipolis-APIM - Version APIM-v0.5.1
----------------------------------------------------
## Bug

*   [[APIE-402](https://jira.antwerpen.be/browse/APIE-402)] - Connectiestring tussen Kong en Cassandra was plots verdwenen
*   [[APIE-448](https://jira.antwerpen.be/browse/APIE-448)] - Multilanguage Engine Swagger file breaks try-out functionality

## New Feature

*   [[APIE-447](https://jira.antwerpen.be/browse/APIE-447)] - Feature request: aanpassing copy "my marketplace"

## Story

*   [[APIE-475](https://jira.antwerpen.be/browse/APIE-475)] - Wijzigingen in IDP config en user attributen

Release Notes - Digipolis-APIM - Version APIM-v0.5.0
----------------------------------------------------

## Bug

*   [[DPAPIM-258](https://trust1t.atlassian.net/browse/DPAPIM-258)] - Cannot upload a Swagger definition if it does not contain an externaldocs section ==> NPE on BE
*   [[DPAPIM-260](https://trust1t.atlassian.net/browse/DPAPIM-260)] - Cannot retrieve market info for APIs that are published by organizations the user is not a member of

## Story

*   [[DPAPIM-15](https://trust1t.atlassian.net/browse/DPAPIM-15)] - As a developer I would like to have a Java tutorial upon how to create/generate an API
*   [[DPAPIM-97](https://trust1t.atlassian.net/browse/DPAPIM-97)] - As an API consumer I want to be able to subscribe on an API service in order be notified when changes occur
*   [[DPAPIM-172](https://trust1t.atlassian.net/browse/DPAPIM-172)] - As an system administrator I want the user updates to be synchronized - both ways - with the IDP using SCIM v1.1 protocol
*   [[DPAPIM-205](https://trust1t.atlassian.net/browse/DPAPIM-205)] - As a User I want to manage my authorization credentials provisions by the API gateway
*   [[DPAPIM-224](https://trust1t.atlassian.net/browse/DPAPIM-224)] - As a developer publishing services I want to add JWT policy to a service
*   [[DPAPIM-225](https://trust1t.atlassian.net/browse/DPAPIM-225)] - As a consumer for the API Marketplace I want to have an overview of my different credentials and manage them
*   [[DPAPIM-231](https://trust1t.atlassian.net/browse/DPAPIM-231)] - As a developer for the API Engine I want to retrieve consumer information from the IDP - at least the user email for notification purposes
*   [[DPAPIM-235](https://trust1t.atlassian.net/browse/DPAPIM-235)] - Migration Kong 0.5.0
*   [[DPAPIM-245](https://trust1t.atlassian.net/browse/DPAPIM-245)] - As a integration developer I want to have example code available for API integration
*   [[DPAPIM-250](https://trust1t.atlassian.net/browse/DPAPIM-250)] - As a Digipolis develop I want to enable explicitly a service to be exposed on the marketplace view scoped for external users
*   [[DPAPIM-264](https://trust1t.atlassian.net/browse/DPAPIM-264)] - As a developer of the publisher i want an alternative validation for my swagger file, in order to be less restrictive
*   [[DPAPIM-265](https://trust1t.atlassian.net/browse/DPAPIM-265)] - As an integrator I want to integrate a first Engine
*   [[DPAPIM-266](https://trust1t.atlassian.net/browse/DPAPIM-266)] - As a developer I want to integrate my API on the API Engine
*   [[DPAPIM-268](https://trust1t.atlassian.net/browse/DPAPIM-268)] - Double login causes error

Release Notes - Digipolis-APIM - Version APIM-v0.0.2
----------------------------------------------------

## Bug

*   [[DPAPIM-218](https://trust1t.atlassian.net/browse/DPAPIM-218)] - Market Documentation needs to be testable for OAuth2 enabled Services even if application is not yet published
*   [[DPAPIM-220](https://trust1t.atlassian.net/browse/DPAPIM-220)] - BE returns Error 500 when trying to retrieve a Service's definition if none exists
*   [[DPAPIM-221](https://trust1t.atlassian.net/browse/DPAPIM-221)] - Code cleanup
*   [[DPAPIM-234](https://trust1t.atlassian.net/browse/DPAPIM-234)] - Metrics page when resizing you seem to loose information

## Story

*   [[DPAPIM-228](https://trust1t.atlassian.net/browse/DPAPIM-228)] - As a user from a 3rd Party application using OAuth2 I only want to see applicable scopes - withing my role - and not all available scopes
*   [[DPAPIM-242](https://trust1t.atlassian.net/browse/DPAPIM-242)] - As a developer I want to see a page that i'm succesfully loggedout without being redirected to the login page.
*   [[DPAPIM-248](https://trust1t.atlassian.net/browse/DPAPIM-248)] - As a developer I want to have a crud support functionality in both front-end applications

## Task

*   [[DPAPIM-253](https://trust1t.atlassian.net/browse/DPAPIM-253)] - As a tester I want to test functionalities from the API publisher/marketplace
*   [[DPAPIM-254](https://trust1t.atlassian.net/browse/DPAPIM-254)] - As an developer I want to integrate the ME service using an existing client application
*   [[DPAPIM-255](https://trust1t.atlassian.net/browse/DPAPIM-255)] - As a developer I want to support the integration of an authentication/authorization application

Release Notes - Digipolis-APIM - Version APIM-v0.5.0
----------------------------------------------------

## Bug

*   [[DPAPIM-258](https://trust1t.atlassian.net/browse/DPAPIM-258)] - Cannot upload a Swagger definition if it does not contain an externaldocs section ==> NPE on BE
*   [[DPAPIM-260](https://trust1t.atlassian.net/browse/DPAPIM-260)] - Cannot retrieve market info for APIs that are published by organizations the user is not a member of

## Story

*   [[DPAPIM-15](https://trust1t.atlassian.net/browse/DPAPIM-15)] - As a developer I would like to have a Java tutorial upon how to create/generate an API
*   [[DPAPIM-97](https://trust1t.atlassian.net/browse/DPAPIM-97)] - As an API consumer I want to be able to subscribe on an API service in order be notified when changes occur
*   [[DPAPIM-172](https://trust1t.atlassian.net/browse/DPAPIM-172)] - As an system administrator I want the user updates to be synchronized - both ways - with the IDP using SCIM v1.1 protocol
*   [[DPAPIM-205](https://trust1t.atlassian.net/browse/DPAPIM-205)] - As a User I want to manage my authorization credentials provisions by the API gateway
*   [[DPAPIM-224](https://trust1t.atlassian.net/browse/DPAPIM-224)] - As a developer publishing services I want to add JWT policy to a service
*   [[DPAPIM-225](https://trust1t.atlassian.net/browse/DPAPIM-225)] - As a consumer for the API Marketplace I want to have an overview of my different credentials and manage them
*   [[DPAPIM-231](https://trust1t.atlassian.net/browse/DPAPIM-231)] - As a developer for the API Engine I want to retrieve consumer information from the IDP - at least the user email for notification purposes
*   [[DPAPIM-235](https://trust1t.atlassian.net/browse/DPAPIM-235)] - Migration Kong 0.5.0
*   [[DPAPIM-245](https://trust1t.atlassian.net/browse/DPAPIM-245)] - As a integration developer I want to have example code available for API integration
*   [[DPAPIM-250](https://trust1t.atlassian.net/browse/DPAPIM-250)] - As a Digipolis develop I want to enable explicitly a service to be exposed on the marketplace view scoped for external users
*   [[DPAPIM-264](https://trust1t.atlassian.net/browse/DPAPIM-264)] - As a developer of the publisher i want an alternative validation for my swagger file, in order to be less restrictive
*   [[DPAPIM-265](https://trust1t.atlassian.net/browse/DPAPIM-265)] - As an integrator I want to integrate a first Engine
*   [[DPAPIM-266](https://trust1t.atlassian.net/browse/DPAPIM-266)] - As a developer I want to integrate my API on the API Engine
*   [[DPAPIM-268](https://trust1t.atlassian.net/browse/DPAPIM-268)] - Double login causes error

Release Notes - Digipolis-APIM - Version APIM-v0.0.2
----------------------------------------------------

## Bug

*   [[DPAPIM-218](https://trust1t.atlassian.net/browse/DPAPIM-218)] - Market Documentation needs to be testable for OAuth2 enabled Services even if application is not yet published
*   [[DPAPIM-220](https://trust1t.atlassian.net/browse/DPAPIM-220)] - BE returns Error 500 when trying to retrieve a Service's definition if none exists
*   [[DPAPIM-221](https://trust1t.atlassian.net/browse/DPAPIM-221)] - Code cleanup
*   [[DPAPIM-234](https://trust1t.atlassian.net/browse/DPAPIM-234)] - Metrics page when resizing you seem to loose information

## Story

*   [[DPAPIM-228](https://trust1t.atlassian.net/browse/DPAPIM-228)] - As a user from a 3rd Party application using OAuth2 I only want to see applicable scopes - withing my role - and not all available scopes
*   [[DPAPIM-242](https://trust1t.atlassian.net/browse/DPAPIM-242)] - As a developer I want to see a page that i'm succesfully loggedout without being redirected to the login page.
*   [[DPAPIM-248](https://trust1t.atlassian.net/browse/DPAPIM-248)] - As a developer I want to have a crud support functionality in both front-end applications

## Task

*   [[DPAPIM-253](https://trust1t.atlassian.net/browse/DPAPIM-253)] - As a tester I want to test functionalities from the API publisher/marketplace
*   [[DPAPIM-254](https://trust1t.atlassian.net/browse/DPAPIM-254)] - As an developer I want to integrate the ME service using an existing client application
*   [[DPAPIM-255](https://trust1t.atlassian.net/browse/DPAPIM-255)] - As a developer I want to support the integration of an authentication/authorization application

Releases - Digipolis-APIM - Version APIM-v0.0.1
------------------------------------------------

## Bug

*   [[DPAPIM-47](https://trust1t.atlassian.net/browse/DPAPIM-47)] - Marketplace - Categories navbar enable only for API exploration
*   [[DPAPIM-85](https://trust1t.atlassian.net/browse/DPAPIM-85)] - Double Access-Control-Allow-Origin headers returned when there is an error
*   [[DPAPIM-87](https://trust1t.atlassian.net/browse/DPAPIM-87)] - Rest endpoint for api key and keyauth missing
*   [[DPAPIM-120](https://trust1t.atlassian.net/browse/DPAPIM-120)] - the test is going towards localhost visireg, where it should go through gateway
*   [[DPAPIM-135](https://trust1t.atlassian.net/browse/DPAPIM-135)] - prevent upload of files other than .json
*   [[DPAPIM-140](https://trust1t.atlassian.net/browse/DPAPIM-140)] - As a user when adding rate limiting to a plan, the policy blocks upon application registration
*   [[DPAPIM-176](https://trust1t.atlassian.net/browse/DPAPIM-176)] - I choose to make a clone of the existing published service, yet all fields are empty (in the new version)
*   [[DPAPIM-178](https://trust1t.atlassian.net/browse/DPAPIM-178)] - Creating an application/service with a logo sometimes results in Internal Server Error
*   [[DPAPIM-179](https://trust1t.atlassian.net/browse/DPAPIM-179)] - Categories that contain spaces are saved with dashes instead of spaces
*   [[DPAPIM-185](https://trust1t.atlassian.net/browse/DPAPIM-185)] - Publish application does not give result
*   [[DPAPIM-186](https://trust1t.atlassian.net/browse/DPAPIM-186)] - create version with existing name should give error
*   [[DPAPIM-187](https://trust1t.atlassian.net/browse/DPAPIM-187)] - error message text
*   [[DPAPIM-193](https://trust1t.atlassian.net/browse/DPAPIM-193)] - (URL validation) error when publishing a service which has 2 plans
*   [[DPAPIM-197](https://trust1t.atlassian.net/browse/DPAPIM-197)] - Search API field does not work
*   [[DPAPIM-198](https://trust1t.atlassian.net/browse/DPAPIM-198)] - application search field does not work
*   [[DPAPIM-202](https://trust1t.atlassian.net/browse/DPAPIM-202)] - only the most recent version of a service should be visible in the list or tile view.
*   [[DPAPIM-203](https://trust1t.atlassian.net/browse/DPAPIM-203)] - resizing pop-up page does not work properly
*   [[DPAPIM-204](https://trust1t.atlassian.net/browse/DPAPIM-204)] - Clicking My Marketplace in User screen results in error
*   [[DPAPIM-216](https://trust1t.atlassian.net/browse/DPAPIM-216)] - API keys for retired application versions continue to work
*   [[DPAPIM-217](https://trust1t.atlassian.net/browse/DPAPIM-217)] - Service Policies are never visualized in the Marketplace
*   [[DPAPIM-219](https://trust1t.atlassian.net/browse/DPAPIM-219)] - BE returns Error 500 when trying to create a contract that already exists
*   [[DPAPIM-222](https://trust1t.atlassian.net/browse/DPAPIM-222)] - Endpoint to update an Application's OAuth Callback URL is not consistent with other Application endpoints
*   [[DPAPIM-223](https://trust1t.atlassian.net/browse/DPAPIM-223)] - API Engine BaseUrl is still hardcoded in the app.apiEngine.js script
*   [[DPAPIM-238](https://trust1t.atlassian.net/browse/DPAPIM-238)] - BE returns error 500 when trying to register an application that has a contract with an OAuth2 enabled service

## Story

*   [[DPAPIM-13](https://trust1t.atlassian.net/browse/DPAPIM-13)] - As an API consumer I want to see basic metrics upon the usage of my subscribed APIs in the context of an application.
*   [[DPAPIM-18](https://trust1t.atlassian.net/browse/DPAPIM-18)] - Implement datamodel
*   [[DPAPIM-19](https://trust1t.atlassian.net/browse/DPAPIM-19)] - As an API Engine I want to have a MongoDB available for usage (in order to persist API objects)
*   [[DPAPIM-22](https://trust1t.atlassian.net/browse/DPAPIM-22)] - Setup and configure project
*   [[DPAPIM-23](https://trust1t.atlassian.net/browse/DPAPIM-23)] - As a developer I want to setup an new Angular JS project for the API Publisher application, reusing already existing code of the marketplace, in order to start development
*   [[DPAPIM-24](https://trust1t.atlassian.net/browse/DPAPIM-24)] - As a developer I want to have a WSO2 identity server available in order to be able to dev login/logout and authorization pages.
*   [[DPAPIM-26](https://trust1t.atlassian.net/browse/DPAPIM-26)] - Wireframes
*   [[DPAPIM-34](https://trust1t.atlassian.net/browse/DPAPIM-34)] - Implement application-plan(version)-resource(version) screen
*   [[DPAPIM-35](https://trust1t.atlassian.net/browse/DPAPIM-35)] - Implement application crud page
*   [[DPAPIM-36](https://trust1t.atlassian.net/browse/DPAPIM-36)] - Mockbin - configure Redis as service to autostart
*   [[DPAPIM-37](https://trust1t.atlassian.net/browse/DPAPIM-37)] - Mockbin - setup PM2 process to autostart
*   [[DPAPIM-39](https://trust1t.atlassian.net/browse/DPAPIM-39)] - Marketplace - API Notification implementation
*   [[DPAPIM-40](https://trust1t.atlassian.net/browse/DPAPIM-40)] - Marketplace - API Subscription page implementation
*   [[DPAPIM-41](https://trust1t.atlassian.net/browse/DPAPIM-41)] - Marketplace - API Support page implementation
*   [[DPAPIM-42](https://trust1t.atlassian.net/browse/DPAPIM-42)] - As an API consumer I would like to see service metrics for the services registered in an application.
*   [[DPAPIM-43](https://trust1t.atlassian.net/browse/DPAPIM-43)] - Marketplace - Application page
*   [[DPAPIM-44](https://trust1t.atlassian.net/browse/DPAPIM-44)] - Marketplace Organization page implementation
*   [[DPAPIM-45](https://trust1t.atlassian.net/browse/DPAPIM-45)] - Marketplace - User profile implementation
*   [[DPAPIM-46](https://trust1t.atlassian.net/browse/DPAPIM-46)] - As a developer I want the backend integration for user profile and settings to be done in order to show that in UI
*   [[DPAPIM-51](https://trust1t.atlassian.net/browse/DPAPIM-51)] - API Engine - Implement data model
*   [[DPAPIM-52](https://trust1t.atlassian.net/browse/DPAPIM-52)] - API Engine - Implement general security libraries
*   [[DPAPIM-53](https://trust1t.atlassian.net/browse/DPAPIM-53)] - API Engine - Implement use case: register API
*   [[DPAPIM-54](https://trust1t.atlassian.net/browse/DPAPIM-54)] - API Engine - Implement use case: consume API
*   [[DPAPIM-55](https://trust1t.atlassian.net/browse/DPAPIM-55)] - Implement IDM keycloak (temporary integration point and user management)
*   [[DPAPIM-56](https://trust1t.atlassian.net/browse/DPAPIM-56)] - Marketplace - Create simple screen for API terms
*   [[DPAPIM-57](https://trust1t.atlassian.net/browse/DPAPIM-57)] - Marketplace - integration API Engine: use case consume API
*   [[DPAPIM-58](https://trust1t.atlassian.net/browse/DPAPIM-58)] - Kong client implementation
*   [[DPAPIM-60](https://trust1t.atlassian.net/browse/DPAPIM-60)] - As an architect I want to have an view upon the high availability, failover and resilience of the solution.
*   [[DPAPIM-61](https://trust1t.atlassian.net/browse/DPAPIM-61)] - Planningsmeeting en sprint demo - sprint 2
*   [[DPAPIM-68](https://trust1t.atlassian.net/browse/DPAPIM-68)] - As a API consumer I want to be able logging and comment an issue for a certain API
*   [[DPAPIM-71](https://trust1t.atlassian.net/browse/DPAPIM-71)] - As an API publisher I want to be able adding and configuring basic authentication for my registered API
*   [[DPAPIM-72](https://trust1t.atlassian.net/browse/DPAPIM-72)] - As an API producer I want to be able adding and configuring an Throttling policy on an API.
*   [[DPAPIM-73](https://trust1t.atlassian.net/browse/DPAPIM-73)] - As an API Engine system I want my http logs to be sent to a metrics engine.
*   [[DPAPIM-81](https://trust1t.atlassian.net/browse/DPAPIM-81)] - As a developer I want to use an IDP for authentication in order to access the marketplace or publisher API
*   [[DPAPIM-82](https://trust1t.atlassian.net/browse/DPAPIM-82)] - As an API Engine administrator I want to be able adding Gateway's to the API Engine in order to publish and consume APIs.
*   [[DPAPIM-84](https://trust1t.atlassian.net/browse/DPAPIM-84)] - As an API producer I want to produce the API Engine on the API Gateway in order to expose the contract for external services.
*   [[DPAPIM-86](https://trust1t.atlassian.net/browse/DPAPIM-86)] - Configure default Gateway dev in installscript
*   [[DPAPIM-88](https://trust1t.atlassian.net/browse/DPAPIM-88)] - Missing endpoint for categories
*   [[DPAPIM-89](https://trust1t.atlassian.net/browse/DPAPIM-89)] - Add organization on the generated context path
*   [[DPAPIM-90](https://trust1t.atlassian.net/browse/DPAPIM-90)] - Add endpoint to retrieve all published services
*   [[DPAPIM-91](https://trust1t.atlassian.net/browse/DPAPIM-91)] - As an external developer it should be possible to request an endpoint for the creation of additional consumer API key's to be generated for my api
*   [[DPAPIM-93](https://trust1t.atlassian.net/browse/DPAPIM-93)] - As a API producer I want to create an organization context in which I can register APIs
*   [[DPAPIM-94](https://trust1t.atlassian.net/browse/DPAPIM-94)] - As an API producer I want to create one or more plans that contains policies in the context of an organization
*   [[DPAPIM-95](https://trust1t.atlassian.net/browse/DPAPIM-95)] - As an API producer I want to be able to lock a plan in order to make the plan available to the API registration process
*   [[DPAPIM-96](https://trust1t.atlassian.net/browse/DPAPIM-96)] - As an API developer I want to register API services
*   [[DPAPIM-98](https://trust1t.atlassian.net/browse/DPAPIM-98)] - As an API producer I want to be able uploading an API specification during registration and testing it with a try-out on method level
*   [[DPAPIM-99](https://trust1t.atlassian.net/browse/DPAPIM-99)] - As an API developer I want to publish an API version such that it can be exposed on the marketplace
*   [[DPAPIM-100](https://trust1t.atlassian.net/browse/DPAPIM-100)] - As an API developer, after publication of an API, I want to see an example request an see the base URL that will be exposed
*   [[DPAPIM-101](https://trust1t.atlassian.net/browse/DPAPIM-101)] - As a project coordinator we have to sync with the existing implementation in the current WSO2 apim for authentication of A-stad profiles
*   [[DPAPIM-102](https://trust1t.atlassian.net/browse/DPAPIM-102)] - Planningsmeeting en sprint demo - sprint 3
*   [[DPAPIM-103](https://trust1t.atlassian.net/browse/DPAPIM-103)] - Planningsmeeting en sprint demo - sprint 4
*   [[DPAPIM-104](https://trust1t.atlassian.net/browse/DPAPIM-104)] - As an API Engine developer I want to provide information for API authentication and the available integration endpoints to other Engine providers
*   [[DPAPIM-105](https://trust1t.atlassian.net/browse/DPAPIM-105)] - As an API publisher I want to be able adding, configuring and testing all security policies from API the documentation
*   [[DPAPIM-106](https://trust1t.atlassian.net/browse/DPAPIM-106)] - As an API developer I want to integrate with OAuth2 endpoints in order to authorize consumers.
*   [[DPAPIM-107](https://trust1t.atlassian.net/browse/DPAPIM-107)] - As an API publisher we want CORS to be enabled automatically upon registration for all APIs
*   [[DPAPIM-108](https://trust1t.atlassian.net/browse/DPAPIM-108)] - As an API publisher I want the gateway to log traffic for the API on a remote ELK stack using tcp/http
*   [[DPAPIM-109](https://trust1t.atlassian.net/browse/DPAPIM-109)] - As an API producer I want to be able consulting the API metrics
*   [[DPAPIM-110](https://trust1t.atlassian.net/browse/DPAPIM-110)] - As a business analyst I want to have a user manual for the API Marketplace web aplication
*   [[DPAPIM-111](https://trust1t.atlassian.net/browse/DPAPIM-111)] - As a business analyst I want to have a user manual for the API Publisher web applciation
*   [[DPAPIM-112](https://trust1t.atlassian.net/browse/DPAPIM-112)] - As a web application consumer I want a easy to understand and intuetive web GUI
*   [[DPAPIM-114](https://trust1t.atlassian.net/browse/DPAPIM-114)] - As an application developer I want to be able adding consumers with keyauth for authorization
*   [[DPAPIM-115](https://trust1t.atlassian.net/browse/DPAPIM-115)] - As an API consumer I would like to add a logo to my consuming application
*   [[DPAPIM-116](https://trust1t.atlassian.net/browse/DPAPIM-116)] - As an API consumer I don't want to see categories in the marketplace for unpublished services
*   [[DPAPIM-118](https://trust1t.atlassian.net/browse/DPAPIM-118)] - As an API consumer I want to have a intuitive flow for the service registration
*   [[DPAPIM-119](https://trust1t.atlassian.net/browse/DPAPIM-119)] - As an API consumer/producer I want to have a dashboard for organizations, applications, plans and services
*   [[DPAPIM-125](https://trust1t.atlassian.net/browse/DPAPIM-125)] - As an API consumer, I want to have a quick overview of 'My Marketplace' providing status info and allowing to take actions starting from 'my applications'.
*   [[DPAPIM-128](https://trust1t.atlassian.net/browse/DPAPIM-128)] - As an API consumer, I want to register a service contract for a selected API.
*   [[DPAPIM-134](https://trust1t.atlassian.net/browse/DPAPIM-134)] - As a developer I want to differentiate between policies service-scoped vs plan-scoped
*   [[DPAPIM-139](https://trust1t.atlassian.net/browse/DPAPIM-139)] - As an api user I want to have session management in the API client application in order to maintain a session-like GUI experience
*   [[DPAPIM-142](https://trust1t.atlassian.net/browse/DPAPIM-142)] - As a developer I want to setup the environment in Bamboo for continuous integration
*   [[DPAPIM-149](https://trust1t.atlassian.net/browse/DPAPIM-149)] - As a user of the API Engine client applications I want to be able to log off using SAML2 on the Identity Provider
*   [[DPAPIM-151](https://trust1t.atlassian.net/browse/DPAPIM-151)] - As a security officer, I want the API engine to provision identity information towards the IDP
*   [[DPAPIM-177](https://trust1t.atlassian.net/browse/DPAPIM-177)] - As a developer I want to analyse my API metrics using Mashape analytics
*   [[DPAPIM-188](https://trust1t.atlassian.net/browse/DPAPIM-188)] - As a user I want to see the version, status and possible actions on the plans
*   [[DPAPIM-190](https://trust1t.atlassian.net/browse/DPAPIM-190)] - as a user I require to be guided through the process of service creation.
*   [[DPAPIM-200](https://trust1t.atlassian.net/browse/DPAPIM-200)] - As a user I want to be able using a dev environment at Digipolis to test
*   [[DPAPIM-207](https://trust1t.atlassian.net/browse/DPAPIM-207)] - As a developer I want to use the application API key instead of the logged user api key in the try-out documentation (in the context of an selected application)
*   [[DPAPIM-208](https://trust1t.atlassian.net/browse/DPAPIM-208)] - As a developer I want to see oauth fields in my application when contracted a oauth enabled service
*   [[DPAPIM-209](https://trust1t.atlassian.net/browse/DPAPIM-209)] - As a developer I want to use OAuth2 in the documentation overview of a service version
*   [[DPAPIM-210](https://trust1t.atlassian.net/browse/DPAPIM-210)] - As a developer I want to see service plan details in service tab on the marketplace
*   [[DPAPIM-211](https://trust1t.atlassian.net/browse/DPAPIM-211)] - As a develop I want to see policy information in the publisher API under the policy tab in a readable way.
*   [[DPAPIM-212](https://trust1t.atlassian.net/browse/DPAPIM-212)] - As a developer I want to change my email address on my profile settings
*   [[DPAPIM-213](https://trust1t.atlassian.net/browse/DPAPIM-213)] - As a developer I want to add Terms of use to a service definition
*   [[DPAPIM-214](https://trust1t.atlassian.net/browse/DPAPIM-214)] - As a developer I want to follow/unfollow services in order to be updated for service information
*   [[DPAPIM-215](https://trust1t.atlassian.net/browse/DPAPIM-215)] - As a developer I want to implement an additional application metric in order to show this info on the marketplace
*   [[DPAPIM-226](https://trust1t.atlassian.net/browse/DPAPIM-226)] - As an service producer I want to see which application verions are registered for a service version
*   [[DPAPIM-227](https://trust1t.atlassian.net/browse/DPAPIM-227)] - As a developer I want to see the online doc for a service which is retrieved form the swagger file, on top of the service info
*   [[DPAPIM-232](https://trust1t.atlassian.net/browse/DPAPIM-232)] - As a developer publishing services I want to be able to update the swagger documentation for a published service version
*   [[DPAPIM-237](https://trust1t.atlassian.net/browse/DPAPIM-237)] - As a developer on the marketplace, I want to see for a service which OAuth2 profiles are enabled.
*   [[DPAPIM-240](https://trust1t.atlassian.net/browse/DPAPIM-240)] - As a developer I need to provide an application version callback url before i can use OAuth grant in documentation tab
*   [[DPAPIM-241](https://trust1t.atlassian.net/browse/DPAPIM-241)] - As a developer I want to use announcements for services

## Task

*   [[DPAPIM-137](https://trust1t.atlassian.net/browse/DPAPIM-137)] - Applications that had a contract but then break all contracts stay in status 'Ready'