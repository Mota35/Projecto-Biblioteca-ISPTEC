# 📚 Sistema de Gestão de Biblioteca Universitária - ISPTEC

Sistema completo de gestão de biblioteca universitária desenvolvido em **Java 23** com **JavaFX 21**, seguindo os princípios de Engenharia de Software.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Modelo de Domínio](#modelo-de-domínio)
- [Regras de Negócio](#regras-de-negócio)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Executar](#como-executar)
- [Credenciais de Teste](#credenciais-de-teste)
- [Funcionalidades](#funcionalidades)

---

## 🎯 Visão Geral

Este sistema permite a gestão completa de uma biblioteca universitária, incluindo:

- ✅ Gestão de livros, autores e categorias
- ✅ Cadastro e gestão de membros/estudantes
- ✅ Empréstimos com controle de prazo e multas
- ✅ Reservas com fila de espera
- ✅ Sistema de recomendação de livros (IA)
- ✅ OCR para cadastro de livros por imagem
- ✅ Chatbot assistente virtual

---

## 🏗️ Arquitetura

O sistema segue a **Arquitetura em Camadas** (Layered Architecture):

```
┌─────────────────────────────────────┐
│         APRESENTAÇÃO (Views)        │  JavaFX - Interface gráfica
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│     SERVIÇOS (Business Logic)       │  Interfaces + Implementações
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│          DOMÍNIO (Entities)         │  Classes de domínio
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│    PERSISTÊNCIA (Repositories)      │  Listas em memória / BD
└─────────────────────────────────────┘
```

---

## 📊 Modelo de Domínio

### Diagrama de Classes

```
                    ┌──────────────┐
                    │   Pessoa     │ (abstract)
                    │ - id         │
                    │ - nome       │
                    │ - email      │
                    │ - senha      │
                    └──────┬───────┘
                           │
          ┌────────────────┴────────────────┐
          │                                 │
    ┌─────▼─────┐                    ┌──────▼──────┐
    │  Membro   │                    │Bibliotecario│
    │-matricula │                    │-funcionarioId│
    │-historico │                    │-departamento│
    │-bloqueado │                    └─────────────┘
    └─────┬─────┘
          │
    ┌─────▼─────┐
    │ Estudante │
    │ - curso   │
    │ - anoLetivo│
    └───────────┘

    ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
    │    Livro    │────▶│   Autor     │     │  Categoria  │
    │ - titulo    │     │ - nome      │     │ - nome      │
    │ - isbn      │     │-nacionalidade│    │ - descricao │
    │ - quantidade│     └─────────────┘     └─────────────┘
    └──────┬──────┘
           │
    ┌──────▼──────┐     ┌─────────────┐
    │ Emprestimo  │     │   Reserva   │
    │ - data      │     │ - data      │
    │ - devolucao │     │ - ativa     │
    │ - renovacoes│     │ - expirada  │
    └─────────────┘     └─────────────┘
```

### Entidades Principais

| Entidade | Descrição |
|----------|-----------|
| `Pessoa` | Classe abstrata base (herança) |
| `Membro` | Utilizador da biblioteca |
| `Estudante` | Estudante universitário (herda de Membro) |
| `Bibliotecario` | Funcionário administrador |
| `Livro` | Livro do acervo |
| `Autor` | Autor de livros |
| `Categoria` | Categoria de livros |
| `Emprestimo` | Registro de empréstimo |
| `Reserva` | Reserva de livro |
| `Recomendacao` | Sugestão de livros (IA) |
| `Chatbot` | Assistente virtual |
| `OCRService` | Extração de dados por imagem |

---

## 📜 Regras de Negócio

### Empréstimo

| Regra | Descrição |
|-------|-----------|
| **RN01** | Um membro pode ter no máximo **3 livros** emprestados |
| **RN02** | Livro só pode ser emprestado se estiver **disponível** |
| **RN03** | Membro **bloqueado** não pode realizar empréstimos |
| **RN04** | Prazo padrão de empréstimo: **14 dias** |

### Renovação

| Regra | Descrição |
|-------|-----------|
| **RN05** | Máximo de **2 renovações** por empréstimo |
| **RN06** | Não pode renovar se existir **reserva ativa** para o livro |
| **RN07** | Não pode renovar empréstimo **atrasado** |

### Reserva

| Regra | Descrição |
|-------|-----------|
| **RN08** | Só é possível reservar livro **indisponível** |
| **RN09** | Reservas são atendidas por **ordem de data** |
| **RN10** | Reserva expira após **3 dias** se não for retirada |
| **RN11** | Membro não pode ter reservas duplicadas para o mesmo livro |

### Multa

| Regra | Descrição |
|-------|-----------|
| **RN12** | Multa por atraso: **50 KZ por dia** |
| **RN13** | Multa acima de **1000 KZ** bloqueia novos empréstimos |

---

## 📁 Estrutura do Projeto

```
bibliotecaa/
├── pom.xml                           # Configuração Maven
├── README.md                         # Documentação
│
└── src/main/java/isptec/biblioteca/
    │
    ├── Program.java                  # Classe principal
    ├── ServiceFactory.java           # Fábrica de serviços (Singleton)
    │
    ├── enumeracao/                   # Enumerações
    │   ├── EstadoLivro.java          # DISPONIVEL, EMPRESTADO, RESERVADO
    │   ├── EstadoEmprestimo.java     # ATIVO, DEVOLVIDO, ATRASADO
    │   ├── StatusMembro.java         # ATIVO, BLOQUEADO
    │   ├── StatusReserva.java        # PENDENTE, CONCLUIDA, CANCELADA
    │   ├── TipoUsuario.java          # ADMINISTRADOR, ESTUDANTE
    │   └── Perfil.java               # ADMIN, USUARIO, BIBLIOTECARIO
    │
    ├── model/                        # Modelos simplificados para views
    │   ├── Livro.java
    │   ├── Membro.java
    │   ├── Emprestimo.java
    │   ├── Reserva.java
    │   ├── Usuario.java
    │   │
    │   └── entities/                 # Entidades de domínio completas
    │       ├── Pessoa.java           # Classe abstrata (herança)
    │       ├── Membro.java           # Membro da biblioteca
    │       ├── Estudante.java        # Estudante (herda Membro)
    │       ├── Bibliotecario.java    # Administrador
    │       ├── Livro.java            # Livro do acervo
    │       ├── Autor.java            # Autor de livros
    │       ├── Categoria.java        # Categoria de livros
    │       ├── Emprestimo.java       # Empréstimo de livro
    │       ├── Reserva.java          # Reserva de livro
    │       ├── Recomendacao.java     # Recomendação (IA)
    │       ├── Chatbot.java          # Assistente virtual
    │       └── OCRService.java       # Extração por imagem
    │
    ├── service/                      # Interfaces de serviço
    │   ├── LivroService.java
    │   ├── MembroService.java
    │   ├── EstudanteService.java
    │   ├── EmprestimoService.java
    │   ├── ReservaService.java
    │   ├── IAService.java
    │   ├── AuthService.java          # Autenticação (legado)
    │   ├── LibraryService.java       # Serviço geral (legado)
    │   │
    │   └── impl/                     # Implementações
    │       ├── LivroServiceImpl.java
    │       ├── MembroServiceImpl.java
    │       ├── EstudanteServiceImpl.java
    │       ├── EmprestimoServiceImpl.java
    │       ├── ReservaServiceImpl.java
    │       ├── IAServiceImpl.java
    │       └── AuthServiceImpl.java
    │
    ├── repository/                   # Repositórios (Persistência)
    │   ├── Repository.java           # Interface genérica CRUD
    │   ├── LivroRepository.java
    │   ├── MembroRepository.java
    │   ├── EmprestimoRepository.java
    │   └── ReservaRepository.java
    │
    ├── exception/                    # Exceções personalizadas
    │   ├── BibliotecaException.java
    │   ├── EmprestimoException.java
    │   ├── ReservaException.java
    │   └── AutenticacaoException.java
    │
    ├── util/                         # Utilitários
    │   ├── Constantes.java           # Constantes do sistema
    │   ├── DataUtil.java             # Formatação de datas
    │   ├── ValidacaoUtil.java        # Validações
    │   └── MenuHelper.java           # Auxiliar de menus
    │
    └── views/                        # Interface gráfica (JavaFX)
        ├── LoginView.java
        ├── DashboardAdminView.java
        ├── DashboardUserView.java
        ├── LivrosView.java
        ├── MembrosView.java
        ├── EmprestimosView.java
        ├── ReservasView.java
        ├── CatalogoUserView.java
        ├── MinhasReservasView.java
        ├── PerfilUserView.java
        ├── ChatbotView.java
        ├── RelatoriosView.java
        └── TrocarSenhaDialog.java
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 23 ou superior
- Maven 3.8+ (opcional - para build via linha de comando)

### Comandos

```bash
# Clonar o repositório
cd bibliotecaa

# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn javafx:run

# Executar testes
mvn test

# Gerar JAR
mvn package
```

---

## 🔑 Credenciais de Teste

| Tipo | Email | Senha |
|------|-------|-------|
| **Administrador** | admin001@isptec.co.ao | admin123 |
| **Estudante** | 20230001@isptec.co.ao | 1234 |

---

## ✨ Funcionalidades

### Administrador
- 📚 Gestão completa de livros
- 👥 Gestão de membros
- 📋 Gestão de empréstimos e devoluções
- 📊 Relatórios e estatísticas
- 🔒 Bloqueio/desbloqueio de membros

### Estudante
- 🔍 Consulta ao catálogo de livros
- 📖 Visualização de empréstimos ativos
- 📝 Reserva de livros
- 🤖 Acesso ao chatbot
- 👤 Gestão do perfil

### IA e Extras
- 💡 **Recomendação de livros** - Sistema inteligente baseado em histórico de empréstimos
- 📷 **Cadastro via OCR** - Upload de imagem da capa para extração automática de dados
- 🤖 **Chatbot BiblioBot** - Assistente virtual com:
  - Modo local (respostas pré-programadas inteligentes)
  - Modo IA (integração com OpenAI API - configurável)
  - Busca de livros por comando
  - Estatísticas em tempo real
  - Informações sobre políticas, prazos e multas

---

## 🤖 Configuração do Chatbot com IA

O chatbot pode funcionar em dois modos:

### Modo Local (Padrão)
Funciona sem configuração adicional com respostas inteligentes pré-programadas.

### Modo IA (OpenAI)
Para habilitar respostas geradas por IA:
1. Obtenha uma chave de API em [platform.openai.com](https://platform.openai.com)
2. No chatbot, clique no ícone ⚙️
3. Insira sua chave de API (formato: `sk-...`)
4. O chatbot passará a usar GPT para gerar respostas

---

## 📝 Licença

Este projeto foi desenvolvido para fins acadêmicos no âmbito da disciplina de **Engenharia de Software I** do **ISPTEC**.

---

**Desenvolvido com ❤️ no ISPTEC - 2026**
