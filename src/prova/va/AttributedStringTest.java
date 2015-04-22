package prova.va;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

public class AttributedStringTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new AttributedStringTest();
	}

	public JTextPane txtObservacoes = null;
	private Icon negritoIcone;

	private JMenuItem itemtam;

	public String ultima_palavra = "", palavra = "", saida = "";
	public String txtComplilado = "";
	public final char CARACTER_SEPARADOR = '§'; // Caracter que separa o texto
												// do vetor de estilos
	public final Lexico lexico = new Lexico();
	protected boolean sintaticamente = false;
	protected boolean lexicamente = false;
	protected boolean semanticamente = false;
	protected boolean salvo = false;
	protected boolean compilado;

	AttributedStringTest() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initMenuBar();
		initTextPane();
		setSize(800, 600);
		setLocationRelativeTo(null);

		setVisible(true);
	}

	private void initMenuBar() {
		JMenuBar mbar = new JMenuBar();

		JMenu menu = new JMenu("Formatacao");
		JMenuItem item = new JMenuItem("Color");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(
						AttributedStringTest.this, "Color Chooser", Color.cyan);
				if (color != null) {
					MutableAttributeSet attr = new SimpleAttributeSet();
					StyleConstants.setForeground(attr, color);
					txtObservacoes.setCharacterAttributes(attr, false);
				}
			}
		});
		menu.add(item);

		JMenu font = new JMenu("Font");
		font.add(item = new JMenuItem("12"));
		item.addActionListener(new StyledEditorKit.FontSizeAction(
				"font-size-12", 12));
		font.add(this.itemtam = new JMenuItem("24"));
		this.itemtam.addActionListener(new StyledEditorKit.FontSizeAction(
				"font-size-24", 24));
		font.add(item = new JMenuItem("36"));
		item.addActionListener(new StyledEditorKit.FontSizeAction(
				"font-size-36", 36));
		font.addSeparator();
		font.add(item = new JMenuItem("Serif"));
		item.setFont(new Font("Serif", Font.PLAIN, 12));
		item.addActionListener(new StyledEditorKit.FontFamilyAction(
				"font-family-Serif", "Serif"));
		font.add(item = new JMenuItem("SansSerif"));
		item.setFont(new Font("SansSerif", Font.PLAIN, 12));
		item.addActionListener(new StyledEditorKit.FontFamilyAction(
				"font-family-SansSerif", "SansSerif"));
		font.add(item = new JMenuItem("Monospaced"));
		item.setFont(new Font("Monospaced", Font.PLAIN, 12));
		item.addActionListener(new StyledEditorKit.FontFamilyAction(
				"font-family-Monospaced", "Monospaced"));
		font.addSeparator();
		menu.add(font);

		mbar.add(menu);
		setJMenuBar(mbar);
	}

	private void initTextPane() {
		// doc = new DefaultStyledDocument();
		this.txtObservacoes = new JTextPane();
		// JScrollPane scroll = new JScrollPane(txtObservacoes);
		// getContentPane().add(scroll);
		addbutons();
		this.itemtam.doClick();
		this.txtObservacoes.setText(this.padrao());
	}

	private String padrao() {
		return "principal{ \n\n\n\n}\n";
	}

	private void addbutons() {
		setLayout(new BorderLayout());

		final JPanel pnlPrincipal = new JPanel();
		pnlPrincipal.setLayout(new BorderLayout());
		add(pnlPrincipal, BorderLayout.CENTER);

		final JScrollPane scrollObservacoes = new JScrollPane();
		pnlPrincipal.add(scrollObservacoes, BorderLayout.CENTER);
		// this.txtObservacoes = new JTextPane();

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

		if (this.negritoIcone != null)
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
		btnlexico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saida = "";
				palavra = txtObservacoes.getText();
				lexico.setInput(palavra);
				try {
					Token t = null;
					while ((t = lexico.nextToken()) != null) {
						saida = saida + "\n" + t.toString();
					}
					lexicamente = true;
				} catch (LexicalError er) {
					saida = saida + " (err) " + er.getMessage() + " (pos) "
							+ er.getPosition();
					lexicamente = false;
				}

				JOptionPane.showMessageDialog(null, saida);
				saida = "";
				ultima_palavra = "";
				palavra = "";
			}
		});
		pnlControles.add(btnlexico);

		final JButton btnsintatico = new JButton();
		btnsintatico.setBounds(0, 80, 100, 20);
		btnsintatico.setText("Sintatico");
		btnsintatico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lexicamente) {
					saida = "";
					palavra = txtObservacoes.getText();
					lexico.setInput(palavra);

					Sintatico sintatico = new Sintatico();
					Semantico semantico = new Semantico();
					try {
						sintatico.parse(lexico, semantico);
						JOptionPane.showMessageDialog(null,
								"Codigo Sintaticamente Correto!");
						sintaticamente = true;
						// JOptionPane.showInputDialog(palavra);
					} catch (LexicalError er) {
						saida = saida + " (err_lexico) " + er.getMessage()
								+ " (pos) " + er.getPosition();
						sintaticamente = false;
					} catch (SyntaticError er) {
						saida = saida + " (err_sintatico) " + er.getMessage()
								+ " (pos) " + er.getPosition();
						sintaticamente = false;
					} catch (SemanticError er) {
						saida = saida + " (err_semantico) " + er.getMessage()
								+ " (pos) " + er.getPosition();
						sintaticamente = false;
					}
					if (!saida.equals(""))
						txtObservacoes.setText(saida);
					saida = "";
					ultima_palavra = "";
					palavra = "";
				}
			}
		});
		pnlControles.add(btnsintatico);

		final JButton btnsemantico = new JButton();
		btnsemantico.setBounds(0, 100, 100, 20);
		btnsemantico.setText("Semantico");
		btnsemantico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sintaticamente && lexicamente) {
					saida = "";
					int flag = 0;
					palavra = txtObservacoes.getText();
					Semantico semantico = new Semantico();
					StringTokenizer st = new StringTokenizer(palavra, "\n");
					while (st.hasMoreTokens()) {
						String linha = (String) st.nextToken();
						StringTokenizer st1 = new StringTokenizer(linha, " ");
						while (st1.hasMoreTokens()) {
							String pal = (String) st1.nextToken();
							lexico.setInput(pal);
							try {
								Token t = null;
								while ((t = lexico.nextToken()) != null) {
									String tmp = t.getLexeme();
									if (tmp.equals("read"))
										flag = 1;
									else if (tmp.equals("writeln"))
										flag = 2;
									else if (tmp.equals("numeros")
											|| tmp.equals("real"))
										flag = 3;
									else if (tmp.equals("letras"))
										flag = 4;
									else if (tmp.equals("repita"))
										flag = 5;
									else if (flag == 1) {
										t = lexico.nextToken();
										saida = saida+ semantico.converttoJava(1002,t.getLexeme());
										t = lexico.nextToken();
										flag = 0;
									} else if (flag == 2) {
										t = lexico.nextToken();
										saida = saida+ semantico.converttoJava(1003,t.getLexeme());
										flag = 0;
									} else if (flag == 3) {
										saida = saida+ semantico.converttoJava(1004,t.getLexeme());
										flag = 0;
									} else if (flag == 4) {
										saida = saida+ semantico.converttoJava(1005,t.getLexeme());
										flag = 0;
									} else if (flag == 5) {
										t = lexico.nextToken();
										saida = saida+ semantico.converttoJava(1006,t.getLexeme());
										t = lexico.nextToken();
										flag = 0;
									} else
										saida = saida+ semantico.converttoJava(t.getId(), tmp);
								}
								saida = saida + " ";
								semanticamente = true;
							} catch (LexicalError er) {
								saida = saida + " (err) " + er.getMessage()+ " (pos) " + er.getPosition();
								semanticamente = false;
								sintaticamente = false;
								lexicamente = false;
								break;
							}

						}
						saida = saida + "\n\t";

					}
					saida = saida + "\n}\n";
					txtComplilado = saida;
					saida = "";
					ultima_palavra = "";
					palavra = "";
					JOptionPane.showMessageDialog(null,
							semanticamente ? "Semanticamente Correto"
									: "Semanticamente Incorreto");
				}
			}
		});
		pnlControles.add(btnsemantico);

		final JButton btnsalvar = new JButton();
		btnsalvar.setBounds(0, 120, 100, 20);
		btnsalvar.setText("Salvar");
		btnsalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sintaticamente && lexicamente && semanticamente) {
					salvar("Principal.java", txtComplilado, false);
					salvo = true;
					JOptionPane.showMessageDialog(null, "Salvo");
				}
			}
		});
		pnlControles.add(btnsalvar);

		final JButton btncompilar = new JButton();
		btncompilar.setBounds(0, 140, 100, 20);
		btncompilar.setText("Compilar");
		btncompilar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lexicamente && sintaticamente && semanticamente && salvo) {
					try {
						File arq = new File("Principal.class");
						if (arq.exists()) {
							arq.delete();
						}
						arq = new File("Principal.java");

						if (arq.exists()) {
							Process p = Runtime.getRuntime().exec(
									"javac Principal.java");
							;// exec("C:/Program Files/Java/jdk1.6.0_20/bin/javac Principal.java");

							try {
								p.waitFor();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							arq.delete();
							arq = new File("Principal.class");
							if (arq.exists()) {
								JOptionPane.showMessageDialog(null,
										"Arquivo Compilado!");
								compilado = true;
							} else {
								JOptionPane.showMessageDialog(null,
										"Arquivo não foi Compilado!");
								semanticamente = false;
								sintaticamente = false;
								lexicamente = false;
								salvo = false;
							}

						} else {
							JOptionPane.showMessageDialog(null,
									"Arquivo não foi criado!");
							semanticamente = false;
							sintaticamente = false;
							lexicamente = false;
							salvo = false;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
						semanticamente = false;
						sintaticamente = false;
						lexicamente = false;
						salvo = false;
					}
				}
			}
		});
		pnlControles.add(btncompilar);

		final JButton btnrodar = new JButton(action);
		btnrodar.setBounds(0, 160, 100, 20);
		btnrodar.setText("Rodar");
		btnrodar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (compilado) {
					try {
						File arq = new File("Principal.class");
						if (arq.exists()) {
							Runtime.getRuntime().exec("java Principal");
							;// exec("C:/Program Files/Java/jdk1.6.0_20/bin/java Principal");
						} else {
							JOptionPane.showMessageDialog(null,
									"Arquivo não foi Compilado!");
							semanticamente = false;
							sintaticamente = false;
							lexicamente = false;
							salvo = false;
							compilado = false;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		pnlControles.add(btnrodar);

	}

	public static void salvar(String arquivo, String conteudo, boolean adicionar) {
		FileWriter fw;
		try {
			fw = new FileWriter(arquivo, adicionar);
			fw.write(conteudo);
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}