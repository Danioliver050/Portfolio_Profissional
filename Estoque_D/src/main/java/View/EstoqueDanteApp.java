package View;

import dao.ProdutoDAO;
import model.Produto;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class EstoqueDanteApp extends JFrame {

    private ProdutoDAO produtoDAO;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JTextField campoFiltro;
    private JButton btnRemoverProduto;
    private Usuario usuarioLogado;

    // Construtor padrão (para testes)
    public EstoqueDanteApp() {
        this.usuarioLogado = null;
        inicializarGUI();
    }

    // Construtor sobrecarregado com usuário logado
    public EstoqueDanteApp(Usuario usuario) {
        this.usuarioLogado = usuario;
        inicializarGUI();
    }

    private void inicializarGUI() {
        produtoDAO = new ProdutoDAO();
        setTitle("Controle de Estoque - Dante Cosméticos");
        setSize(950, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenuBar();
        initComponents();
        carregarTodosProdutos();

        // Restrições de cargo nos botões
        if (usuarioLogado != null && !usuarioLogado.getCargo().equalsIgnoreCase("admin")) {
            btnRemoverProduto.setEnabled(false);
        }
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem itemExportar = new JMenuItem("Exportar para CSV");
        JMenuItem itemSair = new JMenuItem("Sair");
        itemExportar.addActionListener(e -> exportarParaCSV());
        itemSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(itemExportar);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);

        JMenu menuProdutos = new JMenu("Produtos");
        JMenuItem itemAdicionar = new JMenuItem("Adicionar");
        JMenuItem itemAtualizar = new JMenuItem("Atualizar");
        JMenuItem itemRemover = new JMenuItem("Remover");
        JMenuItem itemBuscar = new JMenuItem("Buscar");
        JMenuItem itemAlertas = new JMenuItem("Validades Próximas");

        itemAdicionar.addActionListener(e -> adicionarProduto());
        itemAtualizar.addActionListener(e -> atualizarProduto());
        itemRemover.addActionListener(e -> removerProduto());
        itemBuscar.addActionListener(e -> buscarProduto());
        itemAlertas.addActionListener(e -> carregarAlertasValidade());

        menuProdutos.add(itemAdicionar);
        menuProdutos.add(itemAtualizar);
        menuProdutos.add(itemRemover);
        menuProdutos.add(itemBuscar);
        menuProdutos.addSeparator();
        menuProdutos.add(itemAlertas);

        // Restrições de menu por cargo
        if (usuarioLogado != null && !usuarioLogado.getCargo().equalsIgnoreCase("admin")) {
            itemRemover.setEnabled(false);
            // Se quiser, pode desabilitar adicionar e atualizar também
            // itemAdicionar.setEnabled(false);
            // itemAtualizar.setEnabled(false);
        }

        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre o Sistema");
        itemSobre.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Controle de Estoque Dante Cosméticos + CRUD \nVersão 2.0\nDesenvolvido em Java + MySQL"));
        menuAjuda.add(itemSobre);

        menuBar.add(menuArquivo);
        menuBar.add(menuProdutos);
        menuBar.add(menuAjuda);

        setJMenuBar(menuBar);
    }

    private void initComponents() {
        // Campo de filtro
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.add(new JLabel("Filtrar (nome/marca/lote): "));
        campoFiltro = new JTextField(20);
        JButton btnFiltrar = new JButton("Aplicar Filtro");
        JButton btnLimparFiltro = new JButton("Limpar Filtro");
        painelTopo.add(campoFiltro);
        painelTopo.add(btnFiltrar);
        painelTopo.add(btnLimparFiltro);

        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnLimparFiltro.addActionListener(e -> carregarTodosProdutos());

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Código", "Nome", "Marca", "Lote", "Validade", "Preço", "Quantidade"}, 0
        );
        tabela = new JTable(tableModel);

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String dataStr = table.getValueAt(row, 5).toString();
                try {
                    Date dataValidade = sdf.parse(dataStr);
                    LocalDate validade = dataValidade.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate hoje = LocalDate.now();

                    if (validade.isBefore(hoje)) {
                        c.setBackground(new Color(255, 120, 120)); // vermelho
                    } else if (!validade.isAfter(hoje.plusDays(10))) {
                        c.setBackground(new Color(255, 255, 150)); // amarelo
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } catch (Exception e) {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);

        // Painel inferior com botão Voltar e Remover
        JPanel painelBotoes = new JPanel();
        JButton btnVoltar = new JButton("Voltar à Tela Principal");
        btnVoltar.addActionListener(e -> carregarTodosProdutos());

        btnRemoverProduto = new JButton("Remover Produto");
        btnRemoverProduto.addActionListener(e -> removerProduto());

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnRemoverProduto);

        // Layout principal
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(painelTopo, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.SOUTH);
    }

    // ------------------ MÉTODOS CRUD ------------------

    private void carregarTodosProdutos() {
        tableModel.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getCodigoBarras(), p.getNome(), p.getMarca(),
                    p.getLote(), sdf.format(p.getValidade()), p.getPreco(), p.getQuantidade()
            });
        }
    }

    private void carregarAlertasValidade() {
        tableModel.setRowCount(0);
        List<Produto> produtos = produtoDAO.produtosProxVencimento();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getCodigoBarras(), p.getNome(), p.getMarca(),
                    p.getLote(), sdf.format(p.getValidade()), p.getPreco(), p.getQuantidade()
            });
        }
    }

    private void aplicarFiltro() {
        String filtro = campoFiltro.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            carregarTodosProdutos();
            return;
        }

        tableModel.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Produto p : produtos) {
            if (p.getNome().toLowerCase().contains(filtro) ||
                    p.getMarca().toLowerCase().contains(filtro) ||
                    p.getLote().toLowerCase().contains(filtro)) {
                tableModel.addRow(new Object[]{
                        p.getId(), p.getCodigoBarras(), p.getNome(), p.getMarca(),
                        p.getLote(), sdf.format(p.getValidade()), p.getPreco(), p.getQuantidade()
                });
            }
        }
    }

    private void adicionarProduto() {
        try {
            String codigo = JOptionPane.showInputDialog("Código de barras:");
            String nome = JOptionPane.showInputDialog("Nome:");
            String marca = JOptionPane.showInputDialog("Marca:");
            String lote = JOptionPane.showInputDialog("Lote:");
            String validadeStr = JOptionPane.showInputDialog("Validade (yyyy-MM-dd):");
            double preco = Double.parseDouble(JOptionPane.showInputDialog("Preço:"));
            int quantidade = Integer.parseInt(JOptionPane.showInputDialog("Quantidade:"));

            Produto p = new Produto(0, codigo, nome, marca, lote,
                    new SimpleDateFormat("yyyy-MM-dd").parse(validadeStr),
                    preco, quantidade, null, null);

            if (produtoDAO.inserir(p)) {
                JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
                carregarTodosProdutos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar produto.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void atualizarProduto() {
        try {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
                return;
            }
            String codigo = tabela.getValueAt(linha, 1).toString();
            Produto p = produtoDAO.buscarPorCodigo(codigo);
            if (p != null) {
                String novoNome = JOptionPane.showInputDialog("Nome:", p.getNome());
                String novaMarca = JOptionPane.showInputDialog("Marca:", p.getMarca());
                String novoLote = JOptionPane.showInputDialog("Lote:", p.getLote());
                String validadeStr = JOptionPane.showInputDialog("Validade (yyyy-MM-dd):",
                        new SimpleDateFormat("yyyy-MM-dd").format(p.getValidade()));
                double preco = Double.parseDouble(JOptionPane.showInputDialog("Preço:", p.getPreco()));
                int quantidade = Integer.parseInt(JOptionPane.showInputDialog("Quantidade:", p.getQuantidade()));

                p.setNome(novoNome);
                p.setMarca(novaMarca);
                p.setLote(novoLote);
                p.setValidade(new SimpleDateFormat("yyyy-MM-dd").parse(validadeStr));
                p.setPreco(preco);
                p.setQuantidade(quantidade);

                if (produtoDAO.atualizar(p)) {
                    JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                    carregarTodosProdutos();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar produto.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void removerProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
            return;
        }
        String codigo = tabela.getValueAt(linha, 1).toString();
        if (produtoDAO.remover(codigo)) {
            JOptionPane.showMessageDialog(this, "Produto removido!");
            carregarTodosProdutos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao remover produto.");
        }
    }

    private void buscarProduto() {
        String codigo = JOptionPane.showInputDialog("Digite o código do produto:");
        Produto p = produtoDAO.buscarPorCodigo(codigo);
        if (p != null) {
            tableModel.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tableModel.addRow(new Object[]{
                    p.getId(), p.getCodigoBarras(), p.getNome(), p.getMarca(),
                    p.getLote(), sdf.format(p.getValidade()), p.getPreco(), p.getQuantidade()
            });
        } else {
            JOptionPane.showMessageDialog(this, "Produto não encontrado.");
        }
    }

    private void exportarParaCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("produtos_exportados.csv"))) {
            for (int i = 0; i < tabela.getRowCount(); i++) {
                for (int j = 0; j < tabela.getColumnCount(); j++) {
                    pw.print(tabela.getValueAt(i, j));
                    if (j < tabela.getColumnCount() - 1) pw.print(";");
                }
                pw.println();
            }
            JOptionPane.showMessageDialog(this, "Exportação concluída com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao exportar: " + e.getMessage());
        }
    }
}
