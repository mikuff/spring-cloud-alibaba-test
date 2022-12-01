``` shell
# nacos
.\startup.cmd -m standalone

# sentinal
java -Dserver.port=8070 -Dcsp.sentinel.dashboard.server=localhost:8070 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.4.jar
```