import java.util.HashMap;
import java.util.List;

public class synAnalyzer {
	private int currentToken;
	private HashMap tokenValues;
	private List<Integer> tokens;
	private List<String> lexemes;
	public synAnalyzer(List<String> lexemes, List<Integer> tokens) {
		this.tokens = tokens;
		this.lexemes = lexemes;
		this.tokenValues = Main.tokenValues;
		this.currentToken = 0;
	}
	public void nextToken() {
		/*
		 * Proceeds to the next token
		 */
		this.currentToken += 1;
		System.out.println("Next token: " + getCurrentTokenName() + " (" + getCurrentToken() + ")");
	}
	public Integer getCurrentToken() {
		/*
		 * Gets current token code
		 */
		return this.tokens.get(currentToken);
	}
	public String getCurrentTokenName() {
		/*
		 * Gets current token name
		 */
		return this.lexemes.get(currentToken);
	}
	public void program() {
		/*
		 * <Program> --> BEGIN { <stmt_list> } END 
		 */
		System.out.println("Entering <Program>");
		if(tokenValues.get("BEGIN").equals(getCurrentToken())) {
			nextToken();
			if(tokenValues.get("{").equals(getCurrentToken())) {
				nextToken();
				stmt_list();
			}
			else {
				System.out.println("Error: No { provided to organize statement list");
				System.exit(1);
			}
			if(tokenValues.get("}").equals(this.tokens.get(currentToken))) {
				nextToken();
				//Handle program's end
				if(tokenValues.get("END").equals(this.tokens.get(currentToken))) {
					System.out.println("Program analysis has been completed and found no syntax errors.");
				}
				else {
					System.out.println("Error: Program ends with invalid end token");
					System.exit(1);
				}	
			}
			else {
				System.out.println("Error: No } provided to organize statement list");
				System.exit(1);
			}
		}
		else {
			System.out.print("Error: Program begins with invalid start token.");
			System.exit(1);
		}
		
	}
	public void stmt() {
		/*
		 * <stmt> --> <when_stmt> | <declr_stmt> | <initialize_stmt> | <loop_stmt>
		 */
		System.out.println("Entering <stmt>");
		Integer token = getCurrentToken();
		if(tokenValues.get("when") == token) {
			nextToken();
			when();
		}
		else if(tokenValues.get("loop") == token) {
			nextToken();
			loop();
		}
		else if(tokenValues.get("num") == token) {
			nextToken();
			declr();
		}
		else {
			nextToken();
			init();
		}
	}
	public void stmt_list() {
		/*
		 * <stmt_list> --> <stmt> <stmt_list*>
		 * <stmt_list*> --> EMPTY | <stmt> <stmt_list*>
		 */
		System.out.println("Entering <stmt_list>");
		if(getCurrentToken() == tokenValues.get("when") || getCurrentToken() == tokenValues.get("num") || getCurrentToken() == tokenValues.get("loop") || getCurrentToken() == tokenValues.get("id")) {
			stmt();
			while(getCurrentToken() == tokenValues.get("when") || getCurrentToken() == tokenValues.get("num") || getCurrentToken() == tokenValues.get("loop") || getCurrentToken() == tokenValues.get("id")) {
				stmt();
			}
		}
	}
	public void when() {
		/*
		 * <if_stmt> --> when ( <bexpr> ) { <stmt_list)> } | when ( <bexpr> ) { <stmt_list> } elsewhen { <stmt_list> }
		 */
		System.out.println("Entering <if_stmt>");
		if(tokenValues.get("(") == getCurrentToken()) {
			nextToken();
			bexpr();
			if(tokenValues.get(")") == getCurrentToken()) {
				nextToken();
				if(tokenValues.get("{") == getCurrentToken()) {
					nextToken();
					stmt_list();
					if(tokenValues.get("}") == getCurrentToken()) {
						nextToken();
						if(tokenValues.get("elsewhen") == getCurrentToken()) {
							nextToken();
							if(tokenValues.get("{") == getCurrentToken()) {
								nextToken();
								stmt_list();
								if(tokenValues.get("}") == getCurrentToken()) {
									nextToken();
								}
								else {
									System.out.println("Error: Missing } character in elsewhen statement");
									System.exit(1);
								}
							}
							else {
								System.out.println("Error: Missing { character in elsewhen statement");
								System.exit(1);
							}
							stmt_list();
						}
					}
					else {
						System.out.println("Error: Missing } character in when statement");
						System.exit(1);
					}
				}
				else {
					System.out.println("Error: Missing { character in when statement");
					System.exit(1);
				}
			}
			else {
				System.out.println("Error: Missing ) character in when statement");
				System.exit(1);
			}
		}
		else {
			System.out.println("Error: Missing ( character in when statement");
			System.exit(1);
		}
	}
	public void loop() {
		/*
		 * <loop_stmt> --> loop ( <expr> ) { <stmt_list> }
		 */
		System.out.println("Entering <loop_stmt>");
		if(tokenValues.get("(") == getCurrentToken()) {
			nextToken();
			bexpr();
			if(tokenValues.get(")") == getCurrentToken()) {
				nextToken();
				if(tokenValues.get("{") == getCurrentToken()) {
					nextToken();
					stmt_list();
					if(tokenValues.get("}") == getCurrentToken()) {
						nextToken();
					}
					else {
						System.out.println("Error: Missing } character in loop statement");
						System.exit(1);
					}
				}
				else {
					System.out.println("Error: Missing { character in loop statement");
					System.exit(1);
				}
			}
			else {
				System.out.println("Error: Missing ) character in loop statement");
				System.exit(1);
			}
		}
		else {
			System.out.println("Error: Missing ( character in loop statement");
			System.exit(1);
		}
	}
	public void declr() {
		/*
		 * <declr> --> num id
		 */
		System.out.println("Entering <declr>");
		if(tokenValues.get("id") == getCurrentToken()) {
			nextToken();
		}
		else {
			System.out.println("Error: Missing an identifier in declaration statement.");
			System.exit(1);
		}
	}
	public void init() {
		/*
		 * <assign> --> id = <expr>
		 */
		System.out.println("Entering <init>");
		if(tokenValues.get("=") == getCurrentToken()) {
			nextToken();
			expr();
		}
		else {
			System.out.println("Error: Missing an = character in initialization statement.");
			System.exit(1);
		}
	}
	public void expr() {
		/*
		 *<expr> -> <mul_op> <add_op*>
		 * <expr*> -> EMPTY | + <mul_op> <add_op*> | <mul_op> 
		 */
		System.out.println("Entering <expr>");
		mul();
		while(tokenValues.get("+") == getCurrentToken()) {
			nextToken();
			mul();	
		}
	}
	public void mul() {
		/*
		 * <mul_op> -> <sub_op> <mul_op*>
		 * <mul_op*> -> EMPTY | * <sub_op> <mul_op*> | <sub_op>
		 */
		System.out.println("Entering <mul_op>");
		sub();
		while(tokenValues.get("*") == getCurrentToken()) {
			nextToken();
			sub();	
		}
	}
	public void div() {
		/*
		 * <div_op> -> <mod_op> <div_op*>
		 * <div_op*> -> EMPTY | / <mod_op> <div_op*> | <mod_op>
		 */
		System.out.println("Entering <div_op>");
		mod();
		while(tokenValues.get("/") == getCurrentToken()) {
			nextToken();
			mod();	
		}
	}
	public void sub() {
		/*
		 * <sub_op> -> <div_op> <sub_op*>
		 * <sub_op*> -> EMPTY | - <div_op> <sub_op*> | <div_op>
		 */
		System.out.println("Entering <sub_op>");
		div();
		while(tokenValues.get("-") == getCurrentToken()) {
			nextToken();
			div();
		}
	}
	public void mod() {
		/*
		 * <mod_op> -> <exp_op> <mod_op*>
		 * <mod_op*> -> EMPTY | % <exp_op> <mod_op> | <exp_op>
		 */
		System.out.println("Entering <mod_op>");
		exp();
		while(tokenValues.get("%") == getCurrentToken()) {
			nextToken();
			exp();
		}
	}
	public void exp() {
		/*
		 * <exp_op> -> <term> <exp_op*>
		 * <exp_op> -> EMPTY | E <term> <exp_op*> | <term>
		 */
		System.out.println("Entering <exp_op>");
		term();
		while(tokenValues.get("E") == getCurrentToken()) {
			nextToken();
			term();
		}
	}
	public void term() {
		/*
		 * <term> -> id | integer | ( <expr> )
		 */
		System.out.println("Entering <term>");
		if(tokenValues.get("int_one_byte") == getCurrentToken() || tokenValues.get("int_two_byte") == getCurrentToken() || tokenValues.get("int_four_byte") == getCurrentToken() || tokenValues.get("int_eight_byte") == getCurrentToken() || tokenValues.get("id") == getCurrentToken()) {
			nextToken();
		}
		else if(tokenValues.get("(") == getCurrentToken()) {
			nextToken();
			expr();
			if(tokenValues.get(")") == getCurrentToken()) {
				nextToken();
			}
			else {
				System.out.println("Error: Missing closing ) in expression");
				System.exit(1);
			}
		}
		else {
			System.out.println("Error: Invalid token found in expression statement");
			System.exit(1);
		}
	}
	public void bexpr() {
		/*
		 * <bexpr> --> <bor> <bexpr*>
		 * <bexpr*> --> EMPTY | & <bexpr*> | <bor>
		 */
		System.out.println("Entering <bexpr>");
		bor();
		while(tokenValues.get("&") == getCurrentToken()) {
			nextToken();
			bor();
		}
	}
	public void bor() {
		/*
		 *<bor> --> <beq> <bor*>
		 *<bor*> --> EMPTY | || <bor*> | <beq>
		 */
		System.out.println("Entering <bor>");
		beq();
		while(tokenValues.get("|") == getCurrentToken()) {
			nextToken();
			beq();
		}
	}
	public void beq() {
		/*
		 * <beq> --> <bcomp> <beq*>
		 * <beq*> --> EMPTY | == <beq*> | != <beq*> | <bcomp>
		 */
		System.out.println("Entering <beq>");
		bcomp();
		while(tokenValues.get("==") == getCurrentToken() || tokenValues.get("!=") == getCurrentToken()) {
			nextToken();
			bcomp();
		}
	}
	public void bcomp() {
		/*
		 * <bcomp> --> <bexpr> <bcomp*>
		 * <bcomp*> --> EMPTY | > <bcomp*> | < <bcomp*> | >= <bcomp*> | <= <bcomp*> | <bexpr>
		 */
		System.out.println("Entering <bcomp>");
		bnot();
		while(tokenValues.get(">") == getCurrentToken() || tokenValues.get("<") == getCurrentToken() || tokenValues.get(">=") == getCurrentToken() || tokenValues.get("<=") == getCurrentToken()) {
			nextToken();
			bnot();
		}
	}
	public void bnot() {
		/*
		 *  <bnot> --> !<expr> | <expr>
		 */
		System.out.println("Entering <bnot>");
		if(tokenValues.get("!") == getCurrentToken()) {
			nextToken();
			expr();
		}
		else {
			expr();
		}
	}
}
