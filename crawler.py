import requests
from bs4 import BeautifulSoup
import mysql.connector
import argparse

parser = argparse.ArgumentParser(description='Crawl a website and store page titles, descriptions, and links in a MySQL database')
parser.add_argument('start_url', metavar='start_url', type=str, help='the URL of the starting page')
parser.add_argument('--depth', metavar='depth', type=int, help='the maximum depth to crawl')
args = parser.parse_args()

db = mysql.connector.connect(
    host="localhost",
    user="user22",
    password="password",
    database="indexes"
)

cursor = db.cursor()

cursor.execute("""
    CREATE TABLE IF NOT EXISTS testin2 (
        id INT AUTO_INCREMENT PRIMARY KEY,
        title VARCHAR(255),
        description VARCHAR(255),
        link VARCHAR(255)
    )
""")

visited_urls = set()

def crawl_page(url, depth=0):
    try:
        response = requests.get(url)
        html_content = response.text

        soup = BeautifulSoup(html_content, "html.parser")

        title = soup.title.string if soup.title else ""
        description = soup.find("meta", attrs={"name": "description"})["content"] if soup.find("meta", attrs={"name": "description"}) else ""
        link = url

        sql = "INSERT INTO testin2 (title, description, link) VALUES (%s, %s, %s)"
        values = (title, description, link)
        cursor.execute(sql, values)
        db.commit()

        print("Crawled:", url)
        print("Title:", title)
        print("Description:", description)
        print("Link:", link)
        print("--------------------------------")

        if depth < 2:
            links = soup.find_all("a")
            for link in links:
                href = link.get("href")
                if href and href.startswith("http") and href not in visited_urls:
                    visited_urls.add(href)
                    crawl_page(href, depth+1)

    except Exception as e:
        print("Error:", str(e))

if args.depth:
    crawl_page(args.start_url, depth=args.depth)
else:
    crawl_page(args.start_url)

db.close()