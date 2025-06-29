# Apis
The project is the Minestom implementation of the DungeonGenerator. This extension reads the output from the
python generator and generates the dungeon with some schematics.

> [!NOTE]
> The current state of the project is more **experminental** than **stable**.
> It is not recommended to use this in production servers.

# Limitations

The current implementation has some restrictions which are important:

- The floor id is limited to **5** (the value scales the amount of rooms)
- The room scale is limited to **4** (which means a room contains **16** Chunks)
