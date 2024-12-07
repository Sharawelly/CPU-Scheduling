import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class ProcessPanel extends JPanel {
    int x = 50;
    int y = 120;
    int height = 50;
    private ArrayList<Process> processes;
    private Timer timer;
    private int currentIndex = -1;  // Start from no process
    public ProcessPanel(ArrayList<Process> processes) {
        this.processes = processes;

        timer = new Timer(200, e -> {
            if (currentIndex < processes.size() - 1) {
                currentIndex++;  // Move to the next process
                repaint();  // Trigger a repaint to draw the next process
            } else {
                timer.stop();  // Stop the timer when all processes have been drawn
            }
        });

        timer.setInitialDelay(0); // Start the timer immediately
        timer.start();  // Start the timer

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        Font customFont = new Font("Comic Sans MS", Font.PLAIN, 13);
        g.setFont(customFont); // Apply the font

        int xPosition = x;

        for (int i = 0; i <= currentIndex; i++) {
            Process process = processes.get(i);
            Color blockColor = getColorFromName(process.color);
            int width = 25;  // Scale burst time to width

            g.setColor(blockColor);  // Set the color to the block color
            g.fillRect(xPosition, y, width, height);  // Draw the rectangle
//            g.setColor(Color.BLACK);  // Set color for the string
            g.drawString(process.name, xPosition + 3, y + height + 20);

            xPosition += width + 15;  // Move to the next position after this burst
        }
    }

    public static Color getColorFromName(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red":
                return new Color(255, 0, 0);
            case "green":
                return new Color(0, 255, 0);
            case "blue":
                return new Color(0, 0, 255);
            case "black":
                return new Color(0, 0, 0);
            case "pink":
                return  new Color(255, 175, 175);
            case "yellow":
                return new Color(255, 255, 0);
            case "orange":
                return new Color(255, 200, 0);
            case "purple":
                return new Color(128, 0, 128);
            case "brown":
                return new Color(139, 69, 19);
            case "cyan":
                return new Color(0, 255, 255);
            case "magenta":
                return new Color(255, 0, 255);
            case "gray":
                return new Color(128, 128, 128);
            case "silver":
                return new Color(192, 192, 192);
            case "gold":
                return new Color(255, 215, 0);
            case "olive":
                return new Color(128, 128, 0);
            case "maroon":
                return new Color(128, 0, 0);
            case "navy":
                return new Color(0, 0, 128);
            case "teal":
                return new Color(0, 128, 128);
            case "indigo":
                return new Color(75, 0, 130);
            case "violet":
                return new Color(238, 130, 238);
            case "beige":
                return new Color(245, 245, 220);
            case "salmon":
                return new Color(250, 128, 114);
            case "turquoise":
                return new Color(64, 224, 208);
            case "khaki":
                return new Color(240, 230, 140);
            case "lavender":
                return new Color(230, 230, 250);
            case "orchid":
                return new Color(218, 112, 214);
            case "coral":
                return new Color(255, 127, 80);
            case "crimson":
                return new Color(220, 20, 60);
            case "chocolate":
                return new Color(210, 105, 30);
            case "goldenrod":
                return new Color(218, 165, 32);
            case "plum":
                return new Color(221, 160, 221);
            case "peachpuff":
                return new Color(255, 218, 185);
            case "tomato":
                return new Color(255, 99, 71);
            case "azure":
                return new Color(240, 255, 255);
            default:
//                System.out.println("Unknown color: " + colorName + ", defaulting to WHITE.");
                return new Color(255, 255, 255); // Default to WHITE if color is unknown
        }
    }
}