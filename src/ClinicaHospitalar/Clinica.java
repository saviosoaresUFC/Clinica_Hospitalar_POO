package ClinicaHospitalar;

import java.sql.SQLException;
import java.util.*;

public class Clinica {
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Medico> medicos = new ArrayList<>();

    Sql sql = new Sql();

    public Clinica() throws SQLException {
    }

    // ADICIONAR PESSOA
    // se pessoa for instanciado de Paciente, chama o adicionarPaciente que esta no
    // sql e retorna a funcao
    // se nao, chama o adicionarMedico passando os parametros
    public void addPessoa(Pessoa pessoa) throws Exception {
        if (pessoa instanceof Paciente) {
            sql.adicionarPaciente(
                    ((Paciente) pessoa).nome,
                    ((Paciente) pessoa).telefone,
                    ((Paciente) pessoa).problema);
            return;
        }

        sql.adicionarMedico(
                ((Medico) pessoa).nome,
                ((Medico) pessoa).telefone,
                ((Medico) pessoa).especializacao);
    }

    // ADICIONAR CONSULTA
    // se consulta for instanciado de Particular, adiciona uma consulta,
    // e adiciona tbm no paciente e no medico, passando os parametros
    public void addConsulta(Consulta consulta) throws SQLException {
        if (consulta instanceof Particular) {
            sql.adicionarConsulta(
                    consulta.paciente.numeroRegistroPaciente,
                    consulta.paciente.nome,
                    consulta.medico.nome,
                    consulta.medico.especializacao,
                    consulta.diagnostico,
                    consulta.data,
                    "Particular",
                    ((Particular) consulta).valor);
            sql.addConsultaNoPaciente(consulta.paciente.numeroRegistroPaciente);
            sql.addConsultaNoMedico(consulta.medico.nome);
            return;
        }

        sql.adicionarConsulta(
                consulta.paciente.numeroRegistroPaciente,
                consulta.paciente.nome,
                consulta.medico.nome,
                consulta.medico.especializacao,
                consulta.diagnostico,
                consulta.data,
                "Plano(" + ((Plano) consulta).nomePlano + ")",
                0);
        sql.addConsultaNoPaciente(consulta.paciente.numeroRegistroPaciente);
        sql.addConsultaNoMedico(consulta.medico.nome);
    }

    // ADICIONAR PACIENTE
    // percorro a lista de pacientes, se o paciente for igual ao paciente passado
    // como parametro,
    // retorna a funcao passando o paciente encontrado
    public Paciente existePac(int numRegPac, String nomePac) {
        for (Paciente pac : pacientes) {
            if (pac.nome.equals(nomePac) && pac.numeroRegistroPaciente == numRegPac) {
                return pac;
            }
        }
        return null;
    }

    // ADICIONAR MEDICO
    // percorro a lista de medicos, se o medico for igual ao medico passado como
    // parametro,
    // retorna a funcao passando o medico encontrado
    public Medico existeMed(String nomeMed) {
        for (Medico med : medicos) {
            if (med.nome.equals(nomeMed)) {
                return med;
            }
        }
        return null;
    }

    // pega o medico pela especialidade que eh passada como parametro,
    // retornando o nome do medico encontrado
    public String getMedicosPorEspecialidade(Label especialidade) {
        String saida = "";
        for (Medico med : medicos) {
            if (med.especializacao.equals(especialidade)) {
                saida += "[" + med.nome + "]";
            }
        }
        if (saida.equals("")) {
            return "Vazio";
        }
        return saida;
    }

    // informacoes do paciente, retornando a informacao do paciente encontrado
    // se nao, retorna uma excecao
    public String infoPaciente(int numRegPac, String nome) throws Exception {
        for (Paciente pac : pacientes) {
            if (nome.equals(pac.nome) && numRegPac == pac.numeroRegistroPaciente) {
                return pac.infoPaciente();
            }
        }
        throw new Exception("Paciente Inexistente");
    }

    // informacoes do medico, retornando a informacao do medico encontrado
    // se nao, retorna uma excecao
    public String infoMedico(String nome) throws Exception {
        for (Medico med : medicos) {
            if (nome.equals(med.nome)) {
                return med.infoMedico();
            }
        }
        throw new Exception("Medico Inexistente");
    }

    // atualiza a lista de pacientes e medicos
    public void update() throws Exception {
        try {
            pacientes = new ArrayList<>();
            pacientes = new ArrayList<>((List<Paciente>) sql.updateListPac((ArrayList<Paciente>) pacientes));
            medicos = new ArrayList<>();
            medicos = new ArrayList<>((List<Medico>) sql.updateListMed((ArrayList<Medico>) medicos));
        } catch (SQLException ex) {
            // Logger.getLogger(Clinica.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    // REMOVER PACIENTE
    // se o paciente existir, remove o paciente
    // se nao, retorna uma excecao
    // se o paciente tiver consulta, pergunta se deseja excluir a consulta e remove
    // a consulta
    public void removerPaciente(int idPac, String nomePac) throws Exception {
        Paciente pac = existePac(idPac, nomePac);
        if (pac == null) {
            throw new Exception("Paciente " + nomePac + " com id " + idPac + " inexistente.");
        }
        if (pac.consulta != null) {
            System.out.print(nomePac.toUpperCase() + " tem consulta(s) existente(s), deseja exclui-la(s)? (s/n) ");
            String confirmar = ClinicaHospitalar.input();
            if (confirmar.equals("s")) {
                int idpac = idPac;
                sql.removerConsultadoPaciente(idpac, nomePac);
            }
        }
        int idpac = idPac;
        sql.removerPaciente(idpac, nomePac);
    }

    // REMOVER MEDICO -> Manipulação de Dados e Informações
    // se o medico existir, remove o medico
    // se nao, retorna uma excecao
    // se o medico tiver consulta, avise-o que ele tem consulta existente e pegue o nome do paciente
    public void removerMedico(String nomeMed) throws Exception {
        Medico med = existeMed(nomeMed);
        if (med == null) {
            throw new Exception("Medico " + nomeMed + " inexistente.");
        }
        sql.removerMedico(nomeMed);
        if (med.consulta != null) {
            ClinicaHospitalar.println(nomeMed.toUpperCase() + " tem consulta existente, avise aos seguintes pacientes: ");
            ClinicaHospitalar.print(sql.getNomeDoPacienteNaConsultaDoMedico(nomeMed));
        }
    }

    @Override
    public String toString() {
        String saida = "";

        saida += "==== PACIENTES ====\n";

        for (Paciente pac : pacientes) {
            saida += "[" + pac + "]\n";
        }

        saida += "==== MEDICOS ==== \n";
        for (Medico med : medicos) {
            saida += "[" + med + "]\n";
        }
        return saida;
    }

}
