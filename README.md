# Hamama - Website

The repository contains the greenhouse website which written in Html, Css, Javascript and Java(Jsp). the data on the website is from the database,
which contains the updated measures from the physical greenhousde.
the project, is written with Google App Engine (in order to get it running on google cloud.)

In the website, there is a server, which the website is using in order to trasfer information from the database. 
it is also possible to upload data to the database through the server, with the following api:
	
```
Add a new measure to a specific sensor: 
http://<ip address>:8080/board?cmd=measure&sid=<sid>&time=<time>&value=<12.4>

Add a new measure to the log: 
http://<ip address>:8080/board?cmd=log&sid=<sid>&time=<time>&priority=<1-3>&message=<message>
```

# Screenshot's from the website:

<br>

<p align="center" width="70%">
    <img width="90%" src="https://user-images.githubusercontent.com/60574244/131137267-4d4d5968-1e13-4d68-afc1-6476d82bf9fb.png">  ㅤ
</p>

<br>

<p align="center" width="70%">
    <img width="90%" src="https://user-images.githubusercontent.com/60574244/131139098-84265424-9d4a-4a87-a4ea-6298b3673cbc.png">  ㅤ
</p>

<br>

<p align="center" width="70%">
    <img width="90%" src="https://user-images.githubusercontent.com/60574244/131139313-8d68b2ab-6062-4bba-b4a8-fcc363fe0f1c.png">  ㅤ
</p>

