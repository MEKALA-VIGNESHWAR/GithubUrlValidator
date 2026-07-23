---
name: Dark Innovation OS
colors:
  surface: '#11131b'
  surface-dim: '#11131b'
  surface-bright: '#373942'
  surface-container-lowest: '#0c0e16'
  surface-container-low: '#191b23'
  surface-container: '#1d1f27'
  surface-container-high: '#282a32'
  surface-container-highest: '#32343d'
  on-surface: '#e1e2ed'
  on-surface-variant: '#c3c6d7'
  inverse-surface: '#e1e2ed'
  inverse-on-surface: '#2e3039'
  outline: '#8d90a0'
  outline-variant: '#434655'
  surface-tint: '#b4c5ff'
  primary: '#b4c5ff'
  on-primary: '#002a78'
  primary-container: '#2563eb'
  on-primary-container: '#eeefff'
  inverse-primary: '#0053db'
  secondary: '#ffb690'
  on-secondary: '#552100'
  secondary-container: '#ec6a06'
  on-secondary-container: '#4a1c00'
  tertiary: '#ffb596'
  on-tertiary: '#581e00'
  tertiary-container: '#bc4800'
  on-tertiary-container: '#ffede6'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#ffdbca'
  secondary-fixed-dim: '#ffb690'
  on-secondary-fixed: '#341100'
  on-secondary-fixed-variant: '#783200'
  tertiary-fixed: '#ffdbcd'
  tertiary-fixed-dim: '#ffb596'
  on-tertiary-fixed: '#360f00'
  on-tertiary-fixed-variant: '#7d2d00'
  background: '#11131b'
  on-background: '#e1e2ed'
  surface-variant: '#32343d'
typography:
  display-xl:
    fontFamily: Geist
    fontSize: 64px
    fontWeight: '800'
    lineHeight: '1.1'
    letterSpacing: -0.04em
  headline-lg:
    fontFamily: Geist
    fontSize: 32px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Geist
    fontSize: 24px
    fontWeight: '700'
    lineHeight: '1.2'
  headline-md:
    fontFamily: Geist
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.3'
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.5'
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: '1.4'
  label-caps:
    fontFamily: Geist
    fontSize: 12px
    fontWeight: '600'
    lineHeight: '1'
    letterSpacing: 0.1em
  mono-data:
    fontFamily: Geist
    fontSize: 14px
    fontWeight: '500'
    lineHeight: '1.5'
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  unit: 4px
  gutter: 24px
  margin-desktop: 48px
  margin-mobile: 16px
  container-max: 1440px
---

## Brand & Style
The design system embodies a "Mission Control" aesthetic, blending the high-stakes intensity of a hackathon with the refined precision of premium developer tools. It is engineered for elite builders, prioritizing a cinematic, immersive atmosphere that feels like a production-grade terminal.

The style is a sophisticated hybrid of **Glassmorphism** and **Minimalism**. It utilizes high-contrast typography and deep, layered depth to maintain readability during extended coding sessions. The visual language avoids traditional administrative patterns in favor of a dense, data-rich environment that feels both futuristic and highly functional.

## Colors
The palette is rooted in deep space neutrals to minimize eye strain and maximize the "void" effect of the background. 
- **Primary Blue (#2563EB)** is used for primary actions, system status, and active "Forge" states.
- **Secondary Orange (#F97316)** is reserved for high-urgency alerts, countdown timers, and "Energy" indicators.
- **Glass surfaces** provide the structural foundation, using ultra-low opacity whites to create a sense of depth without breaking the dark theme's immersion.

## Typography
Typography is treated as a functional interface element. **Geist** provides a technical, precise feel for headings and data labels, while **Inter** ensures maximum legibility for body content and documentation. 

For display headings, use tight letter-spacing to create a "compact-industrial" look. Labels should frequently utilize uppercase transforms with increased tracking to differentiate system metadata from user-generated content.

## Layout & Spacing
The layout follows an **Asymmetrical Bento Grid** philosophy. Content is organized into distinct "modules" of varying sizes that snap to a 12-column grid. 

- **Desktop:** Use wide margins (48px) and a fluid 24px gutter. 
- **Mission Control View:** Avoid long vertical scrolls; prioritize a "single-screen dashboard" where secondary information is tucked into collapsible side panels or glass drawers.
- **Mobile:** Reflow bento boxes into a single-column stack, maintaining the glass border hierarchy to separate distinct modules.

## Elevation & Depth
Depth is created through transparency and blur rather than traditional drop shadows.
- **Level 0 (Background):** Base `#050816` color.
- **Level 1 (Bento Cells):** `#0B1220` with a subtle 1px inner border.
- **Level 2 (Active Widgets/AI Panels):** Glassmorphic surfaces using `rgba(255, 255, 255, 0.04)` with a **24px backdrop-blur**.
- **Accents:** Use outer glows (bloom effect) for primary buttons and active indicators to simulate light emitting from the screen.

## Shapes
The shape language is "Soft-Technical." Use a consistent **8px (0.5rem)** radius for most containers to maintain a modern feel without appearing too consumer-grade or "bubbly." 

- **Small elements (Tags/Buttons):** 4px (0.25rem) for a sharper, more professional look.
- **Input fields:** 6px to sit comfortably between the two.
- **Focus States:** 2px offset solid stroke in Primary Blue.

## Components
- **Buttons:** Primary buttons use a solid Blue fill with a subtle "inner-glow" top border. Secondary buttons are ghost-style with a 1px glass stroke.
- **AI Widgets:** Always styled with the 24px glass blur and a shimmering 1px border gradient (Blue to Transparent).
- **Mission Control Cards:** Should feature a "header-bar" with a mono-spaced ID (e.g., `MOD_01`) and a status dot.
- **Inputs:** Darker than the container background (`#050816`) with a subtle `1px` border that illuminates on focus.
- **Progress Bars:** Thin, 4px height, using the Primary Blue for standard progress and Secondary Orange for "crunch time" (final 2 hours of the hackathon).
- **Data Tables:** Borderless rows with `bg_tertiary` hover states and mono-spaced fonts for numerical values.