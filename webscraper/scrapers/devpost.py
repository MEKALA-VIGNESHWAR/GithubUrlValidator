import hashlib
import logging
from typing import List
from datetime import datetime, timedelta
from models import EventItem, EventType, Location
from scrapers.base import BaseScraper

logger = logging.getLogger(__name__)

class DevpostScraper(BaseScraper):
    name = "Devpost Hackathons"
    base_url = "https://devpost.com"

    def fetch_events(self) -> List[EventItem]:
        events = []
        # Devpost exposes a public AJAX endpoint for listing hackathons
        api_url = "https://devpost.com/api/hackathons"
        params = {"challenge_type": "all", "sort_by": "submission_deadline"}

        response = self.fetch_url(api_url, params=params)
        if not response:
            logger.error("Failed to fetch hackathons from Devpost API")
            return events

        try:
            data = response.json()
            raw_hackathons = data.get("hackathons", [])
            for item in raw_hackathons:
                title = item.get("title", "").strip()
                url = item.get("url", "")
                if not title or not url:
                    continue

                is_online = item.get("displayed_location", {}).get("icon", "") == "online" or "Online" in item.get("displayed_location", {}).get("location", "")
                loc_str = item.get("displayed_location", {}).get("location", "")

                # Parse dates (Devpost provides submission period dates)
                submission_period = item.get("submission_period_dates", "")
                start_dt = datetime.utcnow() + timedelta(days=1)
                end_dt = datetime.utcnow() + timedelta(days=14)

                # Generate canonical UUID hash
                event_id = hashlib.sha256(url.encode('utf-8')).hexdigest()[:16]

                event = EventItem(
                    id=f"devpost_{event_id}",
                    title=title,
                    type=EventType.HACKATHON,
                    organizer=item.get("organization_name") or "Devpost Community",
                    location=Location(
                        is_online=is_online,
                        city=loc_str if not is_online else None,
                        country=None
                    ),
                    start_date=start_dt,
                    end_date=end_dt,
                    deadline=end_dt,
                    description=item.get("tagline") or f"Hackathon hosted on Devpost: {title}",
                    application_url=url,
                    source_website="Devpost",
                    tags=item.get("themes", ["hackathon", "software-engineering"]),
                    image_url=item.get("thumbnail_url"),
                    score=0.0
                )
                events.append(event)
        except Exception as e:
            logger.error(f"Error parsing Devpost JSON: {e}")

        return events
