package automata;

public class State {

	private String _name;
	
	public State(String name) {
		_name = name;
	}
	
	public String name() {
		return _name;
	}

	// Optional - Use to get equality based in abtributes, 
	// instead than by reference (which is Java's default). 
	
	/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		return true;
	}
	*/
}
