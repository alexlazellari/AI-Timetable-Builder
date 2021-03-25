import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class HillClimbing {
	private ArrayList<State> states;

	public HillClimbing() {
		this.states = null;
	}

	public State HC(State initialState) {

		this.states = new ArrayList<State>();
		this.states.add(initialState);
		while (this.states.size() >0) {
			State currentState = this.states.remove(0);
			
			if (currentState.getErrorCounter() <= 7) {
				return currentState;
			}
			ArrayList<State> children = currentState.getChildren(65);
			
			Collections.sort(children);
			if (currentState.getErrorCounter()<children.get(0).getErrorCounter()){
				children.add(currentState);
			}
			for(int t=0 ; t<2;t++) {
				if(!this.states.contains(children.get(t))) {
					this.states.add(children.get(t));
				}
			}	
			Collections.sort(this.states);
			

			System.out.println(this.states.get(0)+"Value : "+this.states.get(0).getErrorCounter()+" Size of list: "+this.states.size());
			  
		
			  
			 
			 

		}
		return null;
	}

}
