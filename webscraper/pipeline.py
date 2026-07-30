import json
import logging
from typing import List, Dict, Set
from datetime import datetime, timezone
from models import EventItem
from scrapers import DevpostScraper, GithubInternshipsScraper, TechEventsScraper

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(name)s: %(message)s")
logger = logging.getLogger("EventPipeline")

class EventPipeline:
    """
    Core data pipeline that orchestrates fetching, deduplication,
    expired item filtering, relevance scoring/ranking, and JSON normalization.
    """

    def __init__(self, user_interests: List[str] = None):
        self.scrapers = [
            DevpostScraper(),
            GithubInternshipsScraper(),
            TechEventsScraper()
        ]
        self.user_interests = [i.lower() for i in (user_interests or ["software", "python", "java", "react", "ai", "cloud"])]

    def run(self) -> List[EventItem]:
        logger.info("Starting Event Scraper Data Pipeline...")
        raw_events: List[EventItem] = []

        # 1. Fetch from all sources
        for scraper in self.scrapers:
            try:
                logger.info(f"Running scraper: {scraper.name}")
                items = scraper.fetch_events()
                logger.info(f"Fetched {len(items)} raw events from {scraper.name}")
                raw_events.extend(items)
            except Exception as e:
                logger.error(f"Error executing scraper {scraper.name}: {e}", exc_info=True)

        logger.info(f"Total raw events collected across all sources: {len(raw_events)}")

        # 2. Filter out expired items
        valid_events = self.filter_expired(raw_events)
        logger.info(f"Events remaining after filtering expired items: {len(valid_events)}")

        # 3. Deduplicate
        deduped_events = self.deduplicate(valid_events)
        logger.info(f"Events remaining after deduplication: {len(deduped_events)}")

        # 4. Rank & Score items
        ranked_events = self.rank_and_score(deduped_events)

        logger.info("Pipeline processing completed successfully!")
        return ranked_events

    def filter_expired(self, events: List[EventItem]) -> List[EventItem]:
        """Filter out events whose deadline or start_date has passed."""
        now = datetime.utcnow()
        valid = []
        for event in events:
            # Check deadline if present
            if event.deadline and event.deadline.replace(tzinfo=None) < now:
                logger.debug(f"Filtering out expired deadline item: {event.title}")
                continue
            # Check end date or start date
            effective_end = event.end_date or event.start_date
            if effective_end and effective_end.replace(tzinfo=None) < now:
                logger.debug(f"Filtering out past event item: {event.title}")
                continue
            valid.append(event)
        return valid

    def deduplicate(self, events: List[EventItem]) -> List[EventItem]:
        """Deduplicate listings by canonical URL and title+organizer hash."""
        seen_urls: Set[str] = set()
        seen_title_hash: Set[str] = set()
        unique_events: List[EventItem] = []

        for event in events:
            # Clean URL
            clean_url = event.application_url.strip().rstrip("/").lower()
            if clean_url in seen_urls:
                continue

            # Title + Organizer fuzzy hash
            title_organizer_key = f"{event.title.lower()}_{event.organizer.lower()}".replace(" ", "")
            if title_organizer_key in seen_title_hash:
                continue

            seen_urls.add(clean_url)
            seen_title_hash.add(title_organizer_key)
            unique_events.append(event)

        return unique_events

    def rank_and_score(self, events: List[EventItem]) -> List[EventItem]:
        """
        Rank items by calculating a relevance score:
        - Freshness: Higher score for upcoming events (nearer start/deadline date)
        - Interest Match: Tags matching user_interests boost score
        - Completeness: Bonus for having images, location, and full description
        """
        now = datetime.utcnow()

        for event in events:
            score = 50.0  # Base score

            # 1. Freshness Score (max +30 pts)
            days_until_start = (event.start_date.replace(tzinfo=None) - now).days
            if 0 <= days_until_start <= 7:
                score += 30.0  # Happening this week!
            elif 7 < days_until_start <= 30:
                score += 20.0  # Happening this month
            elif days_until_start > 30:
                score += 10.0

            # 2. Tag & User Interest Match (max +25 pts)
            matched_tags = sum(1 for tag in event.tags if any(interest in tag for interest in self.user_interests))
            score += min(matched_tags * 5.0, 25.0)

            # 3. Quality & Completeness (max +15 pts)
            if event.image_url:
                score += 5.0
            if len(event.description) > 50:
                score += 5.0
            if event.location.is_online or event.location.city:
                score += 5.0

            event.score = round(score, 2)

        # Sort descending by score
        return sorted(events, key=lambda x: x.score, reverse=True)

    def to_json(self, events: List[EventItem]) -> str:
        """Serialize list of EventItems to JSON."""
        return json.dumps([e.to_dict() for e in events], indent=2)

if __name__ == "__main__":
    pipeline = EventPipeline()
    events = pipeline.run()
    json_output = pipeline.to_json(events)
    print("\n--- SAMPLE PIPELINE JSON OUTPUT ---")
    print(json_output)
