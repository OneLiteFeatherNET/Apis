package net.theevilreaper.apis.api.data;

import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoomDTOTest {

    @Test
    void testRecord() {
        var roomData = new RoomData(0, 0, RoomType.NORMAL, new DoorFace[0]);
        var schematicPath = Paths.get("test.schem");
        var regionPath = Paths.get("test.region");
        
        var dto = new RoomDTO(roomData, schematicPath, regionPath);
        
        assertNotNull(dto);
        assertEquals(roomData, dto.roomData());
        assertEquals(schematicPath, dto.schematicPath());
        assertEquals(regionPath, dto.regionPath());
    }
}
