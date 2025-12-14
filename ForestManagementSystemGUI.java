import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// ===================== MODEL CLASSES =====================

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
        return "Zone ID: " + zoneId + ", Name: " + zoneName + ", Officer: " + officer;
    }
}

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
        return "Tree ID: " + treeId + ", Species: " + species + ", Age: " + age + " years";
    }
}

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
        return "Animal: " + animal + ", Location: " + location + ", Date: " + date;
    }
}

// ===================== CUSTOM BUTTON CLASS =====================

class GreenHoverButton extends JButton {
    private Color normalColor = new Color(34, 139, 34);
    private Color hoverColor = new Color(0, 180, 0);
    private Color pressColor = new Color(0, 100, 0);

    public GreenHoverButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setFocusPainted(false);
        setBorder(new RoundedBorder(10, normalColor));
        setContentAreaFilled(false);
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressColor);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }
        });
    }
}

// ===================== ROUNDED BORDER CLASS =====================

class RoundedBorder extends AbstractBorder {
    private int radius;
    private Color color;

    public RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 10, 5, 10);
    }
}

// ===================== MAIN GUI CLASS =====================

public class ForestManagementSystemGUI extends JFrame {
    private ArrayList<ForestZone> zones = new ArrayList<>();
    private ArrayList<Tree> trees = new ArrayList<>();
    private ArrayList<Wildlife> wildlifeList = new ArrayList<>();

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private DefaultTableModel zonesTableModel;
    private DefaultTableModel treesTableModel;
    private DefaultTableModel wildlifeTableModel;

