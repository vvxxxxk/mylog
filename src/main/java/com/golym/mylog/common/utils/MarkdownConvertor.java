package com.golym.mylog.common.utils;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

public class MarkdownConvertor {

    public static String convertMarkdownToPlainText(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);

        // Custom renderer to handle special cases
        CustomTextContentRenderer renderer = new CustomTextContentRenderer();
        document.accept(renderer);
        return renderer.getText();
    }

    private static class CustomTextContentRenderer extends AbstractVisitor {
        private StringBuilder textContent = new StringBuilder();

        @Override
        public void visit(Paragraph paragraph) {
            visitChildren(paragraph);
            textContent.append("\n");
        }

        @Override
        public void visit(Text text) {
            textContent.append(text.getLiteral());
        }

        @Override
        public void visit(Heading heading) {
            textContent.append(heading.getLevel() == 3 ? "\n" : "");
            visitChildren(heading);
            textContent.append("\n");
        }

        @Override
        public void visit(BlockQuote blockQuote) {
            // textContent.append("> ");
            visitChildren(blockQuote);
            textContent.append("\n");
        }

        @Override
        public void visit(SoftLineBreak softLineBreak) {
            textContent.append("\n");
        }

        @Override
        public void visit(HardLineBreak hardLineBreak) {
            textContent.append("\n");
        }

        @Override
        protected void visitChildren(Node node) {
            Node child = node.getFirstChild();
            while (child != null) {
                child.accept(this);
                child = child.getNext();
            }
        }

        public String getText() {
            return textContent.toString().trim();
        }
    }

    public static void main(String[] args) {
        String markdown = "# Hello World\nThis is a **bold** text.";
        String plainText = convertMarkdownToPlainText(markdown);
        System.out.println(plainText);
    }
}
