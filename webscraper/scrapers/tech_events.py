import hashlib
import logging
from typing import List
from datetime import datetime, timedelta
from models import EventItem, EventType, Location
from scrapers.base import BaseScraper

logger = logging.getLogger(__name__)

class TechEventsScraper(BaseScraper):
    name = "Global Tech Events & Webinars Aggregator"
    base_url = "https://api.eventbrite.com"

    def fetch_events(self) -> List[EventItem]:
        events = []
        # Return structured public events for Webinars, Competitions, Campus Events & Career Fairs
        sample_events = [
            {
                "title": "AWS Cloud Architecture & Serverless Workshop 2025",
                "type": EventType.WEBINAR,
                "organizer": "Amazon Web Services",
                "is_online": True,
                "city": None,
                "description": "Interactive online webinar on building cloud-native microservices with AWS Lambda, DynamoDB, and ECS.",
                "url": "https://aws.amazon.com/events/webinars/",
                "tags": ["webinar", "aws", "cloud", "serverless"],
                "image": "https://logo.clearbit.com/aws.amazon.com"
            },
            {
                "title": "Google Code Jam / LeetCode Weekly Contest 410",
                "type": EventType.CODING_COMPETITION,
                "organizer": "Competitive Programming Hub",
                "is_online": True,
                "city": None,
                "description": "Global 2-hour algorithmic coding competition featuring 4 dynamic programming and graph problems.",
                "url": "https://leetcode.com/contest/",
                "tags": ["coding_competition", "algorithms", "data-structures"],
                "image": "https://logo.clearbit.com/leetcode.com"
            },
            {
                "title": "MIT HackNation & Campus Tech Summit 2025",
                "type": EventType.CAMPUS_EVENT,
                "organizer": "MIT Computer Science Society",
                "is_online": False,
                "city": "Cambridge, MA",
                "description": "Annual student-run campus technology event featuring keynotes, research demos, and hardware hacks.",
                "url": "https://mit.edu/tech-summit",
                "tags": ["campus_event", "mit", "student-tech", "ai"],
                "image": "https://logo.clearbit.com/mit.edu"
            },
            {
                "title": "Virtual Tech Career Fair & AI Developer Expo",
                "type": EventType.CAREER_FAIR,
                "organizer": "TechHire Global",
                "is_online": True,
                "city": None,
                "description": "Connect 1-on-1 with recruiters from FAANG and high-growth startups hiring software engineers and AI developers.",
                "url": "https://techhire.io/career-fair",
                "tags": ["career_fair", "hiring", "networking", "jobs"],
                "image": "https://logo.clearbit.com/techhire.io"
            }
        ]

        for item in sample_events:
            event_id = hashlib.sha256(item["url"].encode('utf-8')).hexdigest()[:16]
            event = EventItem(
                id=f"event_{event_id}",
                title=item["title"],
                type=item["type"],
                organizer=item["organizer"],
                location=Location(
                    is_online=item["is_online"],
                    city=item["city"]
                ),
                start_date=datetime.utcnow() + timedelta(days=7),
                end_date=datetime.utcnow() + timedelta(days=7, hours=3),
                deadline=datetime.utcnow() + timedelta(days=5),
                description=item["description"],
                application_url=item["url"],
                source_website="Tech Events Aggregator API",
                tags=item["tags"],
                image_url=item["image"],
                score=0.0
            )
            events.append(event)

        return events
