package lz77;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DekodowanieLZ77 {
	private static final int BuferKodowaniaRozmiar = 128;
	private static final int BuferSlownikaRozmiar = 255;

	String wyjscie = "";
	String bufSlownikowy = "";
	private StringBuilder wyjscieTekst;
//	private String wejscieKody;
//	private String doZdekodowania;
	private int bufKodowaniaPtr;
	byte[] wczytaneBajty;

	public void dekoduj(String plik) throws IOException {
		inicjalizujDekodowanie(plik);
		rozpocznijDekodowanie();
		zapiszDoPliku();
	}

	private void zapiszDoPliku() throws IOException {
		String nazwaPliku = "dek_out.txt";
		File newTextFile = new File(nazwaPliku);
		FileWriter fw = new FileWriter(newTextFile);
		String pom = wyjscieTekst.toString();
//		pom = pom.substring(BuferSlownikaRozmiar);
		fw.write(pom);
		fw.close();

	}

	private void rozpocznijDekodowanie() {

//		while (bufKodowaniaPtr < doZdekodowania.length()) {
//			String pierwszyArg = doZdekodowania.substring(bufKodowaniaPtr,
//					bufKodowaniaPtr + 1);
//			bufKodowaniaPtr++;
//			String drugiArg = doZdekodowania.substring(bufKodowaniaPtr,
//					bufKodowaniaPtr + 1);
//			bufKodowaniaPtr++;
		while (bufKodowaniaPtr < wczytaneBajty.length) {
			int pierwszyArg = wczytaneBajty[bufKodowaniaPtr];
			bufKodowaniaPtr++;
			char drugiArg = (char)wczytaneBajty[bufKodowaniaPtr];
			bufKodowaniaPtr++;
			if (pierwszyArg ==0) {
				bufSlownikowy += drugiArg;
				// sprwadzPrzepelnienieBufora

			} else {
				// drugiArgument mow ile znakow nalezy skopiowac poczawszy od
				// cofniecia sie w buferzeSlown o pierwszy argument
				int arg1 = Integer.valueOf(pierwszyArg);
				if(arg1<0){
					arg1 = 256+arg1;
				}
				int arg2 = (int)drugiArg;
				
				String znakiDoWyniku ="";
//				if(bufSlownikowy.length()<BuferSlownikaRozmiar){
//					for(int i=0; i< arg2; i++){
//						znakiDoWyniku += bufSlownikowy.substring(bufKodowaniaPtr, );
//					}
//				}else{
				System.out.print("       ["+arg1 +"; "+ arg2+"]");
				System.out.print(" "+(bufSlownikowy.length() - arg1) +"  "+(bufSlownikowy.length()- arg1 + arg2)  );
					znakiDoWyniku = bufSlownikowy.substring(
						bufSlownikowy.length() - arg1, bufSlownikowy.length()
								- arg1 + arg2);
			//	}
				

				bufSlownikowy += znakiDoWyniku;
				// bufKodowaniaPtr += znakiDoWyniku.length();
				// sprawdzPrzepelnienieBufora

			}
			sprwadzPrzepelnienieBufora();
		}
		wyjscieTekst.append(bufSlownikowy);

	}

	private void sprwadzPrzepelnienieBufora() {
		if (bufSlownikowy.length() > BuferSlownikaRozmiar) {
			int index = bufSlownikowy.length() - BuferSlownikaRozmiar;
			String doWyjscia = bufSlownikowy.substring(0, index);
			bufSlownikowy = bufSlownikowy.substring(index);
			// if ((doWyjscia.equals(" " )|| doWyjscia.charAt(0)==' ')
			// && (bufKodowaniaPtr <= BuferSlownikaRozmiar)) {
			// return;
			// } else {

			wyjscieTekst.append(doWyjscia);
			wyjscie += doWyjscia;
			// }
		}
	}

	private void inicjalizujDekodowanie(String plik) throws IOException {

		wczytajPlik(plik);

		wyjscieTekst = new StringBuilder();
	//	doZdekodowania = wejscieKody.toString();
		bufKodowaniaPtr = 0;
//		for (int i = 0; i < BuferSlownikaRozmiar; i++) {
//			bufSlownikowy += " ";
//		}

	}

	private void wczytajPlik(String plik) throws IOException {
		// File file = new File(plik);
		// BufferedReader br = new BufferedReader(new InputStreamReader(
		// new FileInputStream(file)));
		// StringBuffer content = new StringBuffer();
		// String line;
		//
		// while ((line = br.readLine()) != null) {
		// content.append(line);content.append('\n');
		// }
		// br.close();
		// wejscieKody = content.toString();
		System.out.println("\n -------------------------------");
		wczytaneBajty = czytanieBajtowZPliku(plik);
	}

	public static byte[] czytanieBajtowZPliku(String nazwaPliku) {
		File file = new File(nazwaPliku);

		ByteArrayOutputStream byteArrayOutputStream = null;
		InputStream inputStream = null;
		byte[] readedBytes = null;
		System.out.print(" \n\n  *****************");
		try {
			readedBytes = new byte[(int) file.length()];
			byteArrayOutputStream = new ByteArrayOutputStream();
			inputStream = new FileInputStream(file);
			int read = 0;
			while ((read = inputStream.read(readedBytes)) != -1) {
				byteArrayOutputStream.write(readedBytes, 0, read);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (byteArrayOutputStream != null)
					byteArrayOutputStream.close();
			} catch (IOException e) {
			}

			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
			}
		}

		return readedBytes;

	}
}
