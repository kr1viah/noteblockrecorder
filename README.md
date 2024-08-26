# Note Block Recorder
## Installation instructions
1. Go to the [releases](https://github.com/kr1viah/noteblockrecorder/releases) tab
2. Download the latest release
3. Put it in your mods folder
## Usage guide
This mod automatically logs any sounds that have the `RECORDS` category. This (in vanilla worlds/servers) is going to be any of the 15 note blocks (excluding mob heads). After you disconnect/save and quit the mod will put them in `notes.json` in your .minecraft folder. Use [NBSTool](https://github.com/IoeCmcomc/NBSTool/tree/master) to convert these to .nbs. You can also use NBSTool to convert them to audio files.

This mod has support for any sounds that have the `RECORDS` category, but aren't one of the 15 note blocks. It will put them under `custom_instruments` with a name, so when you convert them to .nbs and open them with [Open Note Block Studio](https://opennbs.org/) it will promt you with the option to add the custom sounds.
## To-do list
* Add support for stereo audio
* Add support for choosing when to start/stop logging (Either through a chat command, keybind or next time you join a world)
* Add support for changing the header 
