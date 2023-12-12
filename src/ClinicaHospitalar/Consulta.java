package ClinicaHospitalar;

public class Consulta {
    protected Paciente paciente;
    protected Medico medico;
    protected String data;
    protected String diagnostico;
    protected double valorConsulta;
    
    public Consulta( Paciente paciente, Medico medico, String data , String diagnostico) {
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.diagnostico = diagnostico;
    }
    
    public void setData(String data){
        this.data = data;
    }
    
    public double getValorConsulta() {
        return valorConsulta;
    }    
}