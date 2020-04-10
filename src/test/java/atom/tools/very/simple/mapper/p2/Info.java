package atom.tools.very.simple.mapper.p2;

import java.util.List;

public class Info {
	
	private String a;
	private String aaaa;
	private Integer  b;
	private boolean c;
	private Adress adress;
	private EnumPays pays;
	
	private List<Voiture> voitures;
	
	private List<String> ls;
	private List<String> ls2;
	private List<String> ls4;
	
	public String getA() {
		return a;
	}
	public List<Voiture> getVoitures() {
		return voitures;
	}
	public void setVoitures(List<Voiture> voitures) {
		this.voitures = voitures;
	}
	public void setA(String a) {
		this.a = a;
	}
	public Integer getB() {
		return b;
	}
	public void setB(Integer b) {
		this.b = b;
	}
	public Boolean getC() {
		return c;
	}
	public void setC(Boolean c) {
		this.c = c;
	}
	public Adress getAdress() {
		return adress;
	}
	public void setAdress(Adress adress) {
		this.adress = adress;
	}
	public void setC(boolean c) {
		this.c = c;
	}
	public List<String> getLs() {
		return ls;
	}
	public void setLs(List<String> ls) {
		this.ls = ls;
	}
	public String getAaaa() {
		return aaaa;
	}
	public void setAaaa(String aaaa) {
		this.aaaa = aaaa;
	}
	public List<String> getLs2() {
		return ls2;
	}
	public void setLs2(List<String> ls2) {
		this.ls2 = ls2;
	}
	public List<String> getLs4() {
		return ls4;
	}
	public void setLs4(List<String> ls4) {
		this.ls4 = ls4;
	}
	public EnumPays getPays() {
		return pays;
	}
	public void setPays(EnumPays pays) {
		this.pays = pays;
	}

}
