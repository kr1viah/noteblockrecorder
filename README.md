# Note Block Recorder
## Installation instructions
1. Go to the [releases](https://github.com/kr1viah/noteblockrecorder/releases) tab
2. Download the latest release
3. Put it in your mods folder
## Usage guide
This mod automatically logs any sounds that have the `RECORDS` category. This (in vanilla worlds/servers) is going to be any of the 16 note blocks (excluding mob heads). After you disconnect/save and quit the mod will put them in `notes.json` in your .minecraft folder. Use [NBSTool](https://github.com/IoeCmcomc/NBSTool/tree/master) to convert these to .nbs. You can also use NBSTool to convert them to audio files (if the .json doesn't have any custom instruments).

This mod has support for any sounds that have the `RECORDS` category, but aren't one of the 16 note blocks. It will put them under `custom_instruments` with a name, so when you convert them to .nbs and open them with [Open Note Block Studio](https://opennbs.org/) it will promt you to add the custom sounds.
## To-do list
* Add support for stereo audio
* Add support for choosing when to start/stop logging (Either through a chat command, keybind or next time you join a world)
* Add support for changing the header 
## How it works
There are 6 parts to this:
1. Getting the packet
2. The header
3. The notes
4. The layers
5. The custom instruments
6. Writing the results to a .json file
### 1. Getting the packet
I'm using fabric apis [event system](https://docs.fabricmc.net/develop/events) to make a custom event, and then use a mixin of the [ClientConnection class](https://github.com/kr1viah/noteblockrecorder/blob/main/src/client/java/org/kr1v/noteblockrecorder/client/mixin/ClientConnectionMixin.java) to check if a packet is part of the PlaySoundS2CPacket class. If it is, I check if the category is `RECORDS` and then fire the event with the packet.
### 2. The header
This consists of a bunch of data. None of this data really matters for how the notes are recorded. The `length` is just the last `note`s tick + 1. The `height` is how many layers there are. There are a few more, but they are all hardcoded (such as `loop`, `left_clicks`, `tempo`).
### 3. The notes
These consist of a `tick`, `layer`, `inst`, `key`, `vel` (volume), `pan` and `pitch`

`tick`:
  The current tick in the world

`layer`: 
  What layer the sound should appear on. 2 notes cant be on the same layer, so for every note in the same tick the layer is going to be one higher

`inst`:
  The instrument. This is either one of the 16 vanilla sounds (harp, bass, etc.) or any one of the custom sounds. In [this](https://github.com/kr1viah/noteblockrecorder/blob/main/src/client/java/org/kr1v/noteblockrecorder/client/Instruments.java) class you can see how the mod knows which instrument to use, or how it calculates which custom sound is being played.

`key`:
  Which key the note belongs to. This is calculated in [this](https://github.com/kr1viah/noteblockrecorder/blob/main/src/client/java/org/kr1v/noteblockrecorder/client/Pitch.java) class, using this formula: `key = 12 * log2(pitch) + 44`, where `pitch` is the pitch that the packet gives (In vanilla a value inbetween 0.5 and 2.0), and `key` is the key the note is in. 

`vel`: 
  I found that this was a value inbetween 0.0 and 3.0 for the packet, and NBSTool takes a value from 0 to 100. At first I did `vel = packet.getVolume() * 100/3`, but found that this was a bit quiet, so instead I increased the volume of the lower volumes, and kept the higher volumes the same. This is pretty accurate to what you hear in game. This does not have a seperate class, but it is in [NoteblockrecorderClient.java](https://github.com/kr1viah/noteblockrecorder/blob/main/src/client/java/org/kr1v/noteblockrecorder/client/NoteblockrecorderClient.java)

`pan`:
  This is the value that defines how far left or right the sound should be played at. I am planning on implementing this, since I have the tools to do so, but I have not done so yet. This value can range from -100 to 100, where 100 means 100% of the sound from the note will be on the right side, and -75 means that 75% of the sound from the note will be on the left side.

`pitch`:
  This is the leftover from rounding `key` times 100. This can only be an integer, and is 1/100th of a semitone. So for example key 45 and pitch 30 would be F#4 +30
### 4. The layers
This is the simplest of the 4, with all hardcoded values. This is because the packet does not provide any values for me to use to change these, and with how I am currently setting the `layer` this does not seem feasable ([Layer.java](https://github.com/kr1viah/noteblockrecorder/blob/main/src/client/java/org/kr1v/noteblockrecorder/client/Layer.java))
### 5. The custom instruments
Inside of [Instruments.java](https://github.com/kr1viah/noteblockrecorder/blob/main/src/client/java/org/kr1v/noteblockrecorder/client/Instruments.java) I calculate what the sound is, and if it's a custom instrument. If it is, then I add it to the list customSounds. 
### 6. Writing the results to a .json file
At first I was not planning on adding custom instrument suppport, but found out that NBSTool *needs* a `custom_instruments` list in the .json, so I thought why not. NBSTool takes 4 arguments: `header`, `notes`, `layers`, and `custom_instruments` which I explained above. Lastly, to put it all togheter, I used Gson to write the results to .json (in [this](https://github.com/kr1viah/noteblockrecorder/blob/main/src/client/java/org/kr1v/noteblockrecorder/client/JsonWriter.java) class). First, I create the header, then, using `highestlayer` I calculated in [NoteblockrecorderClient](https://github.com/kr1viah/noteblockrecorder/blob/main/src/client/java/org/kr1v/noteblockrecorder/client/NoteblockrecorderClient.java) I do a for loop to create enough layers that every note fits. After that, I make a new jsonStructure with the header, notes, layers and custom_instruments and then use Gson to write it to `notes.json` in your .minecraft folder.
