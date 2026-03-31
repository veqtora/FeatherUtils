# FeatherUtils

A Minecraft plugin heavily inspired by `SimpleFeatherIntigration` with extra features that integrates with the Feather client.

## Features
- Discord Rich Presence with live player count
- Player count updates every 5 seconds
- Custom server list background
- Join message for Feather users

## Requirements
- Paper/Spigot 1.21+
- [Feather Server API](https://github.com/FeatherMC/feather-server-api/releases/tag/v0.0.5)

## Installation
1. Drop the jar into your server's `plugins/` folder
2. Also add the Feather Server API jar to `plugins/`
3. Restart your server
4. Edit `plugins/FeatherUtils/config.yml`

## Commands
- `/featherutils reload` - Reloads config and background
- `/featherutils help` - Shows help menu

## Permissions
- `featherutils.commands.use` - Allows use of commands (default: op)
- `featherutils.receivemessages` - Receive Feather join messages (default: true)

## Author
- Veqtora
