# Change Url That You Want to get it's Site map
- open application.properties
- change value for testRootSiteUrl

# When application Started Automatically crawler Run
- Using postConstruct Method in Service Class
- when application finish logging ,file with name [siteMap.json] will be created 

# You can although run crawler and change url dynamically
- send Get Request to http://localhost:8080/map?url=https://monzo.com
- you can change value of url parameter

# Crwaler will take time to crawl all links in given page s Please wait and Track The logs
