
import models.Group;
import models.Configuration;
import models.Generative;
import models.Item;
import models.Stock;
import service.Storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static service.Storage.*;

public class MenuMain {
    public static final String DELIMITER = ",";
    public static void main(String[] args) {
        boolean isMenuActive = true;
        Group group = new Group("1", "a");
        Group group1 = new Group("2", "b");
        Group group2 = new Group("3", "c");
        Storage.addGroup(group, "1", Storage.getRoots(), true);
        Storage.addGroup(group1, "2", Storage.getRoots(), true);
        Storage.addGroup(group2, "3", Storage.getRoots(), true);
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\User\\IdeaProjects\\tomcat_test\\src\\main\\resources\\item.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                addItem(new Stock(values,Configuration.Resolution.HD),values[4],getRoots());
            }
        } catch (IOException e) {
            throw new AppRuntimeException();

        }
        System.out.println(findHighestPricedItem());
        System.out.println(findAveragePriceRateInTheGroup(group));
        System.out.println(findHighestPricedItemInTheDirectGroup(group1));

    }

    private static void generateItem(Scanner sc) {
        boolean isMenuActive = true;
        while (isMenuActive) {
            System.out.println("type item name or continue");
            String command = sc.nextLine();
            switch (command) {
                case "continue":
                    return;
                default:
                    if (command.isEmpty()) {
                        break;
                    }
                    System.out.println("what type of item do you want to create 1.generative 2.stock");
                    int i = sc.nextInt();
                    sc.nextLine();
                    Item item;
                    System.out.println("type price");
                    double price = sc.nextDouble();
                    sc.nextLine();//new line
                    System.out.println("choose configuration 1.HD 2.FHD 3.4K");
                    int x = sc.nextInt();
                    sc.nextLine();
                    Configuration.Resolution r;
                    switch (x) {
                        case 2:
                            r = Configuration.Resolution.FHD;
                            break;
                        case 3:
                            r = Configuration.Resolution.K4;
                            break;
                        default:
                            r = Configuration.Resolution.HD;
                            break;
                    }
                    Configuration c = new Configuration(r);
                    if (i == 1) {
                        System.out.println("choose complexity from 1-2");
                        double complexity = sc.nextDouble();
                        sc.nextLine();//for new line
                        item = new Generative(UUID.randomUUID().toString(), command, complexity, price, c);
                    } else {
                        item = new Stock(UUID.randomUUID().toString(), command, price, c);
                    }
                    System.out.println("Type Parent id");
                    String parentId = sc.nextLine();


                    boolean isAdded = Storage.addItem(item, parentId, Storage.getRoots());
                    if (isAdded) {
                        System.out.print("Created item: ");
                        System.out.println(item);
                        basket.addToBasket(item);
                    } else {
                        System.out.printf("Parent id not found: %s%n", parentId);
                    }
            }
        }
    }
}