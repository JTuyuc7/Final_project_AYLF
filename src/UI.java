import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class UI {

    private JFrame frame;
    private JTextPane textPane;
    private JButton openButton;
    private JButton saveButton;
    private StyledDocument document;
    private Style keywordStyle, symbolStyle, numberStyle, identifierStyle, otherStyle;

    public UI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Resaltador de Sintaxis");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el StyledDocument y el JTextPane
        StyleContext styleContext = new StyleContext();
        document = new DefaultStyledDocument(styleContext);
        textPane = new JTextPane(document);

        // Configurar el JScrollPane y agregar el JTextPane
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Definir estilos
        defineStyles(styleContext);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        openButton = new JButton("Seleccionar archivo");
        saveButton = new JButton("Guardar como...");
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        // Acción del botón "Seleccionar archivo"
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFileToAnalyze();
            }
        });

        // Acción del botón "Guardar como..."
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveHighlightedText();
            }
        });

        frame.setVisible(true);
    }

    private void defineStyles(StyleContext styleContext) {
        Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);

        //* Palabras reservadas
        keywordStyle = styleContext.addStyle("KeywordStyle", null);
        StyleConstants.setForeground(keywordStyle, Color.BLUE);

        //* Simbolos
        symbolStyle = styleContext.addStyle("SymbolStyle", null);
        StyleConstants.setForeground(symbolStyle, Color.RED);

        //* Numeros
        numberStyle = styleContext.addStyle("NumberStyle", null);
        StyleConstants.setForeground(numberStyle, new Color(128, 0, 128)); // Morado

        //* Identificadores
        identifierStyle = styleContext.addStyle("IdentifierStyle", null);
        StyleConstants.setForeground(identifierStyle, Color.BLACK);
        // *Agregar background if needed
//        StyleConstants.setBackground(identifierStyle, Color.PINK);

        // Cualquier otra palabra
        otherStyle = styleContext.addStyle("OtherStyle", null);
        StyleConstants.setForeground(otherStyle, Color.yellow);
    }

    private void selectFileToAnalyze() {
        JFileChooser fileChooser = new JFileChooser();
        int opt = fileChooser.showOpenDialog(frame);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textPane.setText(""); // Limpiar el texto existente
            highlightSyntax(selectedFile);
        }
    }

    private void highlightSyntax(File file) {
        try {
            // Crear el analizador léxico
            Reader reader = new BufferedReader(new FileReader(file));
            LanguageFlexer lexer = new LanguageFlexer(reader);

            // Procesar todo el archivo
            while (lexer.yylex() != LanguageFlexer.YYEOF) {
                // El analizador ya está llenando la lista de tokens
            }

            // Obtener la lista de tokens
            List<LanguageFlexer.Token> tokens = lexer.getTokens();

            // Insertar los tokens en el documento con el estilo apropiado
            for (LanguageFlexer.Token token : tokens) {
                String lexeme = token.lexeme;
                Style style;

                switch (token.type) {
                    case LanguageFlexer.RESERV_WORD:
                        style = keywordStyle;
                        break;
                    case LanguageFlexer.IDENTIFIER:
                        style = identifierStyle;
                        break;
                    case LanguageFlexer.NUMBER:
                        style = numberStyle;
                        break;
                    case LanguageFlexer.SYMBOL:
                        style = symbolStyle;
                        break;
                    default:
                        style = otherStyle;
                        break;
                }

                document.insertString(document.getLength(), lexeme, style);
            }

            // Cerrar el lector
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveHighlightedText() {
        JFileChooser fileChooser = new JFileChooser();
        int opt = fileChooser.showSaveDialog(frame);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textPane.getText());
                JOptionPane.showMessageDialog(frame, "Archivo guardado exitosamente.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UI());
    }
}
