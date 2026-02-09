# Configuração do Banco de Dados - Sistema de Biblioteca ISPTEC

## 📋 Pré-requisitos

1. **MySQL Server 8.0+** instalado e rodando
2. **Java 23** ou superior
3. **Maven** para gerenciar dependências

## 🗄️ Passos para Configuração

### 1. Instalar MySQL

Se ainda não tiver o MySQL instalado:

**Windows:**
- Baixe o MySQL Installer: https://dev.mysql.com/downloads/installer/
- Execute o instalador e escolha "Developer Default"
- Configure uma senha para o usuário `root`

**Linux/Mac:**
```bash
# Ubuntu/Debian
sudo apt-get install mysql-server

# MacOS
brew install mysql
```

### 2. Criar o Banco de Dados

1. Abra o MySQL Workbench ou terminal MySQL:
```bash
mysql -u root -p
```

2. Execute o script de criação:
```bash
mysql -u root -p < database/create_database.sql
```

**OU** copie e cole o conteúdo do arquivo `database/create_database.sql` diretamente no MySQL Workbench.

### 3. Configurar Credenciais

Edite o arquivo `src/main/resources/database.properties`:

```properties
# Configurações de Conexão MySQL
db.url=jdbc:mysql://localhost:3306/biblioteca_isptec?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=SUA_SENHA_AQUI

# Pool de Conexões HikariCP
db.pool.maximumPoolSize=10
db.pool.minimumIdle=3
db.pool.connectionTimeout=30000
db.pool.idleTimeout=600000
db.pool.maxLifetime=1800000
```

**⚠️ IMPORTANTE:** Substitua `SUA_SENHA_AQUI` pela senha do seu MySQL.

### 4. Instalar Dependências

Execute no terminal do projeto:

```bash
mvn clean install
```

Isso irá baixar:
- MySQL Connector/J 8.3.0
- HikariCP 5.1.0 (Pool de Conexões)
- SLF4J (Logging)

### 5. Testar Conexão

Execute a aplicação. Se tudo estiver correto, verá a mensagem:
```
Pool de conexões inicializado com sucesso!
```

## 🏗️ Estrutura do Banco de Dados

### Tabelas Principais

1. **pessoa** - Tabela base para herança (Membro, Estudante, Bibliotecário)
2. **membro** - Dados de membros da biblioteca
3. **estudante** - Dados específicos de estudantes
4. **bibliotecario** - Dados de funcionários/administradores
5. **livro** - Acervo de livros
6. **autor** - Autores de livros
7. **categoria** - Categorias de livros
8. **emprestimo** - Registro de empréstimos
9. **reserva** - Registro de reservas

### Tabelas de Relacionamento

- **livro_autor** - N:N entre Livro e Autor
- **livro_categoria** - N:N entre Livro e Categoria

### Tabelas Auxiliares

- **historico_acao** - Log de ações (auditoria)
- **notificacao** - Notificações para usuários

## 📊 Views Disponíveis

- `view_emprestimos_ativos` - Empréstimos ativos com informações completas
- `view_reservas_ativas` - Reservas ativas ordenadas por data
- `view_livros_disponiveis` - Livros disponíveis com autores e categorias
- `view_membros_info` - Informações completas de membros

## 🔄 Stored Procedures

- `sp_realizar_emprestimo` - Realiza empréstimo com validações
- `sp_devolver_livro` - Registra devolução e calcula multa
- `sp_renovar_emprestimo` - Renova empréstimo (estende prazo)
- `sp_criar_reserva` - Cria reserva com validações

## ⚙️ Eventos Agendados

- `evt_verificar_emprestimos_atrasados` - Executa diariamente
- `evt_expirar_reservas` - Executa diariamente

## 👤 Usuário Padrão

Após executar o script, um administrador padrão é criado:

```
Email: admin@biblioteca.isptec.ao
Senha: admin123
```

**⚠️ IMPORTANTE:** Altere esta senha na primeira execução!

## 🔧 Solução de Problemas

### Erro: "Access denied for user 'root'@'localhost'"
- Verifique se a senha em `database.properties` está correta
- Verifique se o MySQL está rodando: `sudo service mysql status`

### Erro: "Unknown database 'biblioteca_isptec'"
- Execute o script `create_database.sql` primeiro

### Erro: "Communications link failure"
- Verifique se o MySQL está rodando na porta 3306
- Verifique o firewall

### Erro: "Public Key Retrieval is not allowed"
- Adicione `allowPublicKeyRetrieval=true` na URL de conexão (já está configurado)

## 📝 Comandos Úteis MySQL

```sql
-- Ver todos os bancos de dados
SHOW DATABASES;

-- Usar o banco de dados
USE biblioteca_isptec;

-- Ver todas as tabelas
SHOW TABLES;

-- Ver estrutura de uma tabela
DESCRIBE pessoa;

-- Ver dados de uma tabela
SELECT * FROM pessoa;

-- Ver quantidade de registros
SELECT COUNT(*) FROM livro;

-- Limpar todos os dados (CUIDADO!)
TRUNCATE TABLE emprestimo;
TRUNCATE TABLE reserva;
```

## 🔐 Segurança

**Para Produção:**
1. Crie um usuário específico para a aplicação:
```sql
CREATE USER 'biblioteca_app'@'localhost' IDENTIFIED BY 'senha_forte';
GRANT SELECT, INSERT, UPDATE, DELETE ON biblioteca_isptec.* TO 'biblioteca_app'@'localhost';
FLUSH PRIVILEGES;
```

2. Use senhas criptografadas (bcrypt) ao invés de texto plano

3. Configure SSL para conexões

## 📚 Documentação Adicional

- [MySQL Documentation](https://dev.mysql.com/doc/)
- [HikariCP](https://github.com/brettwooldridge/HikariCP)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)

## 🆘 Suporte

Para dúvidas ou problemas, contate o administrador do sistema.

