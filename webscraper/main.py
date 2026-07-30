# pyrefly: ignore [missing-import]
import streamlit as st
from scrape import scrape_website

st.title("AI Web Scraper")
url = st.text_input("Enter the URL of the website:")

if st.button("Scrape Site"):
    if url:
        st.write("Scraping the website...")
        result = scrape_website(url)
        if result:
            st.success("Website scraped successfully!")
            st.text_area("Scraped Content", result, height=300)
        else:
            st.error("Failed to scrape website or result was empty.")
    else:
        st.warning("Please enter a valid website URL.")