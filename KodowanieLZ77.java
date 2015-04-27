package lz77;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class KodowanieLZ77 {
	private static final int BuferKodowaniaRozmiar = 128;
	private static final int BuferSlownikaRozmiar = 255;

	private String wejscie;
	private String bufSlownikowy = ""; 
	//private StringBuilder wyjscieKody;
	private int bufKodowaniaPtr; // okno kodowania
	private ArrayList<Kod> listaKodow; 
	
	
	public void zakoduj(String plikDoZakodowania, String plikWyjsciowy) throws IOException {
		inicjalizujDane();
		rozpocznijKodowanie();
		zapiszDoPliku(plikWyjsciowy);
	}

	private void zapiszDoPliku(String nazwaPliku) throws IOException {
//		File newTextFile = new File(nazwaPliku);
//		FileWriter fw = new FileWriter(newTextFile);
//		fw.write(wyjscieKody.toString());
//		fw.close();
		byte[] wyjscie = new byte[listaKodow.size()*2];
		int i=-1;
		System.out.println("\n_____________________________________" );
		for(Kod x: listaKodow){
			wyjscie[++i] = x.getPrzesWSlown();
		//	System.out.print(" "+ wyjscie[i]+ " " );
			wyjscie[++i] = x.getIleSkopiowac();
		//	System.out.print(" "+ wyjscie[i]+ " " );
		}
		System.out.println("_____________________________________\n" );
		BufferedOutputStream bos = null;
		try {
			FileOutputStream fs = new FileOutputStream(new File(nazwaPliku));
			bos = new BufferedOutputStream(fs);
			bos.write(wyjscie);
			bos.close();
			bos = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void rozpocznijKodowanie() {
		while (bufKodowaniaPtr < wejscie.length()) {
			String znak ="";
			String podciag = wejscie.substring(bufKodowaniaPtr, bufKodowaniaPtr+1);
			if (bufSlownikowy.indexOf(podciag) == -1) {
				//brak elementu w bufSlown.
				//wyslij (0,kodZnaku)
//				wyjscieKody.append(0);
//				wyjscieKody.append(podciag);
				byte i = (byte)0;
				byte j = (byte)podciag.charAt(0);
			//	System.out.print("    "+ 0+ " "+ podciag.charAt(0));
				listaKodow.add(new Kod(i,j));
				//usun 1 elem z bufKodowania i dodaj go do bufSlownika
				znak = wejscie.substring(bufKodowaniaPtr, bufKodowaniaPtr+1);
			
				//aktualizujBufSlow 
				
				bufSlownikowy += znak;
				aktualizujBufSlownikowy();
			//	System.out.println("Dodano do bufSlownikowego 1 znak, teraz bufSlown: "+ bufSlownikowy);
				
				//aktualizuj odkodowana ilosc 
				bufKodowaniaPtr ++;
			}else{//jest Znak w BufSlownikowym
				//poszukuj najdluzszego podciagu
				int liczbaPasujacychZnakow = 1;
				int indexPasujacy =0;
				
				while (liczbaPasujacychZnakow < BuferSlownikaRozmiar && (bufKodowaniaPtr+ liczbaPasujacychZnakow < wejscie.length())) {
					//pobierz kolejna literke ze slownika
					podciag += wejscie.substring(bufKodowaniaPtr+ liczbaPasujacychZnakow, bufKodowaniaPtr+liczbaPasujacychZnakow+1);

				    indexPasujacy = bufSlownikowy.indexOf(podciag);

					if (indexPasujacy != -1) {
						liczbaPasujacychZnakow++;
					} else {
						// The matching test failed. Break out of the
						// loop.
						break;
					}// end else
				}// end while
				// liczbaPasujacychZnakow mowi o ile mam teraz przesunac BuferSlownika w lewo i usunac z buferaKodowania znakow
				//jest tez liczba ktora jako drugi argument zostaje wysylana na wyjscie 
				
				znak =  wejscie.substring(bufKodowaniaPtr, bufKodowaniaPtr+liczbaPasujacychZnakow);
				indexPasujacy = bufSlownikowy.indexOf(znak);// tutaj mam informacje o gdzie w slowniku znajduje sie podciag
				int przesuniecie=0;
//				if(bufSlownikowy.length()>=BuferSlownikaRozmiar){
//				   przesuniecie = BuferSlownikaRozmiar - indexPasujacy;
//				}else{
					przesuniecie= bufSlownikowy.length() - indexPasujacy;
			//	}
//				wyjscieKody.append(przesuniecie);
//				wyjscieKody.append(znak.length());
				byte i = (byte)przesuniecie;
				byte j = (byte)liczbaPasujacychZnakow;
				System.out.print("    "+ przesuniecie+ " "+ liczbaPasujacychZnakow);
				listaKodow.add(new Kod(i,j));
				
				
				//aktualnij buferKodowania
				bufKodowaniaPtr += znak.length();
				//aktualnij bufSlownikowy
				if(bufSlownikowy.length()>=BuferSlownikaRozmiar){
				bufSlownikowy = bufSlownikowy.substring(znak.length());
				}
				bufSlownikowy +=znak;
				aktualizujBufSlownikowy();
					
			}
		}// end while loop
	}

	private void aktualizujBufSlownikowy() {
		if(bufSlownikowy.length()>=BuferSlownikaRozmiar){
			bufSlownikowy = bufSlownikowy.substring(bufSlownikowy.length()-BuferSlownikaRozmiar); //skroc o ten pierwszy znak
			}
		
	}

	private void inicjalizujDane() throws IOException {
		wczytajPlik();
//		wyjscieKody = new StringBuilder();
		listaKodow = new ArrayList<Kod>();
		bufKodowaniaPtr = 0;
//		for(int i=0; i<BuferSlownikaRozmiar; i++){
//			bufSlownikowy += " ";
//		}
	}

	private void wczytajPlik() throws IOException {
		File file = new File("test1.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuffer content = new StringBuffer();
		String line;

		while ((line = br.readLine()) != null) {
			content.append(line); 
			content.append('\n');
		}
		br.close();
		wejscie = content.toString();
		
	}

}
