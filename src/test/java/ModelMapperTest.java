import lombok.experimental.ExtensionMethod;
import org.example.ModelExtensions;
import org.example.dto.CollectionsDto;
import org.example.dto.NestedCollectionsDto;
import org.example.dto.NestedDto;
import org.example.model.CollectionsModel;
import org.example.model.SimpleModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtensionMethod(ModelExtensions.class)
public class ModelMapperTest {
    @Test
    public void simpleMap() {
        var model = SimpleModel.builder().string("string").integer(3).bool(true).notOnDTO("Mismatch").build();
        //var mapper = new ModelMapper();

        var result = model.toDtoUnchanged();

        assertEquals("string", result.getString());
        assertNull(result.getNotOnModel());
        assertEquals(3, result.getInteger());
        assertTrue(result.isBool());
    }

    @Test
    public void simpleMapRenamed() {
        var model = SimpleModel.builder().string("string").integer(3).bool(true).notOnDTO("Mismatch").build();

        var result = model.toDto();

        assertEquals("string", result.getString());
        assertEquals("Mismatch", result.getNotOnModel());
        assertEquals(3, result.getInteger());
        assertTrue(result.isBool());
    }

    @Test
    public void FlatToNested() {
        var model = SimpleModel.builder().string("string").integer(3).bool(true).notOnDTO("Mismatch").build();

        var result = model.toNestedDto();

        assertEquals("string", result.getInside().getString());
        assertEquals("Mismatch", result.getInside().getNotOnModel());
        assertEquals(3, result.getInteger());
        assertTrue(result.getInside().isBool());
    }

    @Test
    public void FlatListToNestedList(){
        var modelList = new ArrayList<SimpleModel>();
        modelList.add(SimpleModel.builder().string("string1").integer(1).bool(true).notOnDTO("Mismatch1").build());
        modelList.add(SimpleModel.builder().string("string2").integer(2).bool(false).notOnDTO("Mismatch2").build());
        modelList.add(SimpleModel.builder().string("string3").integer(3).bool(true).notOnDTO("Mismatch3").build());

        var result = ModelExtensions.mapList(modelList, NestedDto.class);

        assertEquals("string1", result.get(0).getInside().getString());
        assertEquals("Mismatch1", result.get(0).getInside().getNotOnModel());
        assertEquals(1, result.get(0).getInteger());
        assertTrue(result.get(0).getInside().isBool());

        assertEquals("string2", result.get(1).getInside().getString());
        assertEquals("Mismatch2", result.get(1).getInside().getNotOnModel());
        assertEquals(2, result.get(1).getInteger());
        assertFalse(result.get(1).getInside().isBool());

        assertEquals("string3", result.get(2).getInside().getString());
        assertEquals("Mismatch3", result.get(2).getInside().getNotOnModel());
        assertEquals(3, result.get(2).getInteger());
        assertTrue(result.get(2).getInside().isBool());
    }

    @Test
    public void FlatListToNestedListConverter(){
        var modelList = new ArrayList<SimpleModel>();
        modelList.add(SimpleModel.builder().string("string1").integer(1).bool(true).notOnDTO("Mismatch1").build());
        modelList.add(SimpleModel.builder().string("string2").integer(2).bool(false).notOnDTO("Mismatch2").build());
        modelList.add(SimpleModel.builder().string("string3").integer(3).bool(true).notOnDTO("Mismatch3").build());

        var result = ModelExtensions.mapList(modelList, NestedDto.class);

        assertEquals("string1", result.get(0).getInside().getString());
        assertEquals("Mismatch1", result.get(0).getInside().getNotOnModel());
        assertEquals(1, result.get(0).getInteger());
        assertTrue(result.get(0).getInside().isBool());

        assertEquals("string2", result.get(1).getInside().getString());
        assertEquals("Mismatch2", result.get(1).getInside().getNotOnModel());
        assertEquals(2, result.get(1).getInteger());
        assertFalse(result.get(1).getInside().isBool());

        assertEquals("string3", result.get(2).getInside().getString());
        assertEquals("Mismatch3", result.get(2).getInside().getNotOnModel());
        assertEquals(3, result.get(2).getInteger());
        assertTrue(result.get(2).getInside().isBool());
    }

