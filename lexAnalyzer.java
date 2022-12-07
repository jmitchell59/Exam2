import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
	public static HashMap<String, Integer> tokenValues = new HashMap<>();
	
	public static void main(String[] args) {
		//Load our token values
		loadTokenValues();
		//Get Code File
		Path path = Path.of("test.txt");
		//Path path = Path.of("test2.txt");
		//Path path = Path.of("lexerr.txt");
		//Path path = Path.of("synerr.txt");
		
		//Convert into Lists containing number codes and another the valid lexemes
		List<String> lexemes = getLexemes(path);
		List<Integer> tokens = Lexer.parseTokens(lexemes);
		
		//Check lists for valid syntax
		synAnalyzer syn = new synAnalyzer(lexemes, tokens);
		syn.program();
	}
	
	public static void loadTokenValues() {
		//Loads all the lexemes into a hashmap
		tokenValues.put("BEGIN", 0);
		tokenValues.put("END", 1);
		tokenValues.put("when", 2);
		tokenValues.put("elsewhen", 22);
		tokenValues.put("loop", 3);
		tokenValues.put("+", 4);
		tokenValues.put("-", 5);
		tokenValues.put("*", 6);
		tokenValues.put("/", 7);
		tokenValues.put("=", 8);
		tokenValues.put(">", 9);
		tokenValues.put("<", 10);
		tokenValues.put(">=", 11);
		tokenValues.put("<=", 12);
		tokenValues.put("==", 13);
		tokenValues.put("!=", 14);
		tokenValues.put("!", 15);
		tokenValues.put("num", 16);
		tokenValues.put("{", 20);
		tokenValues.put("}", 21);
		tokenValues.put("(", 23);
		tokenValues.put(")", 24);
		tokenValues.put("int_one_byte", 25);
		tokenValues.put("id", 26);
		tokenValues.put("int_two_byte", 27);
		tokenValues.put("int_four_byte", 28);
		tokenValues.put("int_eight_byte", 29);
		tokenValues.put("&", 30);
		tokenValues.put("|", 31);
		tokenValues.put("E", 32);
		tokenValues.put("%", 32);
		
	}
	public static List<String> getLexemes(Path pathToFile) {
		/*
		 * Reads each line and separates the lexemes by spaces, storing them in a list.
		*/
		List<String> lexemes = new ArrayList();
		try {
			List<String> lines = Files.readAllLines(pathToFile);
			for(String line: lines) {
				String[] lineSplit = line.split(" ");
				for(String lexeme: lineSplit) {
					if(lexeme != "") {
						lexemes.add(lexeme);
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("Error: Unable to find file with path: " + pathToFile.toString());
			System.exit(1);
		}
		return lexemes;
	}
	class Lexer {
		public static List<Integer> parseTokens(List<String> lexemes) {
			/*
			 * Checks if the lexeme list contains valid tokens.
			*/
			assert lexemes.size() != 0;
			List<Integer> tokens = new ArrayList<Integer>();
			for(String lexeme: lexemes) {
				//Check if the lexeme is in the HashMap
				System.out.println("Validating lexeme: " + lexeme);
				if(tokenValues.get(lexeme) != null) {
					tokens.add(tokenValues.get(lexeme));
				}
				//Check if the lexeme is an integer literal or identifier
				else {
					if(Character.isDigit(lexeme.charAt(0))){
						//Check if byte identifier is valid and if a value is provided
						char posZero = lexeme.charAt(0);
						if(lexeme.length() <= 2) {
							System.out.println("Error: Unable to identify type of integer due to invalid structure being too short.");
							System.exit(0);
						}
						if(lexeme.charAt(1) == '_' && lexeme.length() <= 2) {
							System.out.println("Error: Unable to identify type of integer due to invalid structure not clarifying valid byte size.");
							System.exit(0);
						}
						switch(posZero) {
							//Check if number following type identifier is valid
							case '1':
								for(int i = 2; i < lexeme.length(); i++) {
									if(!Character.isDigit(lexeme.charAt(i))) {
										System.out.println("Error: Letter character was provided where an integer character was expected");
										System.exit(1);
									}
								}
								tokens.add(tokenValues.get("int_one_byte"));
								break;
							case '2':
								for(int i = 2; i < lexeme.length(); i++) {
									if(!Character.isDigit(lexeme.charAt(i))) {
										System.out.println("Error: Letter character was provided where an integer character was expected");
										System.exit(1);
									}
								}
								tokens.add(tokenValues.get("int_two_byte"));
								break;
							case '4':
								for(int i = 2; i < lexeme.length(); i++) {
									if(!Character.isDigit(lexeme.charAt(i))) {
										System.out.println("Error: Letter character was provided where an integer character was expected");
										System.exit(1);
									}
								}
								tokens.add(tokenValues.get("int_four_byte"));
								break;
							case '8':
								for(int i = 2; i < lexeme.length(); i++) {
									if(!Character.isDigit(lexeme.charAt(i))) {
										System.out.println("Error: Letter character was provided where an integer character was expected");
										System.exit(1);
									}
								}
								tokens.add(tokenValues.get("int_eight_byte"));
								break;
							default:
								System.out.println("Error: Invalid byte size provided for integer.");
								System.exit(1);
								break;
						}
					}
					else if(Character.isLetter(lexeme.charAt(0)) || lexeme.charAt(0) == '_') {
						//Check if identifier length is correct
						if(lexeme.length() < 6 || lexeme.length() > 8) {
							System.out.println("Error: Invalid length provided for identifier.");
							System.exit(0);
						}
						//Check if all characters are letters
						for(int i = 1; i < lexeme.length(); i++) {
							if(!(Character.isLetter(lexeme.charAt(i)) || lexeme.charAt(i) == '_')) {
								System.out.println("Error: A non-letter was provided where a numerical value was expected.");
								System.exit(1);
							}
						}
						tokens.add(tokenValues.get("id"));
					}
					else {
						System.out.println("Error: Unable to identify token based on starting character.");
						System.exit(1);
					}
				}
			}
			return tokens;
		}
	}
}
