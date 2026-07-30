# pyrefly: ignore [missing-import]
import selenium.webdriver as webdriver
from selenium.webdriver.chrome.service import Service
import time

def scrape_website(website):
    print(f"Launching chrome browser for {website}...")

    options = webdriver.ChromeOptions()
    # Auto-managed driver in Selenium 4+
    driver = webdriver.Chrome(options=options)

    try:
        driver.get(website)
        print("Page loaded, waiting for dynamic content to render...")
        time.sleep(5)  # Wait BEFORE capturing page_source so JavaScript renders
        html = driver.page_source
        return html
    except Exception as e:
        print(f"Error scraping website: {e}")
        return ""
    finally:
        driver.quit()