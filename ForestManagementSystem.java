import java.io.*;
import java.util.*;

// ===================== MODEL CLASSES =====================

// Forest Zone Class
class ForestZone implements Serializable {
    String zoneId;
    String zoneName;
    String officer;

    ForestZone(String zoneId, String zoneName, String officer) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.officer = officer;
    }

    public String toString() {
        return "Zone ID: " + zoneId +
               ", Name: " + zoneName +
               ", Officer: " + officer;
    }
}

// Tree Class
class Tree implements Serializable {
    String treeId;
    String species;
    int age;

    Tree(String treeId, String species, int age) {
        this.treeId = treeId;
        this.species = species;
        this.age = age;
    }

    public String toString() {
        return "Tree ID: " + treeId +
               ", Species: " + species +
               ", Age: " + age + " years";
    }
}

// Wildlife Sighting Class
class Wildlife implements Serializable {
    String animal;
    String location;
    String date;

    Wildlife(String animal, String location, String date) {
        this.animal = animal;
        this.location = location;
        this.date = date;
    }

    public String toString() {
        return "Animal: " + animal +
               ", Location: " + location +
               ", Date: " + date;
    }
}

// ===================== MAIN SYSTEM =====================

public class ForestManagementSystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<ForestZone> zones = new ArrayList<>();
    static ArrayList<Tree> trees = new ArrayList<>();
    static ArrayList<Wildlife> wildlifeList = new ArrayList<>();

    // --------------- File Save & Load ----------------
    static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("forest.dat"))) {
            oos.writeObject(zones);
            oos.writeObject(trees);
            oos.writeObject(wildlifeList);
            System.out.println("Data saved successfully!\n");
        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }

    static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("forest.dat"))) {
            zones = (ArrayList<ForestZone>) ois.readObject();
            trees = (ArrayList<Tree>) ois.readObject();
            wildlifeList = (ArrayList<Wildlife>) ois.readObject();
        } catch (Exception e) {
            System.out.println("No previous data found.\n");
        }
    }

    // ---------------- CRUD OPERATIONS -----------------

    static void addZone() {
        System.out.print("Enter Zone ID: ");
        String id = sc.next();
        System.out.print("Enter Zone Name: ");
        String name = sc.next();
        System.out.print("Enter Officer Name: ");
        String officer = sc.next();

        zones.add(new ForestZone(id, name, officer));
        System.out.println("Zone Added Successfully!\n");
    }

    static void addTree() {
        System.out.print("Enter Tree ID: ");
        String id = sc.next();
        System.out.print("Enter Species: ");
        String species = sc.next();
        System.out.print("Enter Age: ");
        int age = sc.nextInt();

        trees.add(new Tree(id, species, age));
        System.out.println("Tree Added Successfully!\n");
    }

    static void addWildlife() {
        System.out.print("Enter Animal: ");
        String animal = sc.next();
        System.out.print("Enter Location: ");
        String location = sc.next();
        System.out.print("Enter Date (DD-MM-YYYY): ");
        String date = sc.next();

        wildlifeList.add(new Wildlife(animal, location, date));
        System.out.println("Wildlife Record Added Successfully!\n");
    }

    static void viewZones() {
        System.out.println("\n--- Forest Zones ---");
        for (ForestZone z : zones) System.out.println(z);
    }

    static void viewTrees() {
        System.out.println("\n--- Trees ---");
        for (Tree t : trees) System.out.println(t);
    }

    static void viewWildlife() {
        System.out.println("\n--- Wildlife Sightings ---");
        for (Wildlife w : wildlifeList) System.out.println(w);
    }

    // ---------------- SEARCH --------------------
    static void searchTree() {
        System.out.print("Enter Tree ID to Search: ");
        String id = sc.next();

        for (Tree t : trees) {
            if (t.treeId.equals(id)) {
                System.out.println("Record Found: " + t + "\n");
                return;
            }
        }
        System.out.println("No Tree Found!\n");
    }

    // ================== MAIN MENU ==================

    public static void main(String[] args) {
        loadData();

        int choice;
        while (true) {
            System.out.println("\n====== FOREST MANAGEMENT SYSTEM ======");
            System.out.println("1. Add Forest Zone");
            System.out.println("2. Add Tree");
            System.out.println("3. Add Wildlife Sighting");
            System.out.println("4. View Zones");
            System.out.println("5. View Trees");
            System.out.println("6. View Wildlife Records");
            System.out.println("7. Search Tree");
            System.out.println("8. Save & Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input! Enter a number.\n");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1: addZone(); break;
                case 2: addTree(); break;
                case 3: addWildlife(); break;
                case 4: viewZones(); break;
                case 5: viewTrees(); break;
                case 6: viewWildlife(); break;
                case 7: searchTree(); break;
                case 8: saveData(); System.exit(0);
                default: System.out.println("Invalid Choice!\n");
            }
        }
    }
}
