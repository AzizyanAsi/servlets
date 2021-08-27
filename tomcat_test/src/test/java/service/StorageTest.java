package service;


import models.Configuration;
import models.Group;
import models.Item;
import models.Stock;
import org.junit.jupiter.api.*;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;

import static models.Configuration.Resolution.FHD;
import static service.Storage.addItem;
import static service.Storage.getRoots;

public class StorageTest {
//    @ParameterizedTest
//    @CsvFileSource(resources = "/item.csv")
    public void csv(String id, double price, String name, String image, String pId) {
        Assertions.assertNotNull(id);
        Assertions.assertTrue(price > 0);
        Assertions.assertNotNull(name);
        Assertions.assertNotNull(image);
        Assertions.assertNotNull(pId);
        Stock stock = new Stock(id, name, image, price, Configuration.Resolution.HD);
        Storage.addItem(stock, pId, Storage.getRoots());
        ItemNotFoundException e1 = Assertions.assertThrows(ItemNotFoundException.class,
                () -> Storage.findItemByTitle(stock.getName()));


    }


    @Test
    @DisplayName("add group")
    public void create() {
        Group g = new Group("23", "my");
        int size = Storage.groups.size();
        Storage.addGroup(g, "3", Storage.getRoots(), true);
        Assertions.assertEquals(size + 1, Storage.groups.size());
    }

    @Test
    public void read() {
        Group g = new Group("23", "myo");
        Storage.addGroup(g, "3", Storage.getRoots(), true);
        Group s = Storage.findGroupByName(g.getName()).get();
        Assertions.assertNotNull(s);
        Assertions.assertEquals(g.getName(), s.getName());
        Assertions.assertEquals(g.getId(), s.getId());

    }

    @Test
    public void delete() {
        Stock stock = new Stock("1", "blabla", "wow", 200, FHD);
        Storage.addItem(stock, "2", Storage.getRoots());
        Assertions.assertThrows(ItemNotFoundException.class, () -> Storage.deleteItem("1"));


    }

    @Test
    public void update() {
        Stock stock = new Stock("1", "nune", "smth", 200, FHD);
        Storage.addItem(stock, "2", Storage.getRoots());
        Assertions.assertThrows(ItemNotFoundException.class, () -> Storage.updateItem("1", "nap"));


    }


}
