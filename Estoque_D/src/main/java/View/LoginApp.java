package View;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginApp extends JFrame {

    private JTextField tfUsuario;
    private JPasswordField pfSenha;
    private JButton btnLogin;
    private UsuarioDAO usuarioDAO;

    public LoginApp() {
        usuarioDAO = new UsuarioDAO();
        setTitle("Login - Dante Cosméticos");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label e campo usuário
        JLabel lblUsuario = new JLabel("Usuário:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(lblUsuario, gbc);

        tfUsuario = new JTextField(20);
        gbc.gridx = 1;
        painel.add(tfUsuario, gbc);


        JLabel lblSenha = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(lblSenha, gbc);

        pfSenha = new JPasswordField(20);
        gbc.gridx = 1;
        painel.add(pfSenha, gbc);


        btnLogin = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        painel.add(btnLogin, gbc);

        getContentPane().add(painel, BorderLayout.CENTER);


        btnLogin.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String usuario = tfUsuario.getText();
        String senha = new String(pfSenha.getPassword());

        Usuario user = usuarioDAO.autenticar(usuario, senha);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login realizado com sucesso! Bem-vindo " + user.getNome());

            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
            pfSenha.setText("");
            tfUsuario.requestFocus();
        }
    }
}
