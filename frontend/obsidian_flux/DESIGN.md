---
name: Obsidian Flux
colors:
  surface: '#0e1322'
  surface-dim: '#0e1322'
  surface-bright: '#343949'
  surface-container-lowest: '#090e1c'
  surface-container-low: '#161b2a'
  surface-container: '#1a1f2e'
  surface-container-high: '#252939'
  surface-container-highest: '#2f3444'
  on-surface: '#dee2f7'
  on-surface-variant: '#ccc3d8'
  inverse-surface: '#dee2f7'
  inverse-on-surface: '#2b3040'
  outline: '#958da1'
  outline-variant: '#4a4455'
  surface-tint: '#d2bbff'
  primary: '#d2bbff'
  on-primary: '#3f008e'
  primary-container: '#7c3aed'
  on-primary-container: '#ede0ff'
  inverse-primary: '#732ee4'
  secondary: '#89ceff'
  on-secondary: '#00344d'
  secondary-container: '#00a2e6'
  on-secondary-container: '#00344e'
  tertiary: '#ffb784'
  on-tertiary: '#4f2500'
  tertiary-container: '#a15100'
  on-tertiary-container: '#ffe0cd'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#eaddff'
  primary-fixed-dim: '#d2bbff'
  on-primary-fixed: '#25005a'
  on-primary-fixed-variant: '#5a00c6'
  secondary-fixed: '#c9e6ff'
  secondary-fixed-dim: '#89ceff'
  on-secondary-fixed: '#001e2f'
  on-secondary-fixed-variant: '#004c6e'
  tertiary-fixed: '#ffdcc6'
  tertiary-fixed-dim: '#ffb784'
  on-tertiary-fixed: '#301400'
  on-tertiary-fixed-variant: '#713700'
  background: '#0e1322'
  on-background: '#dee2f7'
  surface-variant: '#2f3444'
typography:
  display-lg:
    fontFamily: Geist
    fontSize: 48px
    fontWeight: '800'
    lineHeight: '1.1'
    letterSpacing: -0.04em
  headline-md:
    fontFamily: Geist
    fontSize: 32px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-sm:
    fontFamily: Geist
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.3'
  body-lg:
    fontFamily: Geist
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Geist
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.5'
  label-mono:
    fontFamily: JetBrains Mono
    fontSize: 13px
    fontWeight: '500'
    lineHeight: '1.4'
    letterSpacing: 0.05em
  headline-md-mobile:
    fontFamily: Geist
    fontSize: 28px
    fontWeight: '700'
    lineHeight: '1.2'
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 28px
  xs: 7px
  sm: 14px
  md: 28px
  lg: 56px
  xl: 84px
  gutter: 28px
---

## Brand & Style

The design system is an advanced iteration of the "Dark Innovation OS" aesthetic, evolved for high-density enterprise environments that require both deep focus and technical sophistication. It is built for developers, data scientists, and power users who navigate complex information landscapes.

The style is **Atmospheric Glassmorphism**. It utilizes multi-layered transparency, high-fidelity backdrop blurs, and luminous accents to simulate a multi-dimensional digital workspace. This creates a sense of "infinite canvas" where depth is used semantically to separate system-level tools from user-generated content. The emotional response is one of precision, "flow state" immersion, and quiet power.

## Colors

The palette is anchored by a deep, desaturated navy-black background. Unlike standard flat dark modes, this system uses "Obsidian" depth, where the background serves as a void for luminous glass panels.

- **Primary & AI:** Purple serves as the core "intelligence" identifier, used for AI features and primary actions.
- **Surface Strategy:** Surfaces are rarely opaque. Use `rgba(20, 25, 40, 0.55)` for containers, layered over a very subtle background gradient to maintain depth during scrolling.
- **Accents:** Semantic colors should be used with a 10% opacity background fill and a 100% opacity stroke for "glow" indicators.

## Typography

This design system leverages **Geist** for its mathematical precision and Swiss-inspired clarity. To maintain hierarchy in a dark, translucent environment, the system relies on aggressive weight contrast rather than size alone.

- **Contrast:** Use "Black" (800) or "Bold" (700) weights for all headers to ensure they punch through the backdrop blur.
- **Technical Accents:** Use **JetBrains Mono** for metadata, labels, and code snippets to reinforce the "OS" and technical nature of the platform.
- **Readability:** Body text should maintain a 400 weight; avoid "Thin" or "Light" weights for body copy as they lose legibility against glass backgrounds.

## Layout & Spacing

The "Enterprise Breathe" philosophy dictates a generous use of negative space to reduce cognitive load. 

- **Grid:** A 12-column fluid grid with 28px gutters. 
- **The 28px Rule:** All primary margins and container paddings should default to 28px (`spacing.md`). This creates a consistent rhythm that feels more luxurious and intentional than standard 16px layouts.
- **Responsive:** On mobile, margins compress to 14px, but internal component padding should remain spacious to maintain touch-target integrity.

## Elevation & Depth

Depth is the primary communicator of state and importance.

- **Backdrop Blur:** Every floating container must apply a `20px` backdrop-filter blur.
- **Borders:** Surfaces are defined by a `1px` solid border (`rgba(255, 255, 255, 0.08)`). Top borders can be slightly lighter (0.12 opacity) to simulate a top-down light source.
- **Shadows:** Use deep, expansive shadows for elevated elements: `0 20px 80px rgba(0, 0, 0, 0.45)`.
- **Z-Index Strategy:**
    - Level 0: Background.
    - Level 1: Main content cards (Glass).
    - Level 2: Modals / Command Palette (Higher Blur, Darker Overlay).
    - Level 3: Tooltips / Popovers (Luminous borders).

## Shapes

The design system uses a "Sophisticated Geometric" radius. 

- **Standard Containers:** Use 0.5rem (8px) for standard inputs and buttons.
- **Feature Cards:** Use `rounded-xl` (1.5rem / 24px) to distinguish main content blocks and AI panels from utility UI.
- **Command Palette:** Uses 1rem (16px) to signify its status as a system-level tool.

## Components

### Interaction & Motion
- **Hover Lift:** Interactive cards should lift -4px on the Y-axis and increase border opacity to `0.2`.
- **Glow Borders:** On hover, primary buttons and active AI inputs should trigger a soft `2px` outer glow using their respective accent color.
- **Staggered Entrance:** Lists and grid items must enter via a `20px` slide-up fade with a 50ms stagger per item.

### Specialized Components
- **Command Palette (Cmd+K):** A centered modal with a 40px backdrop blur. The input field is borderless, using only a `body-lg` font size. Results are categorized with `label-mono` headers.
- **AI Assistant Panel:** A persistent right-hand drawer. It uses a unique "AI Pulse" border—a gradient stroke that subtly animates between Primary and AI-Purple.
- **Workspace Switcher:** A compact, square-format button in the sidebar. On click, it expands into a grid of icons with "Glassmorphic" tooltips identifying the workspace names.
- **Inputs:** Darker than the container (`rgba(0,0,0,0.2)`) with a focus state that changes the border to `primary_color_hex`.