# run-dev.ps1 - Loads .env.local and starts Spring Boot
# Usage: .\run-dev.ps1  (from the backend/ directory)

$envFile = Join-Path $PSScriptRoot ".env.local"

if (Test-Path $envFile) {
    foreach ($line in Get-Content $envFile) {
        if ($line -notmatch '^\s*#' -and $line -match '=') {
            $parts = $line -split '=', 2
            $key   = $parts[0].Trim()
            $value = $parts[1].Trim()
            [System.Environment]::SetEnvironmentVariable($key, $value, "Process")
            Write-Host "  Set $key"
        }
    }
    Write-Host ""
    Write-Host "Environment loaded from .env.local"
} else {
    Write-Host "WARNING: .env.local not found"
}

Write-Host ""
Write-Host "Starting Spring Boot on port 3000..."
mvn spring-boot:run
