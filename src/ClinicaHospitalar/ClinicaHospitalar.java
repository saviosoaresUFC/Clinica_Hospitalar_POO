package ClinicaHospitalar;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Scanner;
/**
 *
 * @author savio
 */
public class ClinicaHospitalar {

    public static void main(String args[]) throws SQLException, Exception {
        Clinica clinica = new Clinica();
        Sql sql = new Sql();// Banco de dados

        //chama os comando a ser mostrado ao usuario
        showComandos();

        while (true) {
            String line = input();
            println("$" + line);
            String[] arg = line.split(" ");

            //atualiza as listas: pacientes e medicos
            clinica.update();
            try {
                //verifica qual comando foi digitado e executa o comando correspondente a ele, transformando-o em minusculo para nao dar erro
                switch (arg[0].toLowerCase()) {
                    case "cls": {
                        //limpar o terminal
                        clearConsole();
                        //mostrar os comandos
                        showComandos();
                        break;
                    }
                    case "end": {
                        return;
                    }
                    case "addmed": {
                        print("NOME: ");
                        String nome = input();
                        print("TELEFONE: ");
                        String telefone = input();
                        println("  =====  ESPECIALIZACOES  =====");
                        println("- CARDIOLOGISTA  - DERMATOLOGISTA\n- NEUROLOGISTA   - GINECOLOGISTA\n"
                                + "- OFTALMOLOGISTA - ODONTOLOGISTA\n- GERAL");
                        print("QUAL A ESPECIALIZACAO? ");
                        String especializacao = input();

                        //passa um Medico para adicionar, converte a especializacao para um ENUM, e inicia o paciente dele como null
                        clinica.addPessoa(new Medico(nome.toLowerCase(), telefone, Label.converter(especializacao), null));
                        break;
                    }
                    case "addpac": {
                        print("NOME: ");
                        String nome = input();
                        print("TELEFONE: ");
                        String telefone = input();
                        print("DIAGNOSTICO: ");
                        String problema = input();

                        //passa um Paciente para adicionar e inicia a consulta dele como null
                        clinica.addPessoa(new Paciente(nome.toLowerCase(), telefone, problema.toLowerCase(), null));
                        break;
                    }
                    case "show":
                        //mostra os pacientes e os medicos
                        print(clinica.toString());
                        //mostra as consultas
                        print(sql.pesquisaConsulta());
                        break;
                    case "diagnostico": {
                        print("Diagnostico do paciente: ");
                        String diag = input();
                        //passo o diagonostico para verificar qual a melhor especialidade para o seu diagnostico
                        Label saida = Label.verificarEspecialidade(diag);
                        //escrevo a especialidade como titulo, e verifico quais medicos com essa especialidade
                        println("==== " + saida + " ====\nMedico(s): " + clinica.getMedicosPorEspecialidade(saida));
                        break;
                    }
                    case "addcons": {
                        println("PLANO OU PARTICULAR? ");
                        String opcao = input();
                        switch (opcao.toUpperCase()) {
                            case "PARTICULAR": {
                                //ler o id
                                int numRegPac = lerID();
                                // se nao for -1
                                if (numRegPac != -1) {
                                    print("NOME DO PACIENTE: ");
                                    String nomePac = input();
                                    //existePac retorna um Paciente, e ve se existe ou nao o paciente passando o numero_de_registro e o nome do paciente
                                    Paciente pac = clinica.existePac(numRegPac, nomePac.toLowerCase());
                                    //se nao tiver paciente
                                    if (pac == null) {
                                        println("Paciente Inexistente");
                                        break;
                                    }
                                    print("NOME DO MEDICO: ");
                                    String nomeMed = input();
                                    //existeMed retorna um Medico, e ve se existe ou nao o medico passando o numero_de_registro e o nome do medico
                                    Medico med = clinica.existeMed(nomeMed.toLowerCase());
                                    //se nao tiver medico
                                    if (med == null) {
                                        println("Medico Inexistente");
                                        break;
                                    }
                                    print("DATA: ");
                                    String data = input();
                                    print("VALOR: ");
                                    String valor = input();
                                    //adiciona uma consulta passando um Objeto do tipo Particular que eh a classe filha de Consulta
                                    clinica.addConsulta(new Particular(pac, med, data, pac.problema, valor));
                                    break;
                                }
                                break;
                            }
                            case "PLANO": {
                                int numRegPac = lerID();
                                if (numRegPac != -1) {
                                    print("NOME DO PACIENTE: ");
                                    String nomePac = input();
                                    Paciente pac = clinica.existePac(numRegPac, nomePac.toLowerCase());
                                    if (pac == null) {
                                        println("Paciente Inexistente");
                                        break;
                                    }
                                    print("NOME DO MEDICO: ");
                                    String nomeMed = input();
                                    Medico med = clinica.existeMed(nomeMed.toLowerCase());
                                    if (med == null) {
                                        println("Medico Inexistente");
                                        break;
                                    }
                                    print("DATA: ");
                                    String data = input();
                                    print("NOME DO PLANO: ");
                                    String nomePlan = input();
                                    //adiciona uma consulta passando um Objeto do tipo Plano que eh a classe filha de Consulta
                                    clinica.addConsulta(new Plano(pac, med, data, pac.problema, nomePlan.toUpperCase()));
                                    break;
                                }
                                break;
                            }
                            default:
                                println("Tipo de consulta nao especificado!");
                        }
                        break;
                    }
                    case "remarcarcons": {
                        int numRegPac = lerID();
                        if (numRegPac != -1) {
                            print("NOME DO PACIENTE: ");
                            String nomePac = input();
                            print("NOME DO MEDICO: ");
                            String nomeMed = input();
                            print("NOVA DATA: ");
                            String data = input();
                            // vai no sql e remarca a consulta passando o numero_registro_paciente, nome_paciente, nome_medico e a data a ser remarcada
                            sql.remarcarConsulta(numRegPac, nomePac.toLowerCase(), nomeMed.toLowerCase(), data);
                        }
                        break;
                    }
                    case "infopac": {
                        int numRegPac = lerID();
                        if (numRegPac != -1) {
                            print("NOME DO PACIENTE: ");
                            String nomePac = input();
                            //mostra todas as informacoes do paciente com o id indicado
                            println(clinica.infoPaciente(numRegPac, nomePac.toLowerCase()));
                        }
                        break;
                    }
                    case "infomed": {
                        print("NOME DO MEDICO: ");
                        String nomeMed = input();
                        //mostra todas as informacoes do medico
                        println(clinica.infoMedico(nomeMed.toLowerCase()));
                        break;
                    }
                    case "removercons": {
                        int numRegPac = lerID();
                        if (numRegPac != -1) {
                            print("NOME DO PACIENTE: ");
                            String nomePac = input();
                            if (clinica.existePac(numRegPac, nomePac.toLowerCase()) == null) {
                                println("Paciente Inexistente.");
                                break;
                            }
                            print("NOME DO MEDICO: ");
                            String nomeMed = input();
                            if (clinica.existeMed(nomeMed.toLowerCase()) == null) {
                                println("Medico Inexistente.");
                                break;
                            }
                            //remove a consulta passando o numero_registro, nome do paciente e o nome do medico
                            sql.removerConsultaGeral(numRegPac, nomePac.toLowerCase(), nomeMed.toLowerCase());
                        }
                        break;
                    }
                    case "removerpac": {
                        int idpac = lerID();
                        if (idpac != -1) {
                            print("NOME DO PACIENTE: ");
                            String nomePac = input();
                            //remove o paciente passando o numero_registro e o nome do paciente
                            clinica.removerPaciente(idpac, nomePac.toLowerCase());
                        }
                        break;
                    }
                    case "removermed": {
                        print("NOME DO MEDICO: ");
                        String nomeMed = input();
                        //remove o medico passando o numero_registro e o nome do medico
                        clinica.removerMedico(nomeMed.toLowerCase());
                        break;
                    }
                    default:
                        println("fail: comando invalido");
                }
            } catch (Exception e) {
                println(e.getMessage());
            }
        }
    }
    private static final Scanner scanner = new Scanner(System.in);

    public static String input() {
        return scanner.nextLine();
    }

    public static void println(Object value) {
        System.out.println(value);
    }

    public static void print(Object value) {
        System.out.print(value);
    }

    public static void showComandos() {
        println("======== COMANDOS ========");
        println("- addPac    - addMed     - addCons");
        println("- infoPac   - infoMed    - remarcarCons");
        println("- removerPac   - removerMed  - removerCons");
        println("- diagosnito  - show   - end   - cls");
    }

    //funcao que limpa o console
    private static void clearConsole() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (AWTException e) {
            println(e);
        }
    }

    public static int lerID() {
        try {
            print("ID DO PACIENTE: ");
            int idpac = scanner.nextInt();
            scanner.nextLine();
            return idpac;
        } catch (Exception e) {
            scanner.nextLine();
            print("AVISO: O ID do paciente deve ser um numero. Por favor, repita a operacao.\n");
            return -1;
        }
    }
}
