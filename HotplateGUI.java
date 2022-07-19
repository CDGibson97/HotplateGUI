package Question_1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class HotplateGUI extends JPanel implements ActionListener, MouseListener, ChangeListener {

    private final Timer timer;
    private final DrawPanel drawPanel;
    private final JSlider tempSlider = new JSlider(0, 1000, 500);
    JSlider constSlider = new JSlider(0, 100);
    private Element[][] elementArray = new Element[10][10];
    private int temperature;
    private double heat;

    public HotplateGUI() {
        super(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        drawPanel = new DrawPanel();
        JTextField tempTitle = new JTextField("Temperature slider 0 - 1000");
        tempSlider.setMajorTickSpacing(5);
        tempSlider.setPaintTicks(true);
        JTextField constTitle = new JTextField("HeatConstant slider 0.01 - 1.0");
        constSlider.setMajorTickSpacing(5);
        constSlider.setPaintTicks(true);


        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {

                elementArray[x][y] = new Element(0);

            }
        }
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (x + 1 < 10) {
                    elementArray[x][y].addNeighbour(elementArray[x + 1][y]);
                }
                if (y + 1 < 10) {
                    elementArray[x][y].addNeighbour((elementArray[x][y + 1]));
                }
                if (x - 1 >= 0) {
                    elementArray[x][y].addNeighbour(elementArray[x - 1][y]);
                }
                if (y - 1 >= 0) {
                    elementArray[x][y].addNeighbour((elementArray[x][y - 1]));
                }
                Thread thread = new Thread(elementArray[x][y]);
                thread.start();
            }
        }
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(tempTitle);
        panel.add(tempSlider);
        panel.add(constTitle);
        panel.add(constSlider);
        drawPanel.addMouseListener(this);


        super.add(drawPanel, BorderLayout.CENTER);
        super.add(panel, BorderLayout.SOUTH);

        tempSlider.addChangeListener(this);
        constSlider.addChangeListener(this);

        timer = new Timer(30, this);
        timer.start();
    }

    public class DrawPanel extends JPanel {

        public DrawPanel() {
            setPreferredSize(new Dimension(400, 400));
            setBackground(Color.BLACK);
        }

        @Override
        public synchronized void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    elementArray[x][y].draw(g);
                    Element.x += 40;
                }
                Element.x = 0;
                Element.y += 40;
            }
            Element.y = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == timer) {
            drawPanel.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        heatUpElement(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider change = (JSlider) e.getSource();

        if (change == tempSlider) {
            temperature = tempSlider.getValue();
        }
        if (change == constSlider) {
            heat = (double) constSlider.getValue() / 100;
        }
    }

    public void heatUpElement(int x, int y) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ((x >= (i * 40) && x <= ((i * 40) + 40))) {
                    if ((y >= (j * 40)) && (y <= ((j * 40) + 40))) {
                        elementArray[j][i].applyTempToElement(temperature, heat);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hotplate layout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new HotplateGUI());
        frame.pack();
        frame.setLocationRelativeTo(null); //pack frame
        frame.setVisible(true);                                      //show the frame
    }
}

