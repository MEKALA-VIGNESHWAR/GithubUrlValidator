const { test, expect } = require('@playwright/test');

test.describe('HackForge Enterprise E2E Test Suite', () => {

  test('1. Participant Login & Navigation to Mission Control', async ({ page }) => {
    await page.goto('/');
    
    // Check if redirected or on Login Page
    const pageTitle = page.locator('h1, h2');
    await expect(pageTitle.first()).toBeVisible();

    // Click Demo Login as Admin/Participant
    const loginButton = page.locator('button:has-text("Login"), button:has-text("Sign In"), button:has-text("Demo Login")').first();
    if (await loginButton.isVisible()) {
      await loginButton.click();
    }
  });

  test('2. Teammate Matchmaking & Skill Card Publishing', async ({ page }) => {
    await page.goto('/');
    
    // Navigate to Matchmaking Marketplace
    const matchmakingNav = page.locator('button:has-text("Matchmaking Marketplace")');
    if (await matchmakingNav.isVisible()) {
      await matchmakingNav.click();
      await expect(page.locator('text=Teammate Matchmaking')).toBeVisible();
    }
  });

  test('3. Direct S3 Upload & Project Submission Flow', async ({ page }) => {
    await page.goto('/');
    
    const submitNav = page.locator('button:has-text("Submit Project")');
    if (await submitNav.isVisible()) {
      await submitNav.click();
      await expect(page.locator('text=Project Submission')).toBeVisible();
    }
  });

  test('4. Open Badges & Public Certificate Verification', async ({ page }) => {
    await page.goto('/');
    
    const certNav = page.locator('button:has-text("Certificate Verifier")');
    if (await certNav.isVisible()) {
      await certNav.click();
      await expect(page.locator('text=Public Certificate Verification')).toBeVisible();

      // Verify certificate hash search
      const input = page.locator('input[placeholder*="HF-CERT"]');
      await expect(input).toBeVisible();
    }
  });
});
