# Minecraft Plugin: Bocik

Bocik is a Minecraft plugin for Paper servers that opens a protected portal travel GUI and searches for the nearest Nether portal within 64 blocks when the player starts the search.

## Install

1. Build the plugin with Java 21 and Maven:

   ```bash
   mvn package
   ```

2. Copy the generated `plugin.jar` file from the `target` directory into your Paper server's `plugins` directory.

3. Start or restart the Paper server.

## Use

1. Join the server as a player.
2. Run:

   ```text
   /bocik
   ```

3. Click the green `START` item in the center slot of the three-row chest GUI.
4. The plugin searches the surrounding world for Nether portal blocks.
5. When a portal is found, the player is teleported to a position just above it.
6. The GUI cancels inventory interactions while it is open, preventing players from taking its items.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/a4e167a7-3bf8-45aa-8ead-48be862ea965?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=a4e167a7-3bf8-45aa-8ead-48be862ea965
