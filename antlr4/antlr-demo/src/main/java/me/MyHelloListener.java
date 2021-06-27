package me;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import antlrgen.HelloBaseListener;
import antlrgen.HelloLexer;
import antlrgen.HelloParser;

public class MyHelloListener extends HelloBaseListener {

	@Override
	public void enterR(HelloParser.RContext ctx) {
		// 获取整个语句
		System.out.format("getText:%s\n", ctx.getText());
		// 获取子节点个数
		System.out.format("getChildCount:%s\n", ctx.getChildCount());
		// 获取ID的内容
		System.out.format("ID().getText():%s\n", ctx.ID().getText());
		// 获取start token内容
		System.out.format("start token:%s\n", ctx.start.getText());
		// 获取start token index
		System.out.format("start token index:%s\n", ctx.start.getTokenIndex());
		// 获取start token type
		System.out.format("start token type:%s\n", ctx.start.getType());
		// 获取stop token index
		System.out.format("stop token index:%s\n", ctx.start.getStopIndex());
		// 获取stop token text
		System.out.format("stop token:%s\n", ctx.stop.getText());
		// 获取stop token index
		System.out.format("stop token index:%s\n", ctx.stop.getTokenIndex());
		// 获取type=1,index=0 token
		System.out.format("token 1,0:%s\n", ctx.getToken(1, 0).getText());
	}
	
	@Override
	public void exitR(HelloParser.RContext ctx) { 
		System.out.println("exitR");
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
		 // 创建一个能触发回调函数的语法分析树遍历器
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        // 将监听器和语法树传入walk方法，该方法会遍历语法树触发回调
        parseTreeWalker.walk(new MyHelloListener(), tree);
		
	}
	
}
