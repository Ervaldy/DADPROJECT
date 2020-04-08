package types;

public class Sensor {
	
	private int id_sensor;
	private String nombre;
	public int getId_sensor() {
		return id_sensor;
	}
	public void setId_sensor(int id_sensor) {
		this.id_sensor = id_sensor;
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
		result = prime * result + id_sensor;
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
		Sensor other = (Sensor) obj;
		if (id_sensor != other.id_sensor)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	public Sensor(int id_sensor, String nombre) {
		super();
		this.id_sensor = id_sensor;
		this.nombre = nombre;
	}
	public Sensor() {
		super();
		this.id_sensor = 0;
		this.nombre = "";
	}
	@Override
	public String toString() {
		return "Sensor [id_sensor=" + id_sensor + ", nombre=" + nombre + "]";
	}
	
	

}
