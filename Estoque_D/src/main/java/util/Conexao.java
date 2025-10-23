package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {

    private static String URL;
    private static String USER;
    private static String PASSWORD;


    static {
        try (InputStream input = Conexao.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Arquivo config.properties não encontrado!");
            }
            Properties prop = new Properties();
            prop.load(input);
            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao carregar configuração do banco de dados: " + e.getMessage());
        }
    }

    public static Connection conectar() {
        try {
            Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão bem sucedida ao BDD");
            return conexao;
        } catch (SQLException e) {
            System.out.println("Erro ao realizar conexão ao BDD: " + e.getMessage());
            return null;
        }
    }

    public static void desconectar(Connection conexao) {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão ao BDD encerrada");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao encerrar conexão ao BDD: " + e.getMessage());
        }
    }
}
