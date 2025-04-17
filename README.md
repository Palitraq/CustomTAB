# CustomTAB

A lightweight Fabric(for now) mod that enhances your server's TAB list with customizable header and footer.

![Modrinth Downloads](https://img.shields.io/modrinth/dt/customtab)
![Minecraft Version](https://img.shields.io/badge/Minecraft-1.19-green)

## Features
- 🎨 Fully customizable header and footer
- 📊 Real-time server statistics display
- 🌈 Support for all Minecraft color codes
- ⚡ Server-side only (no client installation required)
- 🔧 Easy configuration through JSON file

## Installation
1. Install [Fabric Loader >=0.16.9](https://fabricmc.net/use/)
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download CustomTAB from [Modrinth](https://modrinth.com/mod/customtab) or [CurseForge](https://curseforge.com/minecraft/mc-mods/customtab)
4. Place the mod in your `mods` folder
5. Start the server

## Configuration
The configuration file is located at:
```
.minecraft/config/customtab.json
```

### Available Formatting
```
§0 is black
§1 is dark blue
§2 is dark green
§3 is dark aqua
§4 is dark red
§5 is dark purple
§6 is gold
§7 is gray
§8 is dark gray
§9 is blue
§a is green
§b is aqua
§c is red
§d is light purple
§e is yellow
§f is white
---
§k is obfuscated
§l is bold
§m is strikethrough
§n is underline
§o is italic
---
\n - new line
%TPS% - shows server TPS
%PLAYERS% - shows online players count
```

### Example Configuration
```json
{
    "enabled": true,
    "header": "§6§lCustomTAB\n§eTPS: %TPS%\n§aPlayers: %PLAYERS%",
    "footer": "§e§lMade with §c§l❤"
}
```

## Building from Source
1. Clone the repository
```bash
git clone -b 1.19 https://github.com/Palitraq/CustomTAB.git

2. Build the project

./gradlew build
```

3. Find the compiled jar in `build/libs`

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support
If you encounter any issues or have questions:
1. Check the [Issues](https://github.com/Palitraq/CustomTAB/issues) page
2. Create a new issue if your problem isn't already listed 
