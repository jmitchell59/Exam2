import java.io.File;
import java.io.*;

public class synAnalyzer extends lexAnalyzer {

    public static void main(String[] args) {
        try {
            in_fp = new File("test4.in");
            reader = new FileReader(in_fp);
            program();
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void program() {
        System.out.println("Enter <program>");
        // Uses the first character
        getChar();
        // Reading the next character
        lex();
        if (nextToken == BEGIN) {
            lex();
            stmtlist();
        } else {
            error();
        }
        // uses lex();
        if (nextToken == NEW_LINE) {
            lex();
        }
        if (nextToken == END) {
            System.out.println("Program Had Successfully Ended");
        } else {
            error();
        }
        System.out.println("Exit <program>");
    }

    // statement list
    static void stmtlist() {
        System.out.println("Enter <stmtlist>");
        stmt();
        if (nextToken == SEMI_COLON) {
            lex();
            stmtlist();
        }
        System.out.println("Exit <stmtlist>");
    }

    // Enter statement
    static void stmt() {
        System.out.println("Enter <stmt>");
        switch (nextToken) {
            case NEW_LINE:
                lex();
                stmt();
                break;
            case IF_KEY:
                if_stmt();
                break;
            case WHILE_KEY:
                while_l();
                break;
            case INT_TYPE:
                declr();
                break;
            case FOR_KEY:
                for_l();
                break;
            default:
                error();
                break;
        }
        System.out.println("Exit <stmt>");
    }

    // if statement
    static void if_stmt() {
        System.out.println("Enter <if_stmt>");
        if (nextToken != IF_KEY) {
            error();
        } else {
            lex();
            if (nextToken != PARENTHESIS_LEFT) {
                error();
            } else {
                lex();
                bool_expr();
                if (nextToken != PARENTHESIS_RIGHT) {
                    error();
                } else {
                    lex();
                    stmt();
                }
            }
        }
        System.out.println("Exit <if_stmt>");
    }

    // while loop
    static void while_l() {
        System.out.println("Enter <while_l>");
        if (nextToken != WHILE_KEY) {
            error();
        } else {
            lex();
            if (nextToken != PARENTHESIS_LEFT) {
                error();
            } else {
                lex();
                bool_expr();
                if (nextToken != PARENTHESIS_RIGHT) {
                    error();
                } else {
                    lex();
                    stmt();
                }
            }
        }
        System.out.println("Exit <while_l>");
    }

    // for loop
    static void for_l() {
        System.out.println("Enter <for_l>");
        if (nextToken != FOR_KEY) {
            error();
        } else {
            lex();
            if (nextToken != PARENTHESIS_LEFT) {
                error();
            } else {
                lex();
                if (nextToken != INT_TYPE) {
                    error();
                } else {
                    lex();
                    if (nextToken != IDEN) {
                        error();
                    } else {
                        lex();
                        if (nextToken != UNTIL) {
                            error();
                        } else {
                            lex();
                            if (nextToken != INT_LIT) {
                                error();
                            } else {
                                lex();
                                if (nextToken != PARENTHESIS_RIGHT) {
                                    error();
                                } else {
                                    lex();
                                    stmt();
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Exit <for_l>");
    }

    // Enter a declaration
    static void declr() {
        System.out.println("Enter <declr>");
        if (nextToken != INT_TYPE) {
            error();
        } else {
            lex();
            if (nextToken != IDEN) {
                error();
            } else {
                lex();
                if (nextToken == ASSIGNMENT_OP) {
                    lex();
                    expr();
                }
            }
        }
        System.out.println("Exit <declr>");
    }

    // Enter an expression
    static void expr() {
        System.out.println("Enter <expr>");
        term();
        while (nextToken == MULTIPLY_OP || nextToken == ADDING_OP) {
            lex();
            term();
        }
        System.out.println("Exit <expr>");
    }

    // Enter a term
    static void term() {
        System.out.println("Enter <term>");
        factor();
        while (nextToken == DIVIDE_OP || nextToken == SUBTRACT_OP || nextToken == MODULE_OP) {
            lex();
            factor();
        }
        System.out.println("Exit <term>");
    }

    // Enter a factor
    static void factor() {
        System.out.println("Enter <factor>");
        if (nextToken == IDEN || nextToken == INT_LIT) {
            lex();
        } else {
            if (nextToken == PARENTHESIS_LEFT) {
                lex();
                expr();
                if (nextToken == PARENTHESIS_RIGHT)
                    lex();
                else
                    error();
            } else {
                error();
            }
        }
        System.out.println("Exit <factor>");
    }

    // Boolean Expression
    static void bool_expr() {
        System.out.println("Enter <bool_expr>");
        brel();
        while (nextToken == NOT_EQUAL || nextToken == EQUAL) {
            lex();
            brel();
        }
        System.out.println("Exit <bool_expr>");
    }

    // <brel> expression
    static void brel() {
        System.out.println("Enter <brel>");
        bexpr();
        while (nextToken == GREATER_EQUAL || nextToken == LESS_EQUAL || nextToken == LESS_THAN
                || nextToken == GREATER_THAN) {
            lex();
            bexpr();
        }
        System.out.println("Exit <brel>");
    }

    // <bexpr>
    static void bexpr() {
        System.out.println("Enter <bexpr>");
        bterm();
        while (nextToken == MULTIPLY_OP || nextToken == ADDING_OP) {
            lex();
            bterm();
        }
        System.out.println("Exit <bexpr>");
    }

    // <bterm>
    static void bterm() {
        System.out.println("Enter <bterm>");
        bfactor();
        while (nextToken == DIVIDE_OP || nextToken == SUBTRACT_OP || nextToken == MODULE_OP) {
            lex();
            bfactor();
        }
        System.out.println("Exit <bterm>");
    }

    // <bfactor>
    static void bfactor() {
        System.out.println("Enter <bfactor>");
        if (nextToken == IDEN || nextToken == INT_LIT) {
            lex();
        } else {
            if (nextToken == PARENTHESIS_LEFT) {
                lex();
                bool_expr();
                if (nextToken == PARENTHESIS_RIGHT)
                    lex();
                else
                    error();
            } else {
                error();
            }
        }
        System.out.println("Exit <bfactor>");
    }

    static void error() {
        System.out.println("Syntax Error Detected");
    }

}