# Shield Status+

**Shield Status+** is a lightweight Fabric client-side mod that tracks and displays shield disable cooldowns, giving you clear awareness of when your shield is broken and when it will be usable again.

## Why Use Shield Status+?
Shield Status+ uses a different approach to shield detection than Walksy's mod allowing it to work on any server and any version. For example Shield Status+ works on 1.21.8 servers such as mcpvp.club

This implementation remains fully functional even when the client and server run different Minecraft versions

This version of Shield Status does not replace any of Minecraft’s original shield textures. Instead, it renders a transparent overlay on top of the existing texture, keeping the game’s visuals fully intact.

## Features
- **Shield Cooldown Tracking**  
  Automatically detects when any shield is disabled and shows the remaining cooldown time through color.

- **Shield Interpolation**  
  Shield Interpolation gradually transitions the shield’s disabled color back to its enabled color, giving you a visual sense of how far along the cooldown is. Instead of instantly swapping colors, the overlay smoothly shifts from red to green over the 5-second cooldown

- **Lightweight & Efficient**  
  Built using minimal packet listening and event hooks to ensure smooth performance on any server.

**Shield Interpolation Enabled:**

![A quick video of the Shield Interpolation feature](https://cdn.modrinth.com/data/cached_images/acb1fd7a87df620d83bcc7476c2a92affdf8e14a.gif)

## Commands

ShieldStatus includes a few simple client-side commands for toggling visuals:

### **/shieldstatus** (alias: **/ss**)
Main command for managing ShieldStatus settings.

#### **/shieldstatus toggleoverlay**
Toggles the on-screen shield disable overlay.  
- Shows/hides the cooldown indicator.  
- Sends a chat message confirming whether it’s **enabled** or **disabled**.

#### **/shieldstatus toggleinterpolation**
Toggles cooldown interpolation.  
- Smoothly animates the cooldown instead of updating in fixed steps.  
- Chat feedback confirms whether it’s **enabled** or **disabled**.
