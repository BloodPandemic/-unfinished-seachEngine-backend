# -unfinished-seachEngine-backend
some project in py/java/sql to crawl some pages and display them based on what you searched.

### Note(py)
run the crawler like this
`/bin/python3 crawler.py -h[help] --depth start_url`

## Note
make sure to put the mysql-connector on the same path as the java script
then run 
```bash
javac -cp mysql-connector-j-8.0.33 SearchEngine.java
sudo java -cp mysql-connector-j-8.0.33:. SearchEngine.java
```