    public ForestManagementSystemGUI() {
        setTitle("🌲 Forest Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Load data
        loadData();

        // Create GUI
        createGUI();
        setVisible(true);
    }

    private void createGUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 255, 240));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(220, 245, 220));
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));

        tabbedPane.addTab("🌲 Zones", createZonesPanel());
        tabbedPane.addTab("🌳 Trees", createTreesPanel());
        tabbedPane.addTab("🦁 Wildlife", createWildlifePanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setBackground(new Color(34, 139, 34));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("🌲 FOREST MANAGEMENT SYSTEM 🌲");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        header.add(titleLabel);
        return header;
    }

    private JPanel createZonesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 255, 240));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(new Color(220, 245, 220));
        inputPanel.setBorder(new TitledBorder(new LineBorder(new Color(34, 139, 34), 2), "Add New Zone", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12), new Color(34, 139, 34)));

        JTextField zoneIdField = new JTextField();
        JTextField zoneNameField = new JTextField();
        JTextField officerField = new JTextField();

        styleTextField(zoneIdField);
        styleTextField(zoneNameField);
        styleTextField(officerField);

        inputPanel.add(new JLabel("Zone ID:"));
        inputPanel.add(zoneIdField);
        inputPanel.add(new JLabel("Zone Name:"));
        inputPanel.add(zoneNameField);
        inputPanel.add(new JLabel("Officer Name:"));
        inputPanel.add(officerField);

        GreenHoverButton addZoneBtn = new GreenHoverButton("Add Zone");
        addZoneBtn.addActionListener(e -> {
            if (zoneIdField.getText().isEmpty() || zoneNameField.getText().isEmpty() || officerField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                zones.add(new ForestZone(zoneIdField.getText(), zoneNameField.getText(), officerField.getText()));
                refreshZonesTable();
                zoneIdField.setText("");
                zoneNameField.setText("");
                officerField.setText("");
                JOptionPane.showMessageDialog(this, "Zone Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        inputPanel.add(addZoneBtn);

        // Table Panel
        zonesTableModel = new DefaultTableModel(new String[]{"Zone ID", "Zone Name", "Officer"}, 0);
        JTable zonesTable = new JTable(zonesTableModel);
        stylesTable(zonesTable);
        JScrollPane scrollPane = new JScrollPane(zonesTable);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshZonesTable();
        return panel;
    }

    private JPanel createTreesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 255, 240));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(220, 245, 220));
        inputPanel.setBorder(new TitledBorder(new LineBorder(new Color(34, 139, 34), 2), "Add New Tree", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12), new Color(34, 139, 34)));

        JTextField treeIdField = new JTextField();
        JTextField speciesField = new JTextField();
        JTextField ageField = new JTextField();

        styleTextField(treeIdField);
        styleTextField(speciesField);
        styleTextField(ageField);

        inputPanel.add(new JLabel("Tree ID:"));
        inputPanel.add(treeIdField);
        inputPanel.add(new JLabel("Species:"));
        inputPanel.add(speciesField);
        inputPanel.add(new JLabel("Age (years):"));
        inputPanel.add(ageField);

        GreenHoverButton addTreeBtn = new GreenHoverButton("Add Tree");
        addTreeBtn.addActionListener(e -> {
            if (treeIdField.getText().isEmpty() || speciesField.getText().isEmpty() || ageField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int age = Integer.parseInt(ageField.getText());
                    trees.add(new Tree(treeIdField.getText(), speciesField.getText(), age));
                    refreshTreesTable();
                    treeIdField.setText("");
                    speciesField.setText("");
                    ageField.setText("");
                    JOptionPane.showMessageDialog(this, "Tree Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        inputPanel.add(addTreeBtn);

        // Table Panel
        treesTableModel = new DefaultTableModel(new String[]{"Tree ID", "Species", "Age"}, 0);
        JTable treesTable = new JTable(treesTableModel);
        stylesTable(treesTable);
        JScrollPane scrollPane = new JScrollPane(treesTable);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshTreesTable();
        return panel;
    }

    private JPanel createWildlifePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 255, 240));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(new Color(220, 245, 220));
        inputPanel.setBorder(new TitledBorder(new LineBorder(new Color(34, 139, 34), 2), "Add Wildlife Sighting", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12), new Color(34, 139, 34)));

        JTextField animalField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField dateField = new JTextField();

        styleTextField(animalField);
        styleTextField(locationField);
        styleTextField(dateField);

        inputPanel.add(new JLabel("Animal:"));
        inputPanel.add(animalField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Date (DD-MM-YYYY):"));
        inputPanel.add(dateField);

        GreenHoverButton addWildlifeBtn = new GreenHoverButton("Add Wildlife");
        addWildlifeBtn.addActionListener(e -> {
            if (animalField.getText().isEmpty() || locationField.getText().isEmpty() || dateField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                wildlifeList.add(new Wildlife(animalField.getText(), locationField.getText(), dateField.getText()));
                refreshWildlifeTable();
                animalField.setText("");
                locationField.setText("");
                dateField.setText("");
                JOptionPane.showMessageDialog(this, "Wildlife Record Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        inputPanel.add(addWildlifeBtn);

        // Table Panel
        wildlifeTableModel = new DefaultTableModel(new String[]{"Animal", "Location", "Date"}, 0);
        JTable wildlifeTable = new JTable(wildlifeTableModel);
        stylesTable(wildlifeTable);
        JScrollPane scrollPane = new JScrollPane(wildlifeTable);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshWildlifeTable();
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel();
        footer.setBackground(new Color(220, 245, 220));
        footer.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        GreenHoverButton saveBtn = new GreenHoverButton("💾 Save Data");
        saveBtn.addActionListener(e -> {
            saveData();
            JOptionPane.showMessageDialog(this, "Data Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        GreenHoverButton exitBtn = new GreenHoverButton("❌ Exit");
        exitBtn.setBackground(new Color(200, 50, 50));
        exitBtn.addActionListener(e -> {
            saveData();
            System.exit(0);
        });

        footer.add(saveBtn);
        footer.add(exitBtn);
        return footer;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBorder(new LineBorder(new Color(34, 139, 34), 2));
        field.setBackground(Color.WHITE);
    }

    private void stylesTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(34, 139, 34));
        table.setSelectionBackground(new Color(144, 238, 144));
        table.setSelectionForeground(new Color(34, 139, 34));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(34, 139, 34));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void refreshZonesTable() {
        zonesTableModel.setRowCount(0);
        for (ForestZone zone : zones) {
            zonesTableModel.addRow(new Object[]{zone.zoneId, zone.zoneName, zone.officer});
        }
    }

    private void refreshTreesTable() {
        treesTableModel.setRowCount(0);
        for (Tree tree : trees) {
            treesTableModel.addRow(new Object[]{tree.treeId, tree.species, tree.age});
        }
    }

    private void refreshWildlifeTable() {
        wildlifeTableModel.setRowCount(0);
        for (Wildlife wildlife : wildlifeList) {
            wildlifeTableModel.addRow(new Object[]{wildlife.animal, wildlife.location, wildlife.date});
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("forest.dat"))) {
            oos.writeObject(zones);
            oos.writeObject(trees);
            oos.writeObject(wildlifeList);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("forest.dat"))) {
            zones = (ArrayList<ForestZone>) ois.readObject();
            trees = (ArrayList<Tree>) ois.readObject();
            wildlifeList = (ArrayList<Wildlife>) ois.readObject();
        } catch (Exception e) {
            // No previous data found
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ForestManagementSystemGUI());
    }
}
