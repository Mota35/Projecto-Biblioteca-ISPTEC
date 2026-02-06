# 📚 Sistema de Biblioteca ISPTEC

Sistema completo de gestão de biblioteca desenvolvido em **Java com JavaFX**, com interface gráfica moderna e funcionalidades específicas para administradores e estudantes.

## 🎯 Funcionalidades

### 👨‍💼 Área do Administrador (Controle Total)

**Dashboard:**
- Total de livros no acervo
- Livros emprestados atualmente
- Reservas pendentes
- Membros ativos
- Multas em atraso com valores

**Gestão de Livros:**
- ✅ Cadastrar livro
- ✅ Editar livro
- ✅ Remover livro
- ✅ Definir quantidade de exemplares
- 📷 Registrar livro por foto (OCR) - _em desenvolvimento_

**Gestão de Membros:**
- ✅ Cadastrar membro
- ✅ Bloquear/desbloquear membro
- ✅ Ver histórico de empréstimos
- ✅ Ver multas

**Empréstimos e Devoluções:**
- ✅ Efetuar empréstimo
- ✅ Registrar devolução
- ✅ Aplicar multa
- ⏰ Controle automático de atrasos

**Reservas:**
- ✅ Ver lista de reservas
- ✅ Atender reserva
- ✅ Cancelar reserva

**Relatórios:**
- 📊 Livros mais emprestados - _em desenvolvimento_
- 📊 Membros com atraso - _em desenvolvimento_

**Chatbot:**
- 💬 Assistente virtual - _em desenvolvimento_

---

### 🎓 Área do Utilizador/Estudante (Acesso Limitado)

**Dashboard Pessoal:**
- Meus livros emprestados
- Data de devolução prevista
- Minhas multas (se houver)
- Minhas reservas ativas

**Consulta de Livros:**
- ✅ Ver lista completa de livros
- ✅ Pesquisar por título, autor ou categoria
- ✅ Ver disponibilidade em tempo real
- ✅ Reservar livros indisponíveis
- ❌ **NÃO** pode cadastrar, editar ou remover

**Minhas Reservas:**
- ✅ Ver posição na fila
- ✅ Cancelar reserva

**Renovação:**
- ✅ Renovar empréstimo (máximo 2 vezes)
- ❌ Bloqueio automático se houver reservas pendentes

**Perfil:**
- ✅ Ver dados pessoais
- ✅ Alterar senha

**Chatbot:**
- 💬 Tirar dúvidas sobre livros e procedimentos

---

## 🔐 Credenciais de Teste

### Administrador
```
Email: admin@biblioteca.com
Senha: admin123
```

### Estudante
```
Email: user@biblioteca.com
Senha: user123
```

---

## 🚀 Como Executar

### Pré-requisitos

1. **Java JDK 11 ou superior**
   - Download: https://www.oracle.com/java/technologies/downloads/
   - Ou OpenJDK: https://adoptium.net/

2. **Maven**
   - Download: https://maven.apache.org/download.cgi
   - Ou use a IDE que já tem Maven integrado (IntelliJ IDEA, Eclipse, NetBeans)

### Opção 1: Usando Maven (Linha de Comando)

```bash
# 1. Navegue até a pasta do projeto
cd caminho/para/biblioteca-isptec

# 2. Compile o projeto
mvn clean compile

# 3. Execute a aplicação
mvn javafx:run
```

### Opção 2: Usando IntelliJ IDEA

1. Abra o IntelliJ IDEA
2. Clique em **File → Open** e selecione a pasta do projeto
3. Aguarde o Maven baixar as dependências
4. Localize a classe `Main.java` em `src/main/java/ao/isptec/biblioteca/Main.java`
5. Clique com o botão direito e selecione **Run 'Main'**

### Opção 3: Usando Eclipse

1. Abra o Eclipse
2. Vá em **File → Import → Maven → Existing Maven Projects**
3. Selecione a pasta do projeto e clique em **Finish**
4. Aguarde o Maven baixar as dependências
5. Localize a classe `Main.java`
6. Clique com o botão direito e selecione **Run As → Java Application**

