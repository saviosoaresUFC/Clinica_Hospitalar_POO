package ClinicaHospitalar;

public enum Label {
    CARDIOLOGISTA, DERMATOLOGISTA, NEUROLOGISTA, OFTALMOLOGISTA, ODONTOLOGISTA,
    GERAL, GINECOLOGISTA;

    // um array de string que recebe a string diag e separa ela em palavras por um espaco em branco
    // um for que percorre o array de string
    // e verifica se a palavra esta contida em uma das especialidades
    // se estiver, retorna a especialidade
    // se nao estiver, retorna a especialidade geral
    static Label verificarEspecialidade(String diag) throws Exception {

        String[] arg = diag.split(" ");
        Label label = null;

        for (String arg1 : arg) {
            switch (arg1) {
                case "coracao":
                case "enfarte":
                case "cardiaco":
                case "coagulacao":
                case "coronaria":
                case "cardiopatia":
                case "colesterol":
                case "trombose":
                case "marcapasso":
                case "aneurisma":
                case "infarto":
                case "hipertensao":
                case "arritmia": {
                    return CARDIOLOGISTA;
                }
                case "pele":
                case "manchas":
                case "impetigo":
                case "dermatite":
                case "micose":
                case "dermico":
                case "botox":
                case "queratose":
                case "celulite":
                case "cicatrizacao":
                case "melanoma":
                case "vitiligo":
                case "acne":
                case "cutaneas": {
                    return DERMATOLOGISTA;
                }
                case "cerebro":
                case "encefalite":
                case "ataxia":
                case "parkinson":
                case "epilepsia":
                case "esclerose":
                case "enxaqueca":
                case "sinapse":
                case "tourette":
                case "avc":
                case "neuropatia":
                case "neuronio":
                case "nervoso":
                case "cerebral":
                case "cabeca":
                case "neuromuscular":
                case "cefalia":
                case "huntinton":
                case "tomografia": {
                    return NEUROLOGISTA;
                }
                case "clareamento":
                case "endodentia":
                case "dentista":
                case "canal":
                case "bruxismo":
                case "dentario":
                case "carie":
                case "periodental":
                case "dentaria":
                case "protese":
                case "bucal":
                case "halito":
                case "oral":
                case "gengivite":
                case "siso": {
                    return ODONTOLOGISTA;
                }
                case "refracao":
                case "miopia":
                case "fundoscopia":
                case "retinopexia":
                case "lente":
                case "pterigio":
                case "retina":
                case "conjuntivite":
                case "esclerite":
                case "lentes":
                case "cornea":
                case "visual":
                case "catarata":
                case "estrabismo":
                case "glaucoma":
                case "visao":
                case "astigmatismo":
                case "retinopatia":
                case "lagrima":
                case "vista":
                case "olho": {
                    return OFTALMOLOGISTA;
                }
                case "gravidez":
                case "menopausa":
                case "mulher":
                case "mama":
                case "urinaria":
                case "dst":
                case "anticoncepcional":
                case "colo":
                case "menstruacao":
                case "menstrual":
                case "utero":
                case "ciclo": {
                    return GINECOLOGISTA;
                }
                default:
                    label = GERAL;
            }
        }
        return label;
    }

    // um switch que recebe a string label e verifica se ela esta contida em uma das especialidades
    // se estiver, retorna a especialidade
    // se nao estiver, retorna uma excecao de especialidade nao encontrada
    static Label converter(String label) throws Exception {
        switch (label.toLowerCase()) {
            case "dermatologista": {
                return Label.DERMATOLOGISTA;
            }
            case "neurologista": {
                return Label.NEUROLOGISTA;
            }
            case "odontologista": {
                return Label.ODONTOLOGISTA;
            }
            case "oftalmologista": {
                return Label.OFTALMOLOGISTA;
            }
            case "cardiologista": {
                return Label.CARDIOLOGISTA;
            }
            case "geral": {
                return Label.GERAL;
            }
            case "ginecologista": {
                return Label.GINECOLOGISTA;
            }
            default:
            // se nao estiver, retorna uma excecao de especialidade nao encontrada
                throw new Exception("Especialidade nao existe no nosso banco de dados");
        }
    }
}
