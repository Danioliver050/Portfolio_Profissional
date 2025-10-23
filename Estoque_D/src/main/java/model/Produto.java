package model;

import java.util.Date;

public class Produto {

    private int id;
    private String codigoBarras;
    private String nome;
    private String marca;
    private String lote;
    private Date validade;
    private double preco;
    private int quantidade;
    private Date dataCadastro;
    private Date dataUltimaMovimentacao;

    // Construtor completo
    public Produto(int id, String codigoBarras, String nome, String marca, String lote, Date validade,
                   double preco, int quantidade, Date dataCadastro, Date dataUltimaMovimentacao) {
        this.id = id;
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.marca = marca;
        this.lote = lote;
        this.validade = validade;
        this.preco = preco;
        this.quantidade = quantidade;
        this.dataCadastro = dataCadastro;
        this.dataUltimaMovimentacao = dataUltimaMovimentacao;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public Date getValidade() { return validade; }
    public void setValidade(Date validade) { this.validade = validade; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Date getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(Date dataCadastro) { this.dataCadastro = dataCadastro; }

    public Date getDataUltimaMovimentacao() { return dataUltimaMovimentacao; }
    public void setDataUltimaMovimentacao(Date dataUltimaMovimentacao) { this.dataUltimaMovimentacao = dataUltimaMovimentacao; }


    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", nome='" + nome + '\'' +
                ", marca='" + marca + '\'' +
                ", lote='" + lote + '\'' +
                ", validade=" + validade +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                ", dataCadastro=" + dataCadastro +
                ", dataUltimaMovimentacao=" + dataUltimaMovimentacao +
                '}';
    }
}