### Opção 4: Usando NetBeans

1. Abra o NetBeans
2. Vá em **File → Open Project**
3. Selecione a pasta do projeto
4. Aguarde o Maven baixar as dependências
5. Clique com o botão direito no projeto e selecione **Run**

---

## 📁 Estrutura do Projeto

```
biblioteca-isptec/
├── src/
│   └── main/
│       └── java/
│           └── isptec/
│                   └── biblioteca/
│                       ├── Main.java                    # Classe principal
│                       ├── enumeracao/                  # Enumeração
│                       │   ├── StatusMembro.java
│                       │   ├── StatusReserva.java
│                       │   └── TipoUsario.java
│                       ├── model/                       # Modelos de dados
│                       │   ├── Usuario.java
│                       │   ├── Livro.java
│                       │   ├── Membro.java
│                       │   ├── Emprestimo.java
│                       │   └── Reserva.java
│                       ├── service/                     # Lógica de negócio
│                       │   ├── AuthService.java
│                       │   └── LibraryService.java
│                       └── view/                        # Interfaces gráficas
│                           ├── LoginView.java
│                           ├── DashboardAdminView.java
│                           ├── DashboardUserView.java
│                           ├── LivrosView.java
│                           ├── MembrosView.java
│                           ├── EmprestimosView.java
│                           ├── ReservasView.java
│                           ├── RelatoriosView.java
│                           ├── CatalogoUserView.java
│                           ├── MinhasReservasView.java
│                           ├── PerfilUserView.java
│                           └── ChatbotView.java
├── pom.xml                                             # Configuração Maven
└── README.md                                           # Este arquivo
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 11+**
- **JavaFX 17** - Framework para interface gráfica
- **Maven** - Gerenciamento de dependências e build

---

## 📝 Regras de Negócio

### Empréstimos
- Prazo padrão: **14 dias**
- Renovações permitidas: **até 2 vezes**
- Multa por atraso: **R$ 2,00 por dia**
- Bloqueio de renovação se houver reservas pendentes

### Reservas
- Estudantes podem reservar livros indisponíveis
- Sistema de fila (FIFO - First In, First Out)
- Prazo para retirada após disponibilidade: **48 horas**
- Cancelamento permitido a qualquer momento

### Membros
- Status: **Ativo** ou **Bloqueado**
- Membros bloqueados não podem fazer novos empréstimos
- Administradores podem bloquear/desbloquear membros

---

## 🔄 Próximas Funcionalidades (Roadmap)

- [ ] Registro de livros por foto com OCR
- [ ] Chatbot com IA para recomendações
- [ ] Relatórios avançados com gráficos
- [ ] Notificações por email
- [ ] Integração com banco de dados (MySQL/PostgreSQL)
- [ ] Exportação de relatórios em PDF
- [ ] Sistema de multas com pagamento online
- [ ] App mobile (Android/iOS)

---

## 🐛 Resolução de Problemas

### Erro: "JavaFX runtime components are missing"

Se você estiver usando Java 11+, o JavaFX não vem mais incluído. Certifique-se de:
1. Estar usando Maven para executar (`mvn javafx:run`)
2. Ou configurar o JavaFX manualmente na sua IDE

### Erro: "Cannot find Maven"

Instale o Maven ou use a IDE que já tem Maven integrado (IntelliJ IDEA, Eclipse, NetBeans).

### A interface não aparece / Janela em branco

Certifique-se de que:
1. Está usando Java 11 ou superior
2. O JavaFX está corretamente configurado
3. Tente executar com `mvn javafx:run`

---

## 📧 Suporte

Para dúvidas ou problemas, entre em contato:
- Email: suporte@isptec.ao
- GitHub Issues: [Criar issue](https://github.com/isptec/biblioteca/issues)

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais no ISPTEC.

---

## 👥 Autores

**ISPTEC - Instituto Superior Politécnico de Tecnologias e Ciências**

---

**Desenvolvido com ❤️ em Angola 🇦🇴**
