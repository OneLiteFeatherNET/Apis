# Apis
The project is the Minestom implementation of the DungeonGenerator. This extension reads the output from the
python generator and generates the dungeon with some schematics.

# Dungeon Generator (Interface)
The structure of a generation is defined in the DungeonGenerator interface and the basic implementation is covered
by the BaseGenerator class.
The BaseGenerator contains the implementation to read a dungeon from a file and recreates the floor plan from it.

# DebugGenerator


# Limitations

The current implementation has some restrictions which are important:

- The floor id is limited to **5** (the value scales the amount of rooms)
- The room scale is limited to **4** (which means a room contains **16** Chunks)
- 
