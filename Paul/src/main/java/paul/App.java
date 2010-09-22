package paul;

import java.io.FileInputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Hello world!
 *
 */
public class App 
{
	private static class DummyHandler implements org.xml.sax.ContentHandler{

		private StringBuffer buf = new StringBuffer();
		
		private void flushBuf(){
			if(buf.length()==0)
				return;
			
			System.out.println(buf.toString());
			buf=new StringBuffer();
		}
		
		public void setDocumentLocator(Locator locator) {
			// TODO Auto-generated method stub
			
		}

		public void startDocument() throws SAXException {
			
			
		}

		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		public void endPrefixMapping(String prefix) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			flushBuf();
			System.out.println("<"+qName+">");
			
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			flushBuf();
			System.out.println("</"+qName+">");
			
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			buf.append(ch, start,length);
		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		public void processingInstruction(String target, String data)
				throws SAXException {
			// TODO Auto-generated method stub
			
		}

		public void skippedEntity(String name) throws SAXException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
    public static void main( String[] args ) throws Exception
    {
    	ContentHandler textHandler = new BodyContentHandler();
        FileInputStream stream = new FileInputStream("c:/test.docx");
        Parser parser = new AutoDetectParser();
        ParseContext context = new ParseContext();
        
        parser.parse(stream, new DummyHandler(), new Metadata(), context);
        stream.close();
        
       // System.out.println("Content:"+textHandler);
        
    }
}
