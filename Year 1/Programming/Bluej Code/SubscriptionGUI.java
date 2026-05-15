import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.*;

public class SubscriptionGUI extends JFrame {
    private ArrayList<AIModel> models = new ArrayList<>();
    private final String TXT_FILE = "subscriptions.txt"; 

    // Theme Colors
    private final Color BACKGROUND_GREY = new Color(60, 63, 65); 
    private final Color PANEL_GREY = new Color(75, 78, 80);      
    private final Color TEXT_WHITE = Color.WHITE;
    private final Color BUTTON_COLOR = new Color(90, 93, 95);

    // UI Fields
    private JTextField nameField, priceField, paramField, contextField;
    private JTextField quotaField, teamField, indexField, promptField, tokenField, memberField;
    private JTextArea outputArea;

    public SubscriptionGUI() {
        setTitle("AI Model Subscription System - Shrawan karki");
        setSize(1100, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BACKGROUND_GREY);

        //  GLOBAL configuratioins
        JPanel globalPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        setupPanel(globalPanel, "  Model Details ");
        nameField = new JTextField(); priceField = new JTextField();
        paramField = new JTextField(); contextField = new JTextField();
        addLabelAndField(globalPanel, "Model Name:", nameField);
        addLabelAndField(globalPanel, "Price (NPR):", priceField);
        addLabelAndField(globalPanel, "Parameters (B):", paramField);
        addLabelAndField(globalPanel, "Max Context:", contextField);

        
        JPanel hubsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        hubsPanel.setOpaque(false);

        JPanel personalHub = new JPanel(new GridLayout(3, 1, 10, 10));
        setupPanel(personalHub, "  Personal Plan ");
        quotaField = new JTextField();
        JButton btnAddPersonal = new JButton("Add Personal Plan");
        styleButton(btnAddPersonal);
        personalHub.add(createWhiteLabel("Monthly Prompt Limit:")); 
        personalHub.add(quotaField); 
        personalHub.add(btnAddPersonal);

        JPanel proHub = new JPanel(new GridLayout(3, 1, 10, 10));
        setupPanel(proHub, " Pro Plan ");
        teamField = new JTextField();
        JButton btnAddPro = new JButton("Add Pro Plan");
        styleButton(btnAddPro);
        proHub.add(createWhiteLabel("Team Capacity:")); 
        proHub.add(teamField); 
        proHub.add(btnAddPro);
        
        hubsPanel.add(personalHub); hubsPanel.add(proHub);

        // ---  OPERATIONS & FILE MANAGEMENT ---
        JPanel bottomContainer = new JPanel(new BorderLayout(10, 10));
        bottomContainer.setOpaque(false);
        
        JPanel opsMasterPanel = new JPanel(new GridLayout(4, 1, 0, 5)); 
        setupPanel(opsMasterPanel, "  Operations and File Management ");

        // Row 1: Prompting
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5)); 
        row1.setOpaque(false);
        indexField = new JTextField(5); promptField = new JTextField(15); 
        tokenField = new JTextField(5);
        JButton btnPrompt = new JButton("Process Prompt"); 
        styleButton(btnPrompt);
        row1.add(createWhiteLabel("Target Index:")); 
        row1.add(indexField);
        row1.add(createWhiteLabel("Prompt:")); 
        row1.add(promptField);
        row1.add(createWhiteLabel("Tokens:")); 
        row1.add(tokenField); 
        row1.add(btnPrompt);

        // Row 2: Member Management
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5)); 
        row2.setOpaque(false);
        memberField = new JTextField(10);
        JButton btnAddMember = new JButton("Add Member"); 
        styleButton(btnAddMember);
        row2.add(createWhiteLabel("Member Name:")); 
        row2.add(memberField); 
        row2.add(btnAddMember);

        // Row 3: Check Type
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5)); 
        row3.setOpaque(false);
        JButton btnCheckType = new JButton("Check Plan Type");
        styleButton(btnCheckType);
        row3.add(createWhiteLabel("Check Plan at Index Above:")); 
        row3.add(btnCheckType);

        // Row 4: File I/O
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5)); 
        row4.setOpaque(false);
        JButton btnExport = new JButton("Export to TXT"); 
        JButton btnLoad = new JButton("Load From TXT");
        JButton btnDisplay = new JButton("Display All"); 
        JButton btnClear = new JButton("Clear All Fields");
        styleButton(btnExport); styleButton(btnLoad); 
        styleButton(btnDisplay); styleButton(btnClear);
        row4.add(btnExport); row4.add(btnLoad); 
        row4.add(new JLabel(" | ")); 
        row4.add(btnDisplay); 
        row4.add(btnClear);

        opsMasterPanel.add(row1); 
        opsMasterPanel.add(row2); 
        opsMasterPanel.add(row3); 
        opsMasterPanel.add(row4);

        outputArea = new JTextArea(12, 50);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(40, 42, 44));
        outputArea.setForeground(new Color(171, 178, 191));
        JScrollPane scroll = new JScrollPane(outputArea);

        bottomContainer.add(opsMasterPanel, BorderLayout.NORTH);
        bottomContainer.add(scroll, BorderLayout.CENTER);

        JPanel topWrapper = new JPanel(new BorderLayout(10, 10));
        topWrapper.setOpaque(false);
        topWrapper.add(globalPanel, BorderLayout.NORTH);
        topWrapper.add(hubsPanel, BorderLayout.CENTER);
        add(topWrapper, BorderLayout.NORTH);
        add(bottomContainer, BorderLayout.CENTER);

        // --- ACTION LISTENERS ---

        btnAddPersonal.addActionListener(e -> addPersonalLogic());
        btnAddPro.addActionListener(e -> addProLogic());
        btnPrompt.addActionListener(e -> processPromptLogic());
        btnAddMember.addActionListener(e -> addMemberLogic());

        btnCheckType.addActionListener(e -> {
            try {
                int idx = Integer.parseInt(indexField.getText());
                checkPlanType(idx);
            } catch (NumberFormatException ex) { showError("Please enter a valid numeric Index first!"); }
        });

        btnExport.addActionListener(e -> {
            if (models.isEmpty()) { showError("Nothing to export!"); return; }
            try (PrintWriter writer = new PrintWriter(new FileWriter(TXT_FILE))) {
                for (AIModel m : models) {
                    writer.println(m.showDetails());
                    writer.println("------------------------------------");
                }
                JOptionPane.showMessageDialog(null, "Data Exported to TXT!");
            } catch (IOException ex) { showError("Export Failed!"); }
        });

        btnLoad.addActionListener(e -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(TXT_FILE))) {
                outputArea.setText("--- RETRIEVED FROM FILE ---\n\n");
                String line;
                while ((line = reader.readLine()) != null) { outputArea.append(line + "\n"); }
            } catch (Exception ex) { showError("Load Error: File not found."); }
        });

        btnDisplay.addActionListener(e -> displayLogic());
        btnClear.addActionListener(e -> clearLogic());

        setVisible(true);
    }

    // --- FULL LOGIC IMPLEMENTATION ---

    private void addPersonalLogic() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int params = Integer.parseInt(paramField.getText());
            int context = Integer.parseInt(contextField.getText());
            int quota = Integer.parseInt(quotaField.getText());

            if (name.isEmpty()) throw new Exception("Name cannot be empty");

            models.add(new PersonalPlan(name, price, params, context, quota));
            outputArea.setText("SYSTEM: Personal Plan added successfully at Index " + (models.size() - 1));
        } catch (Exception ex) {
            showError("Invalid Input! Please fill all Model details and Monthly Limit correctly.");
        }
    }

    private void addProLogic() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int params = Integer.parseInt(paramField.getText());
            int context = Integer.parseInt(contextField.getText());
            int teamCap = Integer.parseInt(teamField.getText());

            if (name.isEmpty()) throw new Exception("Name cannot be empty");

            models.add(new ProPlan(name, price, params, context, teamCap));
            outputArea.setText("SYSTEM: Pro Plan added successfully at Index " + (models.size() - 1));
        } catch (Exception ex) {
            showError("Invalid Input! Please fill all Model details and Team Capacity correctly.");
        }
    }

    private void processPromptLogic() {
        try {
            int idx = Integer.parseInt(indexField.getText());
            String prompt = promptField.getText();
            int tokens = Integer.parseInt(tokenField.getText());

            if (idx >= 0 && idx < models.size()) {
                String result = models.get(idx).processPrompt(prompt, tokens);
                outputArea.setText("LOG: Processing Index " + idx + "...\n" + result);
            } else {
                showError("Index out of range!");
            }
        } catch (Exception ex) {
            showError("Please enter valid Index, Prompt, and Token count.");
        }
    }

    private void addMemberLogic() {
        try {
            int idx = Integer.parseInt(indexField.getText());
            String memberName = memberField.getText();

            if (idx >= 0 && idx < models.size()) {
                AIModel selected = models.get(idx);
                if (selected instanceof ProPlan) {
                    String result = ((ProPlan) selected).includeMember(memberName);
                    outputArea.setText(result);
                } else {
                    showError("This index is a Personal Plan. Members can only be added to Pro Plans!");
                }
            } else {
                showError("Invalid Index!");
            }
        } catch (Exception ex) {
            showError("Enter a numeric Index and a Member Name.");
        }
    }

    private void displayLogic() {
        if (models.isEmpty()) {
            outputArea.setText("The list is currently empty.");
            return;
        }
        outputArea.setText("--- CURRENT REGISTERED MODELS ---\n\n");
        for (int i = 0; i < models.size(); i++) {
            outputArea.append("Index [" + i + "]: " + models.get(i).showDetails() + "\n");
            outputArea.append("--------------------------------------------------\n");
        }
    }

    private void clearLogic() {
        nameField.setText(""); 
        priceField.setText("");
        paramField.setText(""); 
        contextField.setText("");
        quotaField.setText("");
        teamField.setText("");
        indexField.setText(""); 
        promptField.setText("");
        tokenField.setText(""); 
        memberField.setText("");
        outputArea.setText("");
    }

    private void checkPlanType(int index) {
        if (index < 0 || index >= models.size()) {
            showError("Index Out of Bounds! No plan exists at index " + index);
            return;
        }
        AIModel selectedPlan = models.get(index);
        if (selectedPlan instanceof PersonalPlan) {
            outputArea.setText("RESULT: Index " + index + " is a [ Personal Plan ].");
            JOptionPane.showMessageDialog(this, "Plan Type: Personal Plan");
        } else if (selectedPlan instanceof ProPlan) {
            outputArea.setText("RESULT: Index " + index + " is a [ Pro Plan ].");
            JOptionPane.showMessageDialog(this, "Plan Type: Pro Plan");
        }
    }

    // Styling Helpers
    private void setupPanel(JPanel p, String title) {
        p.setBackground(PANEL_GREY);
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title, 0, 0, null, TEXT_WHITE));
    }
    
    private void styleButton(JButton b) { b.setBackground(BUTTON_COLOR);
        b.setForeground(TEXT_WHITE); b.setFocusPainted(false); 
    }
    
    private JLabel createWhiteLabel(String text) { JLabel l = new JLabel(text); 
        l.setForeground(TEXT_WHITE); return l; 
    }
    
    private void addLabelAndField(JPanel p, String text, JTextField f) { p.add(createWhiteLabel(text)); 
        p.add(f); 
    }

    private void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Alert", JOptionPane.ERROR_MESSAGE); }

    public static void main(String[] args) { new SubscriptionGUI(); }
}