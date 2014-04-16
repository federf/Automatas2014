
import automata.FA;

public class Main {

	public static void main(String[] args) {
        try{
            String curDir=System.getProperty("user.dir");
            FA.parse_form_file(curDir+"/src/test/nfalambda2.dot");
        }catch (Exception e){ //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
	}

}
