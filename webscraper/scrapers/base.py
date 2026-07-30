import time
import logging
from abc import ABC, abstractmethod
from typing import List, Optional
import requests
from urllib.parse import urlparse
from urllib.robotparser import RobotFileParser
from models import EventItem

logger = logging.getLogger(__name__)

class BaseScraper(ABC):
    """
    Abstract Base Class for all event scrapers/fetchers.
    Enforces ethical scraping practices: user-agent, robots.txt compliance, and rate limiting.
    """
    name: str = "BaseScraper"
    base_url: str = ""
    rate_limit_delay: float = 1.0  # seconds between requests

    def __init__(self, user_agent: str = "TechEventsScraperBot/1.0 (+https://github.com/my-org/tech-events)"):
        self.user_agent = user_agent
        self.session = requests.Session()
        self.session.headers.update({"User-Agent": self.user_agent})
        self._last_request_time = 0.0
        self.robot_parser = RobotFileParser()
        self._load_robots_txt()

    def _load_robots_txt(self):
        if not self.base_url:
            return
        parsed = urlparse(self.base_url)
        robots_url = f"{parsed.scheme}://{parsed.netloc}/robots.txt"
        try:
            self.robot_parser.set_url(robots_url)
            self.robot_parser.read()
        except Exception as e:
            logger.warning(f"Could not fetch robots.txt for {self.base_url}: {e}")

    def is_allowed(self, url: str) -> bool:
        """Check if robots.txt allows scraping this URL."""
        if not self.base_url:
            return True
        try:
            return self.robot_parser.can_fetch(self.user_agent, url)
        except Exception:
            return True

    def fetch_url(self, url: str, params: Optional[dict] = None, max_retries: int = 3) -> Optional[requests.Response]:
        """Fetch URL with rate limiting, robots.txt check, and exponential backoff."""
        if not self.is_allowed(url):
            logger.warning(f"Access to {url} dis-allowed by robots.txt")
            return None

        # Rate limiting delay
        elapsed = time.time() - self._last_request_time
        if elapsed < self.rate_limit_delay:
            time.sleep(self.rate_limit_delay - elapsed)

        for attempt in range(1, max_retries + 1):
            try:
                response = self.session.get(url, params=params, timeout=10)
                self._last_request_time = time.time()
                if response.status_code == 200:
                    return response
                elif response.status_code in (429, 503):
                    wait = 2 ** attempt
                    logger.warning(f"Rate limited/Service unavailable ({response.status_code}). Retrying in {wait}s...")
                    time.sleep(wait)
                else:
                    logger.error(f"HTTP {response.status_code} for {url}")
                    return None
            except Exception as e:
                logger.error(f"Attempt {attempt} failed for {url}: {e}")
                time.sleep(2 ** attempt)
        return None

    @abstractmethod
    def fetch_events(self) -> List[EventItem]:
        """Fetch and parse events into structured EventItem models."""
        pass
