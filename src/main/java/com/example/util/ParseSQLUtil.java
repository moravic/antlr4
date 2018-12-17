package com.example.util;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.List;

public class ParseSQLUtil {
    /**
     * find files
     */
    public static void parse() throws Exception {
        /*
         * classloader
         */
        final ClassLoader classLoader = getClassLoader();
        final String lexerClassName = "PlSqlLexer";
        final String parserClassName = "PlSqlParser";
        final String listenerClassName = "AntlrSelectListener";
        /*
         * get the classes we need
         */
        final Class<? extends Lexer> lexerClass = classLoader.loadClass(lexerClassName).asSubclass(Lexer.class);
        final Class<? extends Parser> parserClass = classLoader.loadClass(parserClassName).asSubclass(Parser.class);
        final Class<? extends ParseTreeListener> listenerClass = classLoader.loadClass(listenerClassName).asSubclass(ParseTreeListener.class);

        final Constructor<?> lexerConstructor = lexerClass.getConstructor(CharStream.class);
        final Constructor<?> parserConstructor = parserClass.getConstructor(TokenStream.class);

        final List<File> exampleFiles = FileUtil.getAllFiles("examples-sql-script");
        final String fileEncoding = "UTF-8";
        final boolean verbose = false;
        final boolean showTree = false;
        final String entryPoint = "sql_script";

        final CaseInsensitiveType caseInsensitiveType = CaseInsensitiveType.UPPER;

        for (final File grammarFile : exampleFiles) {
            System.out.println("Parsing :" + grammarFile.getAbsolutePath());
            CharStream antlrFileStream;
            if (caseInsensitiveType == CaseInsensitiveType.None) {
                antlrFileStream = CharStreams.fromPath(grammarFile.toPath(), Charset.forName(fileEncoding));
            } else {
                antlrFileStream = new AntlrCaseInsensitiveFileStream(grammarFile.getAbsolutePath(), fileEncoding, caseInsensitiveType);
            }

            Lexer lexer = (Lexer) lexerConstructor.newInstance(antlrFileStream);
            Vocabulary voc = lexer.getVocabulary();
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            if (verbose) {
                tokens.fill();
                for (final Token tok : tokens.getTokens()) {
                    String displayName = voc.getDisplayName(tok.getType());
                    String literalName = voc.getLiteralName(tok.getType());
                    String symbolicName = voc.getSymbolicName(tok.getType());
                    System.out.println(tok);
                    System.out.println(displayName);
                    System.out.println(literalName);
                    System.out.println(symbolicName);
                }
            }

            /*
             * get parser
             */
            Parser parser = (Parser) parserConstructor.newInstance(tokens);
            /*final Method method = parserClass.getMethod(entryPoint);
            ParserRuleContext parserRuleContext = (ParserRuleContext) method.invoke(parser);
            *//*
             * show the tree
             *//*
            if (showTree) {
                final String lispTree = Trees.toStringTree(parserRuleContext, parser);
                System.out.println(lispTree);
            }

            Token t = parserRuleContext.getStart();
            int ttype = t.getType();
            System.out.println(voc.getDisplayName(ttype));
            System.out.println("Parent count " + parserRuleContext.getChildCount());
            System.out.flush();
            for (int i = 0; i < parserRuleContext.getChildCount(); i++) {
                inOrderTraversal(parserRuleContext.getChild(i));
            }*/

            ParseTreeListener treeListener = listenerClass.newInstance();

            /*final Method methodListener = listenerClass.getMethod("printSelect", new Class[]{String.class});
            Class[] parTypes = methodListener.getParameterTypes();

            methodListener.invoke(treeListener, "create table t as select serio, 50 from dual");
*/
            final Method methodSelectStatement = parserClass.getMethod("sql_script");
            ParserRuleContext parserRuleContext2 = (ParserRuleContext) methodSelectStatement.invoke(parser);

            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(treeListener, parserRuleContext2);

        }
    }

    /**
     * get a classloader that can find the files we need
     */
    public static ClassLoader getClassLoader() throws MalformedURLException, ClassNotFoundException {
        final URL antlrGeneratedURL = new File("./target/classes").toURI().toURL();
        final URL[] urls = new URL[] { antlrGeneratedURL };
        return new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
    }

    private static void inOrderTraversal(ParseTree parent) {

        System.out.println("Parent " + parent.getText());
        System.out.flush();

        // Iterate over all child nodes of `parent`.
        for (int i = 0; i < parent.getChildCount(); i++) {

            // Get the i-th child node of `parent`.
            ParseTree child = parent.getChild(i);

            if (child instanceof TerminalNode) {
                // We found a leaf/terminal, add its Token to our list.
                TerminalNode node = (TerminalNode) child;
                Token t = node.getSymbol();
                int ttype = t.getType();
                System.out.println(t.getText());
                System.out.flush();
            }
            else {
                // No leaf/terminal node, recursively call this method.
                inOrderTraversal(child);
            }
        }
    }
}

