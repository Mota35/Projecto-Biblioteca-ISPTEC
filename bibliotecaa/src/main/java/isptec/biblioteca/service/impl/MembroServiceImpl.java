package isptec.biblioteca.service.impl;

import isptec.biblioteca.model.entities.Membro;
import isptec.biblioteca.service.MembroService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gestão de membros.
 * Utiliza lista em memória (pode ser substituída por base de dados).
 */
public class MembroServiceImpl implements MembroService {

    private final List<Membro> membros = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean cadastrarMembro(Membro membro) {
        if (membro == null) {
            return false;
        }

        // Verifica se já existe membro com a mesma matrícula
        if (membro.getMatricula() != null && buscarPorMatricula(membro.getMatricula()) != null) {
            return false;
        }

        if (membro.getId() == 0) {
            membro.setId(nextId++);
        }

        membros.add(membro);
        return true;
    }

    @Override
    public boolean atualizarMembro(Membro membro) {
        if (membro == null || membro.getMatricula() == null) {
            return false;
        }

        Membro existente = buscarPorMatricula(membro.getMatricula());
        if (existente != null) {
            membros.remove(existente);
            membros.add(membro);
            return true;
        }
        return false;
    }

    @Override
    public boolean removerMembro(String matricula) {
        if (matricula == null) {
            return false;
        }
        return membros.removeIf(m -> matricula.equals(m.getMatricula()));
    }

    @Override
    public Membro buscarPorMatricula(String matricula) {
        if (matricula == null) return null;
        return membros.stream()
                .filter(m -> matricula.equals(m.getMatricula()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Membro buscarPorId(int id) {
        return membros.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Membro> listarMembros() {
        return new ArrayList<>(membros);
    }

    @Override
    public List<Membro> listarMembrosComEmprestimosAtivos() {
        return membros.stream()
                .filter(m -> m.numeroEmprestimosAtivos() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Membro> listarMembrosBloqueados() {
        return membros.stream()
                .filter(Membro::isBloqueado)
                .collect(Collectors.toList());
    }

    @Override
    public boolean bloquearMembro(String matricula) {
        Membro membro = buscarPorMatricula(matricula);
        if (membro != null) {
            membro.setBloqueado(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean desbloquearMembro(String matricula) {
        Membro membro = buscarPorMatricula(matricula);
        if (membro != null) {
            membro.setBloqueado(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean podeEmprestar(String matricula) {
        Membro membro = buscarPorMatricula(matricula);
        return membro != null && membro.podeEmprestar();
    }
}

