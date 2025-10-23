# Estoque Dante — Sistema de Controle de Estoque para Cosméticos

## Descrição do Projeto

O Estoque Dante é um sistema desktop desenvolvido em Java com interface gráfica (Swing) e integração com banco de dados MySQL.  
O objetivo é facilitar o gerenciamento de produtos cosméticos de um ecommerce, controlando validade, movimentação e registro de estoque.

---

## Principais Funcionalidades

| Função | Descrição |
|--------|-----------|
| Cadastrar Produto | Adiciona um novo produto ao estoque |
| Buscar Produto | Pesquisa produtos por código de barras, nome ou marca |
| Atualizar Produto | Edita informações de produtos cadastrados |
| Remover Produto | Exclui produtos do banco de dados |
| Alertas de Validade | Lista produtos próximos do vencimento |
| Exportar CSV | Exporta dados do estoque em planilha |
| Login com Permissões | Controle de acesso por cargo (admin, logística, marketing, sócio) |

---

## Tecnologias Utilizadas

| Categoria | Tecnologias |
|-----------|------------|
| Linguagem | Java 17+ |
| Banco de Dados | MySQL 8+ |
| Interface Gráfica | Java Swing |
| Conexão | JDBC |
| Persistência | DAO Pattern |
| Configuração Segura | Arquivo `config.properties` |
| IDE | IntelliJ IDEA |
| Relatórios | Exportação em CSV |

---

## Arquitetura (MVC)

O sistema segue o padrão Model–View–Controller (MVC):

EstoqueDante_Publico/
├── dao/ -> Classes de acesso ao banco (ProdutoDAO, UsuarioDAO)
├── model/ -> Classes de modelo (Produto, Usuario)
├── util/ -> Classes utilitárias (Conexao,)
├── view/ -> Telas e interface gráfica (Swing)
├── resources/ -> Arquivos externos (config.properties, estoque_DantePublico.sql)


---

## Configuração do Banco de Dados

### 1. Criar o Banco de Dados

Abra o MySQL Workbench ou terminal MySQL e execute:

```sql
CREATE DATABASE estoque_DantePublico;
USE estoque_DantePublico;
```
2. Importar o Script SQL

O script SQL do projeto está localizado em:
```sql
src/main/resources/estoque_DantePublico.sql
```

No terminal MySQL, execute:
```sql
SOURCE caminho/completo/para/estoque_DantePublico.sql;
```

Exemplo:
```sql
SOURCE caminho/completo/para/estoque_DantePublico.sql;
```

Verifique as tabelas criadas:
```sql
SHOW TABLES;
```
Configuração do Arquivo config.properties

Localização:

```sql
src/main/resources/config.properties
```

Exemplo de conteúdo seguro para repositórios públicos

```sql
db.url=jdbc:mysql://localhost:3306/estoque_DantePublico
db.user=seu_usuario_mysql
db.password=sua_senha_mysql
```
Atenção:

- Não utilize senhas reais em repositórios públicos.
- Mantenha valores genéricos ou de teste.
- Adicione config.properties ao .gitignore.

# Como Executar o Projeto
1. Via IntelliJ IDEA

- Abra o projeto no IntelliJ IDEA.

- Acesse a classe EstoqueDanteApp (pacote view).

- Clique com o botão direito e selecione Run 'EstoqueDanteApp'.

2. Via arquivo JAR

1.Gere o arquivo executável:
```
Build > Build Artifacts > Build
```
2. Localize o arquivo .jar:
```
out/artifacts/EstoqueDanteApp_jar/
```
3. Clique duas vezes sobre o .jar para executar o programa.

Certifique-se de que:

- O MySQL esteja ativo.
- O banco estoque_DantePublico esteja criado.
- O arquivo config.properties esteja no mesmo diretório do .jar.
- 
# Como Rodar o Projeto em Outra Máquina

1. Instale o Java Runtime Environment (JRE 17+).

2. Instale o MySQL Server e Workbench.

3. Copie o .jar e o arquivo .sql do projeto.

4. No MySQL, execute:
````
SOURCE estoque_DantePublico.sql;
```` 

5. Crie o arquivo config.properties no mesmo diretório do .jar:
````
db.url=jdbc:mysql://localhost:3306/estoque_DantePublico
db.user=root
db.password=sua_senha
````

6. Dê duplo clique no arquivo .jar para iniciar o sistema.

