package lz77;

import java.io.IOException;

public class Klz77 {

	public static void main(String[] args) {
		String plikDoZakodowania = "test1.txt";
		String plikWyjsciowy = "test1_out.txt";
		KodowanieLZ77 lz77 = new KodowanieLZ77();
		DekodowanieLZ77 dekLZ77= new DekodowanieLZ77();
		
		try {
			lz77.zakoduj(plikDoZakodowania, plikWyjsciowy);
			dekLZ77.dekoduj(plikWyjsciowy);
		} catch (IOException e) {
			e.printStackTrace();
		}



//		System.out.println("Zdekodowano: "+
//				wyjscieTekst.toString());
	}

	
		
	}

	

	// 2: fill view from input
	// 3: while (view not empty) do
	// 4: begin
	// 5: find longest prefix p of view starting in coded part
	// 6: i := position of p in window
	// 7: j := length of p
	// 8: X := first char after p in view
	// 9: output(i,j,X)
	// 10: add j+1 chars
	// 11: end

		


