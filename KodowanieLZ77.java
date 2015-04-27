package lz77;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class KodowanieLZ77 {
	private static final int BuferKodowaniaRozmiar = 4;
	private static final int BuferSlownikaRozmiar = 4;

	private String wejscie;
	private String bufSlownikowy = ""; 
	private StringBuilder wyjscieKody;
	private int bufKodowaniaPtr; // okno kodowania
	
	
	public void zakoduj(String plikDoZakodowania, String plikWyjsciowy) throws IOException {
		inicjalizujDane();
		rozpocznijKodowanie();
		zapiszDoPliku(plikWyjsciowy);
	}

	private void zapiszDoPliku(String nazwaPliku) throws IOException {
		File newTextFile = new File(nazwaPliku);
		FileWriter fw = new FileWriter(newTextFile);
		fw.write(wyjscieKody.toString());
		fw.close();
		
	}

	private void rozpocznijKodowanie() {
		while (bufKodowaniaPtr < wejscie.length()) {
			String znak ="";
			String podciag = wejscie.substring(bufKodowaniaPtr, bufKodowaniaPtr+1);
			if (bufSlownikowy.indexOf(podciag) == -1) {
				//brak elementu w bufSlown.
				//wyslij (0,kodZnaku)
				wyjscieKody.append(0);
				wyjscieKody.append(podciag);
				//usun 1 elem z bufKodowania i dodaj go do bufSlownika
				znak = wejscie.substring(bufKodowaniaPtr, bufKodowaniaPtr+1);
				System.out.println("Usunięto z bufKodowania: "+ znak);
				//aktualizujBufSlow 
				bufSlownikowy = bufSlownikowy.substring(1); //skroc o ten pierwszy znak
				bufSlownikowy += znak;
				System.out.println("Dodano do bufSlownikowego 1 znak, teraz bufSlown: "+ bufSlownikowy);
				
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
				int przesuniecie = BuferSlownikaRozmiar - indexPasujacy;
				wyjscieKody.append(przesuniecie);
				wyjscieKody.append(znak.length());
				
				//aktualnij buferKodowania
				bufKodowaniaPtr += znak.length();
				//aktualnij bufSlownikowy
				bufSlownikowy = bufSlownikowy.substring(znak.length());
				bufSlownikowy +=znak;
					
			}
		}// end while loop
	}

	private void inicjalizujDane() throws IOException {
		wczytajPlik();
		wyjscieKody = new StringBuilder();
		bufKodowaniaPtr = 0;
		for(int i=0; i<BuferSlownikaRozmiar; i++){
			bufSlownikowy += " ";
		}
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
