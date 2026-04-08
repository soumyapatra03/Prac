import java.util.*;

class Antenna {
	private int antennaId;
	private String antennaName;
	private double antennaVSWR;
	private double signalStrength;
	
	public Antenna(int antennaId, String antennaName, double antennaVSWR, double signalStrength) {
		this.antennaId = antennaId;
		this.antennaName = antennaName;
		this.antennaVSWR = antennaVSWR;
		this.signalStrength = signalStrength;
	}

	public int getAntennaId() {
		return antennaId;
	}

	public String getAntennaName() {
		return antennaName;
	}

	public double getAntennaVSWR() {
		return antennaVSWR;
	}

	public double getSignalStrength() {
		return signalStrength;
	}

	public void setAntennaId(int antennaId) {
		this.antennaId = antennaId;
	}

	public void setAntennaName(String antennaName) {
		this.antennaName = antennaName;
	}

	public void setAntennaVSWR(double antennaVSWR) {
		this.antennaVSWR = antennaVSWR;
	}

	public void setSignalStrength(double signalStrength) {
		this.signalStrength = signalStrength;
	}
}

class Satellite {
	private int satelliteId;
	private String satelliteName;
	private String orbitType;
	private ArrayList<Antenna> antennaList;
	
	public Satellite(int satelliteId, String satelliteName, String orbitType, ArrayList<Antenna> antennaList) {
		this.satelliteId = satelliteId;
		this.satelliteName = satelliteName;
		this.orbitType = orbitType;
		this.antennaList = antennaList;
	}

	public int getSatelliteId() {
		return satelliteId;
	}

	public String getSatelliteName() {
		return satelliteName;
	}

	public String getOrbitType() {
		return orbitType;
	}

	public ArrayList<Antenna> getAntennaList() {
		return antennaList;
	}

	public void setSatelliteId(int satelliteId) {
		this.satelliteId = satelliteId;
	}

	public void setSatelliteName(String satelliteName) {
		this.satelliteName = satelliteName;
	}

	public void setOrbitType(String orbitType) {
		this.orbitType = orbitType;
	}

	public void setAntennaList(ArrayList<Antenna> antennaList) {
		this.antennaList = antennaList;
	}
}

class SatelliteNotFoundException extends Exception {
	public SatelliteNotFoundException(String message) {
		super(message);
	}
}

class NoAntennaFoundException extends Exception {
	public NoAntennaFoundException(String message) {
		super(message);
	}
}

class VSWRComparatorAsc implements Comparator<Antenna> {
	public int compare(Antenna a1, Antenna a2) {
		if(a1.getAntennaVSWR() < a2.getAntennaVSWR()) return -1;
		if(a1.getAntennaVSWR() > a2.getAntennaVSWR()) return 1;
		return 0;
	}
}

class VSWRComparatorDesc implements Comparator<Antenna> {
	public int compare(Antenna a1, Antenna a2) {
		if(a1.getAntennaVSWR() > a2.getAntennaVSWR()) return -1;
		if(a1.getAntennaVSWR() < a2.getAntennaVSWR()) return 1;
		return 0;
	}
}

class Service {
	public void findSatellitesByOrbitType(ArrayList<Satellite> satelliteList, String orbitType) throws SatelliteNotFoundException {
		ArrayList<Satellite> list = new ArrayList<>();
		for(Satellite s : satelliteList) {
			if(s.getOrbitType().trim().equalsIgnoreCase(orbitType.trim())) {
				list.add(s);
			}
		}
		if(list.isEmpty()) {
			throw new SatelliteNotFoundException("No satellite found with given orbit type");
		} else {
			for(int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getSatelliteName());
			}
		}
	}
	
	public void filterAndSortAntennas(ArrayList<Satellite> satelliteList, double maxVSWR) throws NoAntennaFoundException {
		ArrayList<Antenna> list = new ArrayList<>();
		for(Satellite s : satelliteList) {
			for(Antenna a : s.getAntennaList()) {
				if(a.getAntennaVSWR() < maxVSWR && a.getSignalStrength() > 50) {
					list.add(a);
				}
			}
		}
		if(list.isEmpty()) {
			throw new NoAntennaFoundException("No antenna found matching criteria");
		} else {
			Comparator<Antenna> satCom = new VSWRComparatorAsc();
            //Comparator<Antenna> satCom = new VSWRComparatorDesc();

			Collections.sort(list, satCom);
			for(int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getAntennaName() + " " + list.get(i).getAntennaVSWR());
			}
		}
	}
}

public class OrSat {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		ArrayList<Satellite> sat = new ArrayList<>();
		
		for(int i = 0; i < n; i++) {
			int satelliteId = sc.nextInt();
			sc.nextLine();
			String satelliteName = sc.nextLine();
			String orbitType = sc.nextLine();
			int antCount = sc.nextInt();
			
			ArrayList<Antenna> ant = new ArrayList<>();
			for(int j = 0; j < antCount; j++) {
				int antennaId = sc.nextInt();
				sc.nextLine();
				String antennaName = sc.nextLine();
				double antennaVSWR = sc.nextDouble();
				double signalStrength = sc.nextDouble();
				
				ant.add(new Antenna(antennaId, antennaName, antennaVSWR, signalStrength));
			}
			sat.add(new Satellite(satelliteId, satelliteName, orbitType, ant));
		}
		
		sc.nextLine();
		String orType = sc.nextLine();
		double maxVSWR = sc.nextDouble();
		sc.close();
		
		Service satServ = new Service();
		
		try {
			satServ.findSatellitesByOrbitType(sat, orType);
		} catch(SatelliteNotFoundException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		
		try {
			satServ.filterAndSortAntennas(sat, maxVSWR);
		} catch(NoAntennaFoundException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}
}