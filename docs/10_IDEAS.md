# 💡 HackForge Innovation Backlog & Feature Ideas

## 1. High-Impact Enhancements

### 🤖 AI Pitch Deck & Demo Video Evaluator
- Automatically extract slides from uploaded PPT/PDF presentations and process audio transcripts from demo videos.
- Use LLM embeddings to score project alignment against the hackathon problem statement.

### 📜 Automated Verifiable PDF Certificates
- Auto-generate digitally signed participant and winner certificates upon hackathon completion.
- Issue verifiable credentials backed by SHA-256 hashes stored on the `leaderboard` table.

### 🏆 Peer Voting & People's Choice Category
- Allow participants to upvote projects during an open voting window.
- Protect against vote-tampering using IP rate limiting and OAuth user verification.

---

## 2. Infrastructure Expansion Ideas

### ⚡ Serverless Cloud Worker Execution
- Sandbox participant code execution using isolated WebAssembly / MicroVM runtimes (e.g. Firecracker or Docker in Docker).
- Automatically run automated integration test suites against participant GitHub repositories.
