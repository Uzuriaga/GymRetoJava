package app;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.File;

/**
 * Ventana de login con panel izquierdo (formulario) y panel derecho (imagen).
 * Coloca la imagen en: src/main/resources/img/logo.png   (si usas Maven)
 * o en la carpeta del proyecto: img/logo.png (si prefieres ruta en disco).
 */
public class Login extends JFrame {

    public Login() {
        setTitle("Gym - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(420);
        split.setEnabled(false);

        // LEFT: panel de formulario
        JPanel left = new JPanel();
        left.setBackground(Color.WHITE);
        left.setLayout(new BorderLayout());
        left.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Contenedor vertical
        Box vbox = Box.createVerticalBox();
        vbox.setOpaque(false);

        JLabel heading = new JLabel("Bienvenido");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        vbox.add(heading);
        vbox.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("<html><span style='color:gray'>Ingresa tus credenciales para continuar</span></html>");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        vbox.add(subtitle);
        vbox.add(Box.createVerticalStrut(20));

        // Email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        vbox.add(emailLabel);
        vbox.add(Box.createVerticalStrut(6));

        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCED4DA)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        vbox.add(emailField);
        vbox.add(Box.createVerticalStrut(12));

        // Password
        JLabel passLabel = new JLabel("Contraseña");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        vbox.add(passLabel);
        vbox.add(Box.createVerticalStrut(6));

        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCED4DA)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        vbox.add(passField);
        vbox.add(Box.createVerticalStrut(8));

        // Remember + Forgot
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JCheckBox remember = new JCheckBox("Recordar datos");
        remember.setOpaque(false);
        remember.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        row.add(remember, BorderLayout.WEST);

        JLabel forgot = new JLabel("<html><a href=''>Olvidaste tu contraseña?</a></html>");
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgot.setForeground(new Color(0x6C63FF)); // accent color
        forgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(Login.this, "Función de recuperación no implementada.", "Forgot password", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        row.add(forgot, BorderLayout.EAST);

        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        vbox.add(row);
        vbox.add(Box.createVerticalStrut(18));

        // Botoón Login
        JButton btnLogin = new JButton("Entrar");
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0x6C63FF)); // púrpura
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.addActionListener((ActionEvent e) -> {
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena email y contraseña.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            btnLogin.setEnabled(false);
            emailField.setEnabled(false);
            passField.setEnabled(false);
            
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                private String userName = null;
                @Override
                protected Boolean doInBackground() {
                    try {
                        boolean ok = servicio.Login.authenticate(email, password);
                        if (ok) userName = servicio.Login.getUserNameByEmail(email);
                        return ok;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                }
                @Override
                protected void done() {
                    btnLogin.setEnabled(true);
                    emailField.setEnabled(true);
                    passField.setEnabled(true);
                    boolean ok = false;
                    try { ok = get(); } catch (Exception ex) { ex.printStackTrace(); }
                    if (ok) {
                        JOptionPane.showMessageDialog(Login.this, "Bienvenido " + (userName != null ? userName : ""), "OK", JOptionPane.INFORMATION_MESSAGE);
                        // abrir siguiente ventana o cerrar login
                    } else {
                        JOptionPane.showMessageDialog(Login.this, "Email o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
           /*Test 
            if ("ana@gmail.com".equalsIgnoreCase(email) && "12345".equals(contraseña)) {
                JOptionPane.showMessageDialog(this, "Autenticación correcta. Bienvenido Ana.", "OK", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Email o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }*/
        });
        vbox.add(btnLogin);
        vbox.add(Box.createVerticalStrut(18));

        // Sign up prompt
        JLabel signup = new JLabel("¿Primera vez? ¡Regístrate!");
        signup.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signup.setForeground(new Color(0x6C63FF));
        signup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(Login.this, "Función de registro no implementada.", "Sign up", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        signup.setAlignmentX(Component.LEFT_ALIGNMENT);
        vbox.add(signup);

        left.add(vbox, BorderLayout.NORTH);

        // RIGHT: panel con imagen del gym
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(new EmptyBorder(20, 20, 20, 20));
        right.setBackground(new Color(0xF3F5FF));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        right.add(imageLabel, BorderLayout.CENTER);

        // Cargar imagen de manera fiable usando ImageIO
        BufferedImage img = null;
        String resourcePath = "/img/logo.png"; // ruta en classpath: src/main/resources/img/logo.png
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is != null) {
                img = ImageIO.read(is);
            } else {
                // fallback a ruta en disco: img/logo.png
                File f = new File("img/logo.png");
                if (f.exists()) img = ImageIO.read(f);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final BufferedImage imageLoaded = img;
        if (imageLoaded != null) {
            // Actualizar imagen al cambiar tamaño o al mostrarse
            right.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    updateImageLabel(imageLoaded, imageLabel, right);
                }

                @Override
                public void componentShown(java.awt.event.ComponentEvent e) {
                    updateImageLabel(imageLoaded, imageLabel, right);
                }
            });
            // Forzar primer dibujado si el panel ya tiene tamaño
            SwingUtilities.invokeLater(() -> updateImageLabel(imageLoaded, imageLabel, right));
        } else {
            imageLabel.setText("<html><div style='text-align:center;color:#6C63FF'>Imagen no encontrada<br>coloca src/main/resources/img/logo.png</div></html>");
        }

        split.setLeftComponent(left);
        split.setRightComponent(right);

        add(split, BorderLayout.CENTER);
    }

    private void updateImageLabel(Image imageLoaded, JLabel imageLabel, JPanel right) {
        int w = right.getWidth() - 40;
        int h = right.getHeight() - 40;
        if (w <= 0 || h <= 0) return;
        int iw = imageLoaded.getWidth(null);
        int ih = imageLoaded.getHeight(null);
        if (iw <= 0 || ih <= 0) return;
        double aspect = (double) iw / ih;
        int nw = w;
        int nh = (int) (nw / aspect);
        if (nh > h) {
            nh = h;
            nw = (int) (nh * aspect);
        }
        Image scaled = imageLoaded.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
        imageLabel.setText(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            Login frame = new Login();
            frame.setVisible(true);
        });
    }
}