    @Test
    public void CollectionModelToCollectionDto(){
        var modelList = new ArrayList<SimpleModel>();
        var modelSet = new HashSet<SimpleModel>();

        var item1 = SimpleModel.builder().string("string1").integer(1).bool(true).notOnDTO("Mismatch1").build();
        var item2 = SimpleModel.builder().string("string2").integer(2).bool(false).notOnDTO("Mismatch2").build();
        var item3 = SimpleModel.builder().string("string3").integer(3).bool(true).notOnDTO("Mismatch3").build();

        modelList.add(item1);
        modelList.add(item2);
        modelList.add(item3);

        modelSet.add(item1);
        modelSet.add(item2);
        modelSet.add(item3);

        var collectionModel = CollectionsModel.builder()
                .objectList(modelList)
                .objectSet(modelSet)
                .build();

        var result = ModelExtensions.mapper.map(collectionModel, CollectionsDto.class);

        assertEquals(item1.getString(), result.getObjectList().get(0).getString());
        assertEquals(item1.getNotOnDTO(), result.getObjectList().get(0).getNotOnModel());
        assertEquals(item1.getInteger(), result.getObjectList().get(0).getInteger());
        assertEquals(item1.isBool(), result.getObjectList().get(0).isBool());
        assertEquals(item2.getString(), result.getObjectList().get(1).getString());
        assertEquals(item2.getNotOnDTO(), result.getObjectList().get(1).getNotOnModel());
        assertEquals(item2.getInteger(), result.getObjectList().get(1).getInteger());
        assertEquals(item2.isBool(), result.getObjectList().get(1).isBool());
        assertEquals(item3.getString(), result.getObjectList().get(2).getString());
        assertEquals(item3.getNotOnDTO(), result.getObjectList().get(2).getNotOnModel());
        assertEquals(item3.getInteger(), result.getObjectList().get(2).getInteger());
        assertEquals(item3.isBool(), result.getObjectList().get(2).isBool());

        var dto1 = result.getObjectSet().stream().filter(x -> x.getInteger() == item1.getInteger()).findFirst().get();
        assertEquals(item1.getString(), dto1.getString());
        assertEquals(item1.getNotOnDTO(), dto1.getNotOnModel());
        assertEquals(item1.isBool(), dto1.isBool());
        var dto2 = result.getObjectSet().stream().filter(x -> x.getInteger() == item2.getInteger()).findFirst().get();
        assertEquals(item2.getString(), dto2.getString());
        assertEquals(item2.getNotOnDTO(), dto2.getNotOnModel());
        assertEquals(item2.isBool(), dto2.isBool());
        var dto3 = result.getObjectSet().stream().filter(x -> x.getInteger() == item3.getInteger()).findFirst().get();
        assertEquals(item3.getString(), dto3.getString());
        assertEquals(item3.getNotOnDTO(), dto3.getNotOnModel());
        assertEquals(item3.isBool(), dto3.isBool());
    }

    @Test
    public void CollectionModelToNestedCollectionDto(){
        var modelList = new ArrayList<SimpleModel>();
        var modelSet = new HashSet<SimpleModel>();
        var item1 = SimpleModel.builder().string("string1").integer(1).bool(true).notOnDTO("Mismatch1").build();
        var item2 = SimpleModel.builder().string("string2").integer(2).bool(false).notOnDTO("Mismatch2").build();
        var item3 = SimpleModel.builder().string("string3").integer(3).bool(true).notOnDTO("Mismatch3").build();

        modelList.add(item1);
        modelList.add(item2);
        modelList.add(item3);

        modelSet.add(item1);
        modelSet.add(item2);
        modelSet.add(item3);

        var collectionModel = CollectionsModel.builder()
                .objectList(modelList)
                .objectSet(modelSet)
                .build();

        var result = ModelExtensions.mapper.map(collectionModel, NestedCollectionsDto.class);


        assertEquals(item1.getString(), result.getObjectList().get(0).getInside().getString());
        assertEquals(item1.getNotOnDTO(), result.getObjectList().get(0).getInside().getNotOnModel());
        assertEquals(item1.getInteger(), result.getObjectList().get(0).getInteger());
        assertEquals(item1.isBool(), result.getObjectList().get(0).getInside().isBool());
        assertEquals(item2.getString(), result.getObjectList().get(1).getInside().getString());
        assertEquals(item2.getNotOnDTO(), result.getObjectList().get(1).getInside().getNotOnModel());
        assertEquals(item2.getInteger(), result.getObjectList().get(1).getInteger());
        assertEquals(item2.isBool(), result.getObjectList().get(1).getInside().isBool());
        assertEquals(item3.getString(), result.getObjectList().get(2).getInside().getString());
        assertEquals(item3.getNotOnDTO(), result.getObjectList().get(2).getInside().getNotOnModel());
        assertEquals(item3.getInteger(), result.getObjectList().get(2).getInteger());
        assertEquals(item3.isBool(), result.getObjectList().get(2).getInside().isBool());

        var dto1 = result.getObjectSet().stream().filter(x -> x.getInteger() == item1.getInteger()).findFirst().get();
        assertEquals(item1.getString(), dto1.getInside().getString());
        assertEquals(item1.getNotOnDTO(), dto1.getInside().getNotOnModel());
        assertEquals(item1.isBool(), dto1.getInside().isBool());
        var dto2 = result.getObjectSet().stream().filter(x -> x.getInteger() == item2.getInteger()).findFirst().get();
        assertEquals(item2.getString(), dto2.getInside().getString());
        assertEquals(item2.getNotOnDTO(), dto2.getInside().getNotOnModel());
        assertEquals(item2.isBool(), dto2.getInside().isBool());
        var dto3 = result.getObjectSet().stream().filter(x -> x.getInteger() == item3.getInteger()).findFirst().get();
        assertEquals(item3.getString(), dto3.getInside().getString());
        assertEquals(item3.getNotOnDTO(), dto3.getInside().getNotOnModel());
        assertEquals(item3.isBool(), dto3.getInside().isBool());
    }
}
