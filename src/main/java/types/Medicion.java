package types;

public class Medicion {

	private int id_medicion;
	private int id_sensor;
	private int id_aula;
	private double ruido;
	private double co2;
	private double humedad;
	private double temperatura;
	private long fecha;
	
	public int getId_medicion() {
		return id_medicion;
	}
	public void setId_medicion(int id_medicion) {
		this.id_medicion = id_medicion;
	}
	public int getId_sensor() {
		return id_sensor;
	}
	public void setId_sensor(int id_sensor) {
		this.id_sensor = id_sensor;
	}
	public int getId_aula() {
		return id_aula;
	}
	public void setId_aula(int id_aula) {
		this.id_aula = id_aula;
	}
	public double getRuido() {
		return ruido;
	}
	public void setRuido(double ruido) {
		this.ruido = ruido;
	}
	public double getCO2() {
		return co2;
	}
	public void setCO2(double cO2) {
		co2 = cO2;
	}
	public double getHumedad() {
		return humedad;
	}
	public void setHumedad(double humedad) {
		this.humedad = humedad;
	}
	public double getTemperatura() {
		return temperatura;
	}
	public void setTemperatura(double temperatura) {
		this.temperatura = temperatura;
	}
	public long getFecha() {
		return fecha;
	}
	public void setFecha(long fecha) {
		this.fecha = fecha;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(co2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (fecha ^ (fecha >>> 32));
		temp = Double.doubleToLongBits(humedad);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id_aula;
		result = prime * result + id_medicion;
		result = prime * result + id_sensor;
		temp = Double.doubleToLongBits(ruido);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(temperatura);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Medicion other = (Medicion) obj;
		if (Double.doubleToLongBits(co2) != Double.doubleToLongBits(other.co2))
			return false;
		if (fecha != other.fecha)
			return false;
		if (Double.doubleToLongBits(humedad) != Double.doubleToLongBits(other.humedad))
			return false;
		if (id_aula != other.id_aula)
			return false;
		if (id_medicion != other.id_medicion)
			return false;
		if (id_sensor != other.id_sensor)
			return false;
		if (Double.doubleToLongBits(ruido) != Double.doubleToLongBits(other.ruido))
			return false;
		if (Double.doubleToLongBits(temperatura) != Double.doubleToLongBits(other.temperatura))
			return false;
		return true;
	}
	public Medicion(int id_medicion, int id_sensor, int id_aula, double ruido, double co2, double humedad,
			double temperatura, long fecha) {
		super();
		this.id_medicion = id_medicion;
		this.id_sensor = id_sensor;
		this.id_aula = id_aula;
		this.ruido = ruido;
		this.co2 = co2;
		this.humedad = humedad;
		this.temperatura = temperatura;
		this.fecha = fecha;
	}
	public Medicion() {
		this.id_medicion = 0;
		this.id_sensor = 0;
		this.id_aula = 0;
		this.ruido = 0;
		this.co2 = 0;
		this.humedad = 0;
		this.temperatura = 0;
		this.fecha = 0;
		
	}
}
