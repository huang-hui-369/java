package me;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import antlrgen.HelloBaseVisitor;
import antlrgen.HelloLexer;
import antlrgen.HelloParser;

public class MyHelloVisitor extends HelloBaseVisitor<String > {
	
	@Override
	public String visitR(HelloParser.RContext ctx) { 
		visitId(ctx.ID());
		System.out.format("r.h;%s\n", ctx.getChild(0).getText());
		return ctx.getChild(0).getText();
	}
	
	public String visitId(TerminalNode id) { 
		System.out.format("r.id;%s\n", id.getText());
		return id.getText();
	}
	
	@Override
	public String visitChildren(RuleNode node) {
		for(int i=0;i<node.getChildCount();i++) {
			ParseTree item = node.getChild(i);
			if(item.getChildCount()>0) {
				item.accept(this);
			} else {
				System.out.println(item.getText());
			}
		}
		
		return node.getText();
	}
	
	public static void main(String[] args) {
		String str = "hello abc\t\r\n";
		
		CodePointCharStream input = CharStreams.fromString(str);

		HelloLexer lexer = new HelloLexer(input);

		// Get a list of matched tokens
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Pass the tokens to the parser
		HelloParser parser = new HelloParser(tokens);
		
		ParseTree tree = parser.r();

		System.out.println(tree.toStringTree(parser));
		
		MyHelloVisitor v = new MyHelloVisitor();
//		tree.accept(v);
		v.visit(tree);
	
		
	}
}
