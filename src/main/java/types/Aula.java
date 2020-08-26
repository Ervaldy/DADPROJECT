package types;

public class Aula {

	private int id_aula;
	private String nombre;
	private boolean peticion;
	
	public boolean isPeticion() {
		return peticion;
	}
	public void setPeticion(boolean peticion) {
		this.peticion = peticion;
	}
	public boolean getPeticion() {
		return this.peticion;
	}
	public int getId_aula() {
		return id_aula;
	}
	public void setId_aula(int id_aula) {
		this.id_aula = id_aula;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_aula;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Aula other = (Aula) obj;
		if (id_aula != other.id_aula)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	public Aula(int id_aula, String nombre, boolean peticion) {
		super();
		this.id_aula = id_aula;
		this.nombre = nombre;
		this.peticion = peticion;
	}
	
	public Aula(int id_aula, boolean peticion) {
		super();
		this.id_aula = id_aula;
		this.nombre = "";
		this.peticion = peticion;
	}
	public Aula() {
		super();
		this.id_aula = 0;
		this.nombre = "";
	}
	@Override
	public String toString() {
		return "Aula [id_sensor=" + id_aula + ", nombre=" + nombre + "]";
	}
	
	
}
