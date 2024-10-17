// Importaciones necesarias
import java.util.ArrayList;
import java.util.List;
%%
%public
%class LanguageFlexer
%unicode
%integer
%{
    // Definir las constantes para los tipos de tokens
    public static final int RESERV_WORD = 1;
    public static final int IDENTIFIER = 2;
    public static final int NUMBER = 3;
    public static final int SYMBOL = 4;
    public static final int OTHER = 5;

    // Lista para almacenar los tokens encontrados
    private List<Token> tokens = new ArrayList<>();

    // Clase Token para almacenar información sobre cada token
    public class Token {
        public int type;
        public String lexeme;

        public Token(int type, String lexeme) {
            this.type = type;
            this.lexeme = lexeme;
        }
    }

    // Método para obtener la lista de tokens
    public List<Token> getTokens() {
        return tokens;
    }
%}

// Definiciones de patrones
RESERV_WORDS = "abstract"
               |"List"
               |"assert"
               |"boolean"
               |"break"
               |"byte"
               |"case"
               |"catch"
               |"char"
               |"class"
               |"continue"
               |"default"
               |"do"
               |"double"
               |"else"
               |"enum"
               |"extends"
               |"final"
               |"finally"
               |"float"
               |"for"
               |"goto"
               |"if"
               |"implements"
               |"import"
               |"instanceof"
               |"int"
               |"interface"
               |"long"
               |"native"
               |"new"
               |"package"
               |"private"
               |"protected"
               |"public"
               |"return"
               |"short"
               |"static"
               |"strictfp"
               |"super"
               |"switch"
               |"synchronized"
               |"this"
               |"throw"
               |"throws"
               |"transient"
               |"try"
               |"void"
               |"volatile"
               |"while"
               |"BufferedReader"
               |"FileReader"

IDENTIFIERS = [a-zA-Z_][a-zA-Z0-9_]*
NUMBERS = [0-9]+
SYMBOLS =
    "++" | "--" | ">>>" | "<<<" | "==" | "!=" | ">=" | "<=" |
    "&&" | "||" | "<<" | ">>" | "+=" | "-=" | "*=" | "/=" | "%=" |
    "&=" | "|=" | "^=" | "<<=" | ">>=" | ">>>=" | "::" | "..." |
    "+" | "-" | "*" | "/" | "%" | "=" | "!" | "~" | "&" | "|" |
    "^" | ">" | "<" | "?" | ":" | ";" | "," | "." | "(" | ")" |
    "{" | "}" | "[" | "]" | "@"

WHITESPACE = [ \t\r\n]+
ANYCHAR = .

%%

// Reglas de análisis léxico con acciones

{RESERV_WORDS} {
    tokens.add(new Token(RESERV_WORD, yytext()));
    return RESERV_WORD;
}

{IDENTIFIERS} {
    tokens.add(new Token(IDENTIFIER, yytext()));
    return IDENTIFIER;
}

{NUMBERS} {
    tokens.add(new Token(NUMBER, yytext()));
    return NUMBER;
}

{SYMBOLS} {
    tokens.add(new Token(SYMBOL, yytext()));
    return SYMBOL;
}

{WHITESPACE} {
    tokens.add(new Token(OTHER, yytext()));
    return OTHER;
}

{ANYCHAR} {
    tokens.add(new Token(OTHER, yytext()));
    return OTHER;
}

<<EOF>> {
    return YYEOF; // Señalar fin de archivo
}
