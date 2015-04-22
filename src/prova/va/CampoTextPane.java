package prova.va;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

/**
 * <p>Title: CampoTextPane</p>
 * <p>Description: 
 *  Componente que apresenta uma caixa de texto juntamente com botões para aplicação de estilos no texto digitado.
 * 	São três (3) botões que disponibilizam o estilo negrito, itálico e sublinhado.
 * 	Para que os estilos aplicados estejam disponíveis para a posteridade é necessário que além do texto,
 * todo os estilos aplicados sejam persistidos. Sendo assim, ao utilizar esse componente é necessário ter em
 * mente que o campo que armazenará o conteúdo texto deverá suportar também a string que representa os estilos
 * aplicados.
 *  Sugiro fortemente a utilização de campos de armazenamento de tamanhos consideráveis como "LONGTEXT" do MySQL.
 * 	Esse componente também disponibiliza a obtenção do conteúdo informado no campo texto juntamente com todo
 * o estilo aplicado para fins de impressão em relatórios desenvolvidos em Jasper.</p>
 * @author Hudson de Paula Romualdo
 */
public class CampoTextPane extends JPanel{

	public JTextPane txtObservacoes = null;
	
	private StyledDocument doc;
	
	private Icon negritoIcone;
	private Icon italicoIcone;
	private Icon sublinhadoIcone;
	
	private String pdfFonteNome = "Helvetica";
	
	private final String NEGRITO    = "b";
	private final String ITALICO    = "i";
	private final String SUBLINHADO = "u";
	public String ultima_palavra="",palavra="",saida="";
	public final char CARACTER_SEPARADOR = '§'; //Caracter que separa o texto do vetor de estilos
	public final Lexico lexico = new Lexico();
	
	/**
	 * Construtor padrão
	 */
	public CampoTextPane() {
		super();
		this.exec();		
	}
	
	/**
	 * Construtor sobrescrito para inserção de ícones nos botões
	 * @param negritoIcone
	 * @param italicoIcone
	 * @param sublinhadoIcone
	 */
	public CampoTextPane(Icon negritoIcone, Icon italicoIcone, Icon sublinhadoIcone){
		super();
		
		this.negritoIcone = negritoIcone;
		this.italicoIcone = italicoIcone;
		this.sublinhadoIcone = sublinhadoIcone;
		
		this.exec();
	}
	
	/**
	 * Método que constroi o componente
	 */
	private void exec(){
		setLayout(new BorderLayout()); 

		final JPanel pnlPrincipal = new JPanel();
		pnlPrincipal.setLayout(new BorderLayout());
		add(pnlPrincipal, BorderLayout.CENTER);

		final JScrollPane scrollObservacoes = new JScrollPane();
		pnlPrincipal.add(scrollObservacoes, BorderLayout.CENTER);		
		this.txtObservacoes = new JTextPane();		
	    
		this.txtObservacoes.addKeyListener(new KeyAdapter() {
			public void keyTyped(final KeyEvent e) {				
				
				//Impede que o caracter especial que separa o texto do vetor de estilos seja inserido
				if(e.getKeyChar() == CARACTER_SEPARADOR)
				{
					//e.setKeyChar('\u0000');
					
				}
				if (e.getKeyChar()==' ')
				{
					//e.setKeyChar('n');
					//lexico.setInput(palavra);
				}

			}
				//aqui podemos pegar cada caracter digitado			
		});
		
		scrollObservacoes.setViewportView(this.txtObservacoes);
       
		final JPanel pnlControles = new JPanel();
		pnlControles.setPreferredSize(new Dimension(120, 0));
		pnlControles.setMinimumSize(new Dimension(0, 0));
		pnlControles.setLayout(null);
		add(pnlControles, BorderLayout.EAST);

        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME, "");

		final JButton btnNegrito = new JButton(action);
		btnNegrito.setToolTipText("Negrito");
		btnNegrito.setBounds(0, 0, 100, 20);
		btnNegrito.setText("Negrito");
		
		if(this.negritoIcone != null)
			btnNegrito.setIcon(this.negritoIcone);
		
