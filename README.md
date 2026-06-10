# FeatherUtils

A lightweight Paper/Spigot/Folia plugin that integrates your Minecraft server with the
[Feather](https://feathermc.com) client. It adds Discord Rich Presence with a live
player count, a custom server-list background for Feather users, and a configurable
join message when a Feather player connects.

## Features

- **Discord Rich Presence** — shows your server in a player's Discord status, with a
  live `online/max` player count that updates automatically.
- **Custom server-list background** — display a PNG background for players browsing
  your server in the Feather client.
- **Feather join message** — broadcast a configurable message when a player joins
  using Feather, optionally gated behind a permission.
- **Live updates** — presence refreshes on a 5-second timer and immediately on player
  join/leave.

## Requirements

- **Paper**, **Spigot**, or **Folia** **1.21+** (the plugin uses Folia's region
  schedulers and declares `folia-supported`, so it runs natively on all three).
  Built against the 1.21.11 API but loads across the whole 1.21 line.
- [Feather Server API](https://github.com/FeatherMC/feather-server-api/releases/tag/v0.0.5)

## Installation

1. Drop `FeatherUtils.jar` into your server's `plugins/` folder.
2. Add the **Feather Server API** jar to `plugins/` as well (FeatherUtils depends on it).
3. Start or restart the server to generate the default config.
4. Edit `plugins/FeatherUtils/config.yml`, then run `/featherutils reload`.

## Configuration

All options live in `plugins/FeatherUtils/config.yml`:

| Key | Description |
| --- | --- |
| `background` | File name of the PNG placed in `plugins/FeatherUtils/backgrounds/`. |
| `discord-activity.enabled` | Toggle Discord Rich Presence on/off. |
| `discord-activity.image` | Direct link to the image shown in the activity. |
| `discord-activity.image-text` | Hover text for the image. |
| `discord-activity.state` | Top line; supports `%player_count%` and `%max_players%`. |
| `discord-activity.details` | Second line; supports the same placeholders. |
| `join-message.enabled` | Toggle the Feather join broadcast. |
| `join-message.message` | Message text; supports `%player%` and `&` color codes. |
| `join-message.permission` | If set, only players with this permission see the message. |
| `prefix` | Prefix used in command output. |

Place your background image at `plugins/FeatherUtils/backgrounds/<background>` (PNG only).

## Commands

| Command | Description |
| --- | --- |
| `/featherutils reload` | Reloads the config and re-applies the background. |
| `/featherutils help` | Shows the help menu. |

## Permissions

| Permission | Description | Default |
| --- | --- | --- |
| `featherutils.commands.use` | Use FeatherUtils commands. | op |
| `featherutils.receivemessages` | Receive Feather join messages. | true |

## Author

Veqtora
