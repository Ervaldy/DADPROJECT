package types;

public class Actuacion {
	
	private int id_actuacion;
	private int id_medicion;
	public int getId_actuacion() {
		return id_actuacion;
	}
	public void setId_actuacion(int id_actuacion) {
		this.id_actuacion = id_actuacion;
	}
	public int getId_medicion() {
		return id_medicion;
	}
	public void setId_medicion(int id_medicion) {
		this.id_medicion = id_medicion;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_actuacion;
		result = prime * result + id_medicion;
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
		Actuacion other = (Actuacion) obj;
		if (id_actuacion != other.id_actuacion)
			return false;
		if (id_medicion != other.id_medicion)
			return false;
		return true;
	}
	public Actuacion(int id_actuacion, int id_medicion) {
		super();
		this.id_actuacion = id_actuacion;
		this.id_medicion = id_medicion;
	}
	public Actuacion() {
		super();
		this.id_actuacion = 0;
		this.id_medicion = 0;
	}
	@Override
	public String toString() {
		return "Actuacion [id_actuacion=" + id_actuacion + ", id_medicion=" + id_medicion + "]";
	}
	
	

}