		pnlControles.add(btnNegrito);

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME, "");
        
		final JButton btnItalico = new JButton(action);
		btnItalico.setToolTipText("Itálico");
		btnItalico.setBounds(0, 20, 100, 20);
		btnItalico.setText("Italico");
		pnlControles.add(btnItalico);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME, "");
        
		final JButton btnSublinhado = new JButton(action);
		btnSublinhado.setToolTipText("Sublinhado");
		btnSublinhado.setBounds(0, 40, 100, 20);
		btnSublinhado.setText("Sublinhado");
		pnlControles.add(btnSublinhado);
        
		
		final JButton btnlexico = new JButton();
		btnlexico.setBounds(0, 60, 100, 20);
		btnlexico.setText("Lexico");
		btnlexico.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				        saida="";
				        palavra=txtObservacoes.getText();						
						lexico.setInput( palavra);
						try
						{
						    Token t = null;
						    while ( (t = lexico.nextToken()) != null )
						    {
						    	
						    	saida=saida+"\n"+t.toString();
						    }
						}
						catch ( LexicalError er )
						{						    
						    saida=saida+" (err) "+er.getMessage() +" (pos) "+  er.getPosition();
						}

						txtObservacoes.setText(saida);
						saida="";
					    ultima_palavra="";
					    palavra="";
				}			  
			});
		pnlControles.add(btnlexico);

		final JButton btnsintatico = new JButton();
		btnsintatico.setBounds(0, 80, 100, 20);
		btnsintatico.setText("Sintatico");
		btnsintatico.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
						saida="";
				        palavra=txtObservacoes.getText();
						lexico.setInput( palavra);

						Sintatico sintatico = new Sintatico();
						Semantico semantico = new Semantico();
						try
						{
							sintatico.parse(lexico,semantico);							
							JOptionPane.showMessageDialog(null, "Codigo Sintaticamente Correto!");
							//JOptionPane.showInputDialog(palavra);
						}
						catch ( LexicalError er )
						{						    
						    saida=saida+" (err_lexico) "+er.getMessage() +" (pos) "+  er.getPosition();
						}
						catch ( SyntaticError er )
						{
						    saida=saida+" (err_sintatico) "+er.getMessage() +" (pos) "+  er.getPosition();
						}
						catch ( SemanticError er )
						{
						    saida=saida+" (err_semantico) "+er.getMessage() +" (pos) "+  er.getPosition();
						}
						if (!saida.equals(""))
							txtObservacoes.setText(saida);
						saida="";
					    ultima_palavra="";
					    palavra="";
				}			  
			});
		pnlControles.add(btnsintatico);
		

		final JButton btnsemantico = new JButton();
		btnsemantico.setBounds(0, 100, 100, 20);
		btnsemantico.setText("Semantico");
		btnsemantico.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			        saida="";
			        int flag=0;
			        palavra=txtObservacoes.getText();											
					Semantico semantico = new Semantico();
					StringTokenizer st = new StringTokenizer(palavra,"\n");
					while ( st.hasMoreTokens() )
					{
					  String linha = (String)st.nextToken();//pega a linha
					  StringTokenizer st1 = new StringTokenizer(linha," ");
					  //JOptionPane.showMessageDialog(null, "Linha "+linha);
					  while ( st1.hasMoreTokens() )
					  {
						  String pal = (String)st1.nextToken();//cada palavra da linha
						  lexico.setInput(pal);
						  //JOptionPane.showMessageDialog(null,"Palavrra "+pal);
						 try
						 {
						    Token t = null;
						    while ( (t = lexico.nextToken()) != null )
						    {
						    	String tmp=t.getLexeme();
						    	if (tmp.equals("recebe")) //trrata o recebe
						    		flag=1;
						    	else if (tmp.equals("mostre")) //trrata o mostre
						    		flag=2;
						    	else if (tmp.equals("numeros")) //trrata o numeroe
						    		flag=3;
						    	else if (tmp.equals("letras")) //trrata o string
						    		flag=4;
						    	else if (flag==1)//tratar recebe
						    	{
						    		t = lexico.nextToken();
						    		saida=saida+semantico.converttoJava(1002, t.getLexeme());//criado para tratar o recebe
						    		t = lexico.nextToken();
						    		flag=0;
						    	}else if (flag==2)
						    	{
						    		t = lexico.nextToken();
						    		saida=saida+semantico.converttoJava(1003, t.getLexeme());//criado para tratar o recebe
						    		flag=0;
						    		
						    	}else if (flag==3)
						    	{
						    		//t = lexico.nextToken();						    		
						    		saida=saida+semantico.converttoJava(1004, t.getLexeme());//criado para tratar o recebe
						    		flag=0;
						    		
						    	}else if (flag==4)
						    	{
						    		//t = lexico.nextToken();
						    		saida=saida+semantico.converttoJava(1005, t.getLexeme());//criado para tratar o recebe
						    		flag=0;
						    		
						    	}						    	
						    	else
						    	     saida=saida+semantico.converttoJava(t.getId(),tmp);
						    }		
						    saida=saida+" ";
						 }
						 catch ( LexicalError er )
						 {						    
						    saida=saida+" (err) "+er.getMessage() +" (pos) "+  er.getPosition();
						 }
						  
					  }
					  saida=saida+"\n\t";

					}
					saida=saida+"\n}\n";
					txtObservacoes.setText(saida);
					saida="";
				    ultima_palavra="";
				    palavra="";
				}			  
			});
		pnlControles.add(btnsemantico);
		

		final JButton btnsalvar = new JButton();
		btnsalvar.setBounds(0, 120, 100, 20);
		btnsalvar.setText("Salvar");
		btnsalvar.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				    salvar("Principal.java",txtObservacoes.getText() ,false);
				}			  
			});
		pnlControles.add(btnsalvar);

		final JButton btncompilar = new JButton();
		btncompilar.setBounds(0, 140, 100, 20);
		btncompilar.setText("Compilar");
		btncompilar.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  try {
					File arq=new File("Principal.class");
					if (arq.exists())
					{
						arq.delete();
					}
					arq=new File("Principal.java");
					if (arq.exists())
					{
						Process p = Runtime.getRuntime().exec("C:/Program Files/Java/jdk1.6.0_20/bin/javac Principal.java");
							
						try {
							p.waitFor();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						arq.delete();
						arq=new File("Principal.class");
						if (arq.exists())
						{
							JOptionPane.showMessageDialog(null,"Arquivo Compilado!");
						} else JOptionPane.showMessageDialog(null,"Arquivo não foi Compilado!");
							
						
					} else JOptionPane.showMessageDialog(null,"Arquivo não foi criado!");
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
				  //OutputStream os = p.getOutputStream();  
				}			  
			});
		pnlControles.add(btncompilar);
		
		final JButton btnrodar = new JButton(action);
		btnrodar.setBounds(0, 160, 100, 20);
		btnrodar.setText("Rodar");
		btnrodar.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  try {
						File arq=new File("Principal.class");
					  if (arq.exists())
						{
							  Process p = Runtime.getRuntime().exec("C:/Program Files/Java/jdk1.6.0_20/bin/java Principal");
						} else JOptionPane.showMessageDialog(null,"Arquivo não foi Compilado!");

					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  
				  //OutputStream os = p.getOutputStream();  
				}			  
			});
		pnlControles.add(btnrodar);
//		AttributedStringTest test = new AttributedStringTest(this.txtObservacoes);
		
/*		
		this.doc = this.txtObservacoes.getStyledDocument();
		Style style = doc.addStyle("default", null);
		StyleConstants.setFontSize(style, 30);

		style = doc.addStyle(this.NEGRITO, null);
        StyleConstants.setBold(style, true);
        style = doc.addStyle(this.ITALICO, null);
        StyleConstants.setItalic(style, true);
        style = doc.addStyle(this.SUBLINHADO, null);
        StyleConstants.setUnderline(style, true);
*/
	}

	public static void salvar(String arquivo, String conteudo, boolean adicionar)
	{
			FileWriter fw;
			try {
				fw = new FileWriter(arquivo, adicionar);
				fw.write(conteudo);
				fw.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}	
	
}
