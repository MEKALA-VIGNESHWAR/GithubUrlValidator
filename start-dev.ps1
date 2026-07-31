# HackForge Development Launcher
# Starts Spring Boot Backend first, waits for startup, then launches Vite Frontend.

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition

Write-Host "🚀 Launching HackForge Fullstack Development Environment..." -ForegroundColor Cyan

# 1. Start Backend in a new window
Write-Host "⚡ Step 1: Starting Spring Boot Backend (Port 8080)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$projectRoot\backend'; Write-Host 'Starting Spring Boot Backend...' -ForegroundColor Cyan; mvn spring-boot:run -DskipTests"

# 2. Give backend a few seconds to initialize
Write-Host "⌛ Step 2: Waiting 5 seconds for backend initialization..." -ForegroundColor DarkGray
Start-Sleep -Seconds 5

# 3. Start Frontend in a new window
Write-Host "🎨 Step 3: Starting Vite React Frontend (Port 5173)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$projectRoot\frontend'; Write-Host 'Starting Vite Frontend...' -ForegroundColor Green; npm run dev"

Write-Host "✅ Fullstack environment launched successfully!" -ForegroundColor Green
Write-Host "🌐 Access Frontend: http://localhost:5173" -ForegroundColor White
Write-Host "⚙️ Access Backend API: http://localhost:8080/api/v1" -ForegroundColor White
