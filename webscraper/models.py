from dataclasses import dataclass, field, asdict
from enum import Enum
from typing import List, Optional
from datetime import datetime, timezone

class EventType(str, Enum):
    INTERNSHIP = "internship"
    HACKATHON = "hackathon"
    TECH_EVENT = "tech_event"
    WEBINAR = "webinar"
    CODING_COMPETITION = "coding_competition"
    CAMPUS_EVENT = "campus_event"
    CAREER_FAIR = "career_fair"

@dataclass
class Location:
    is_online: bool = True
    city: Optional[str] = None
    country: Optional[str] = None
    venue: Optional[str] = None

    def display_location(self) -> str:
        if self.is_online:
            return "Online / Remote"
        parts = [p for p in [self.venue, self.city, self.country] if p]
        return ", ".join(parts) if parts else "TBD"

@dataclass
class EventItem:
    id: str
    title: str
    type: EventType
    organizer: str
    location: Location
    start_date: datetime
    description: str
    application_url: str
    source_website: str
    end_date: Optional[datetime] = None
    deadline: Optional[datetime] = None
    tags: List[str] = field(default_factory=list)
    image_url: Optional[str] = None
    score: float = 0.0
    created_at: datetime = field(default_factory=datetime.utcnow)

    def to_dict(self) -> dict:
        d = asdict(self)
        d['type'] = self.type.value
        d['start_date'] = self.start_date.isoformat() + "Z" if self.start_date else None
        d['end_date'] = self.end_date.isoformat() + "Z" if self.end_date else None
        d['deadline'] = self.deadline.isoformat() + "Z" if self.deadline else None
        d['created_at'] = self.created_at.isoformat() + "Z" if self.created_at else None
        return d
