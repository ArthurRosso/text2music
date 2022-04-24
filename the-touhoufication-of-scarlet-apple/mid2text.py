from mido import MidiFile, MidiTrack, Message, MetaMessage, second2tick

# Params
MIDI_OUTPUT = False
INPUT_FILE = 'mario.mid'

NOTES = ['C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B']

instructions = []

# Returns the closest BPM possible to the target and the number of increments (or decrements,
# if negative) it had to make.
def set_bpm(target, current):
  BPM_INTERVAL = 50
  diff = target - current
  increments = diff // BPM_INTERVAL
  return (current + increments*BPM_INTERVAL, increments)

midi = MidiFile(INPUT_FILE)
for msg in midi:
    if msg.time > 0:
      instructions.append({
        'type': 'delay',
        'duration': msg.time
      })

    if isinstance(msg, MetaMessage):
        continue
    else:
      if msg.type == 'note_on':
        typ = 'note_on' if msg.velocity > 0 else 'note_off'
        instructions.append({
          'type': typ,
          'note': msg.note
        })
      elif msg.type == 'note_off':
        instructions.append({
          'type': 'note_off',
          'note': msg.note
        })
      elif msg.is_cc() or msg.type == 'program_change':
        pass
      else:
        print(f'warn: Unknown message {msg.type}!')

# Add duration information on notes and remove chords
music = []
curr_note = None
time = 0
for i in range(0, len(instructions)):
  ins = instructions[i]
  if ins['type'] == 'note_on':
      # Stop playing the previous note if it was lower, or ignore incoming note if it is not.
      # This kills chords
      if curr_note != None:
        if ins['note'] > curr_note['note']:
          if time - curr_note['start'] > 0:
            curr_note['end'] = time
            music.append(curr_note)
          curr_note = ins
          ins['start'] = time
      else:
        curr_note = ins
        ins['start'] = time
  elif ins['type'] == 'note_off':
    if curr_note != None and curr_note['note'] == ins['note']:
      curr_note['end'] = time
      music.append(curr_note)
      curr_note = None
  elif ins['type'] == 'delay':
    time += ins['duration']

if MIDI_OUTPUT:
  out = MidiFile()
  track = MidiTrack()
  out.tracks.append(track)
  time = 0

  for note in music:
    s = round(second2tick(note['start'] - time, ticks_per_beat=out.ticks_per_beat, tempo=500000))
    e = round(second2tick(note['end'] - note['start'], ticks_per_beat=out.ticks_per_beat, tempo=500000))

    track.append(Message('note_on', note=note['note'], velocity=80, time=s))
    track.append(Message('note_off', note=note['note'], velocity=80, time=e))

    time = note['end']

  out.save('out.mid')
else:
  out = "t2m1 " # File header
  bpm = 60
  octave = 5
  time = 0
  for instr in music:
    # Interval between last note and current, sleep
    if time != instr['start']:
      length = instr['start'] - time
      if length < 1/880:
        length = 1/880
      target_bpm = int(60 / length)
      bpm, increments = set_bpm(target_bpm, bpm)
      if increments > 0:
        out += "BPM+" * increments
      elif increments < 0:
        out += "BPM-" * (increments*-1)
      out += '*'
      time = instr['start']

    # MIDI note to key+octave
    letters = NOTES[instr['note'] % 12]
    oct = int(instr['note'] / 12)

    # Set BPM to make note last as long as it needs
    length = instr['end'] - instr['start']
    if length < 1/880:
      length = 1/880
    target_bpm = int(60 / length)
    bpm, increments = set_bpm(target_bpm, bpm)
    if increments > 0:
      out += "BPM+" * increments
    elif increments < 0:
      out += "BPM-" * (increments*-1)

    # Set octave
    oct_diff = oct - octave
    octave = oct
    if oct_diff > 0:
      out += "!" * oct_diff
    elif oct_diff < 0:
      out += "?" * (oct_diff*-1)

    # Check if it is sharp
    note = letters[0]
    style = ''
    if (len(letters) > 1):
      if letters[1] == '#':
        style = 't' # Sharp note

    # Output note
    out += note + style

    # Advance
    time = instr['end']

  print(out)
