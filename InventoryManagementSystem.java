import java.io.*;
import java.util.*;

class Product implements Serializable {
    private int id;
    private String name;
    private int quantity;
    private double price;

    public Product(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void addStock(int amount) {
        this.quantity += amount;
    }

    public void reduceStock(int amount) {
        if (amount <= quantity)
            this.quantity -= amount;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Quantity: %d | Price: %.2f",
                id, name, quantity, price);
    }
}

class InventoryManager {
    private List<Product> products;
    private static final String FILE_NAME = "inventory.dat";

    public InventoryManager() {
        products = new ArrayList<>();
        loadFromFile();
    }

    public void addProduct(Product p) {
        products.add(p);
        saveToFile();
    }

    public void displayProducts() {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        for (Product p : products) {
            System.out.println(p);
        }
    }

    public Product searchById(int id) {
        for (Product p : products) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }

    public void updateStock(int id, int quantity, boolean add) {
        Product p = searchById(id);
        if (p != null) {
            if (add)
                p.addStock(quantity);
            else
                p.reduceStock(quantity);
            saveToFile();
        } else {
            System.out.println("Product not found.");
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(products);
        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    public void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            products = (List<Product>) in.readObject();
        } catch (Exception e) {
            products = new ArrayList<>();
        }
    }
}

public class InventoryManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InventoryManager manager = new InventoryManager();

        while (true) {
            System.out.println("\n=== Inventory Management System ===");
            System.out.println("1. Add Product");
            System.out.println("2. Display Products");
            System.out.println("3. Search Product by ID");
            System.out.println("4. Update Stock");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();  // clear buffer
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();
                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();
                    manager.addProduct(new Product(id, name, qty, price));
                    break;

                case 2:
                    manager.displayProducts();
                    break;

                case 3:
                    System.out.print("Enter ID to search: ");
                    id = sc.nextInt();
                    Product found = manager.searchById(id);
                    if (found != null) {
                        System.out.println("Product Found: " + found);
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter ID: ");
                    id = sc.nextInt();
                    System.out.print("Enter Quantity: ");
                    qty = sc.nextInt();
                    System.out.print("Add or Remove (A/R): ");
                    char op = sc.next().toUpperCase().charAt(0);
                    manager.updateStock(id, qty, op == 'A');
                    break;

                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}