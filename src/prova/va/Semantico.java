package prova.va;
import java.util.Vector;

public class Semantico implements Constants {
	Vector<String> v_letras = new Vector<String>();
	Vector<String> v_numeros = new Vector<String>();

	public void executeAction(int action, Token token) throws SemanticError {
		System.out.println("Ação #" + action + ", Token: " + token);
	}

	public boolean ver_vt(String var) {
		for (int i = 0; i < v_letras.size(); i++) {
			if (v_letras.get(i).equals(var))
				return true;
		}
		return false;
	}

	public String converttoJava(int val, String lex) {
		switch (val) {
		case 2:
			return lex;
		case 3:
			return lex;
		case 4:
			return lex;
		case 5:
			return lex;
		case 6:
			return lex;
		case 7:
			return lex;
		case 8:
		case 9:
		case 10:
		case 11:
			return lex;
		case 12:
			return lex;
		case 13:
			return lex;
		case 14:
			return lex;
		case 15:
			return lex;
		case 16:
			return lex;
		case 17:
			return lex;
		case 18:
			return "if";
		case 19:
			return "";
		case 20:
			return "else";
		case 24:
			return "import javax.swing.JOptionPane;\npublic class Principal {\npublic static void main(String[] args)";
		case 25:
			return "while";
		case 26:
			return "";
		case 27:
		case 28:
			return lex;
		case 1002:
			if (ver_vt(lex))
				return lex
						+ "=JOptionPane.showInputDialog(\"entre com a Palavrra\")";
			else
				return lex
						+ "=Double.parseDouble(JOptionPane.showInputDialog(\"entre com o Numero\"))";
		case 1003:
			return "JOptionPane.showMessageDialog(null," + lex;
		case 1004:
			v_numeros.add(lex);
			return "double " + lex;
		case 1005:
			v_letras.add(lex);
			return "String " + lex;
		case 1006:
			return "for ( int i = 0; i < " + lex + " ; i++ )";
		}

		return "";
	}
}
