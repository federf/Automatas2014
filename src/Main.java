
import automata.FA;
import automata.NFALambda;
import automata.State;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        try {
            String curDir = System.getProperty("user.dir");
            NFALambda automata = (NFALambda)FA.parse_form_file(curDir + "/src/test/nfalambda1.dot");
            char c='c';
            LinkedList<State> s=new LinkedList();
            for(State a: automata.states()){
                s.add(a);
            }
            System.out.println("Estados: " );
            for(int i=0;i<s.size();i++){
                System.out.print(s.get(i).name()+", ");
            }
            
            State q4=s.getFirst();//toma el 1er elemento del alfabeto (inicial)
            //automata.delta(automata.initial_state(),'a');
            //String toDot=automata.to_dot();
            //System.out.println(toDot);
            automata.delta(q4, c); //calcula la delta (prueba con AFNLambda)

        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }

}
